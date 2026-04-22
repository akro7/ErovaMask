package io.github.seyud.weave.ui.deny

import android.annotation.SuppressLint
import android.content.pm.ApplicationInfo
import android.content.pm.ComponentInfo
import android.content.pm.PackageManager
import android.content.pm.PackageManager.GET_ACTIVITIES
import android.content.pm.PackageManager.GET_PROVIDERS
import android.content.pm.PackageManager.GET_RECEIVERS
import android.content.pm.PackageManager.GET_SERVICES
import android.content.pm.PackageManager.MATCH_DISABLED_COMPONENTS
import android.content.pm.PackageManager.MATCH_UNINSTALLED_PACKAGES
import android.content.pm.ServiceInfo
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import androidx.core.os.ProcessCompat
import io.github.seyud.weave.core.ktx.getLabel
import java.util.Locale
import java.util.TreeSet

class CmdlineListItem(line: String) {
    val packageName: String
    val process: String

    init {
        val split = line.split(Regex("\\|"), 2)
        packageName = split[0]
        process = split.getOrElse(1) { packageName }
    }
}

const val ISOLATED_MAGIC = "isolated"

data class DenyListAppInfo(
    val applicationInfo: ApplicationInfo,
    val label: String,
    val packageName: String,
    val isSystemApp: Boolean,
    val isApp: Boolean,
    val hasDirectEntry: Boolean,
) : Comparable<DenyListAppInfo> {
    override fun compareTo(other: DenyListAppInfo) = comparator.compare(this, other)

    companion object {
        private val comparator = compareBy<DenyListAppInfo>(
            { it.label.lowercase(Locale.ROOT) },
            { it.packageName }
        )
    }
}

data class ProcessInfo(
    val name: String,
    val packageName: String,
    val isEnabled: Boolean,
) {
    val isIsolated = packageName == ISOLATED_MAGIC
    val isAppZygote = name.endsWith("_zygote")
    val defaultSelection = isIsolated || isAppZygote || name == packageName
    val displayName = if (isIsolated) "(isolated) $name" else name
}

fun buildDenyListAppInfo(
    applicationInfo: ApplicationInfo,
    pm: PackageManager,
    denyList: List<CmdlineListItem>,
): DenyListAppInfo {
    return DenyListAppInfo(
        applicationInfo = applicationInfo,
        label = applicationInfo.getLabel(pm),
        packageName = applicationInfo.packageName,
        isSystemApp = applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM != 0,
        isApp = ProcessCompat.isApplicationUid(applicationInfo.uid),
        hasDirectEntry = denyList.any { it.packageName == applicationInfo.packageName },
    )
}

fun loadAppIcon(pm: PackageManager, appInfo: DenyListAppInfo): Drawable {
    return runCatching { appInfo.applicationInfo.loadIcon(pm) }.getOrDefault(pm.defaultActivityIcon)
}

@SuppressLint("InlinedApi")
fun fetchProcesses(
    pm: PackageManager,
    appInfo: DenyListAppInfo,
    denyList: List<CmdlineListItem>,
): List<ProcessInfo> {
    val info = appInfo.applicationInfo
    val packageDenyList = denyList.filter {
        it.packageName == info.packageName || it.packageName == ISOLATED_MAGIC
    }

    fun createProcess(name: String, pkg: String = info.packageName) =
        ProcessInfo(name, pkg, packageDenyList.any { it.process == name && it.packageName == pkg })

    fun ComponentInfo.getProcName(): String = processName
        ?: applicationInfo.processName
        ?: applicationInfo.packageName

    fun ServiceInfo.isIsolated() = (flags and ServiceInfo.FLAG_ISOLATED_PROCESS) != 0
    fun ServiceInfo.useAppZygote() = (flags and ServiceInfo.FLAG_USE_APP_ZYGOTE) != 0

    fun Array<out ComponentInfo>?.toProcessList() =
        orEmpty().map { createProcess(it.getProcName()) }

    fun Array<ServiceInfo>?.toServiceProcesses() = orEmpty().map {
        if (it.isIsolated()) {
            if (it.useAppZygote()) {
                val proc = info.processName ?: info.packageName
                createProcess("${proc}_zygote")
            } else {
                val proc = if (SDK_INT >= Build.VERSION_CODES.Q) {
                    "${it.getProcName()}:${it.name}"
                } else {
                    it.getProcName()
                }
                createProcess(proc, ISOLATED_MAGIC)
            }
        } else {
            createProcess(it.getProcName())
        }
    }

    val flag = MATCH_DISABLED_COMPONENTS or MATCH_UNINSTALLED_PACKAGES or
        GET_ACTIVITIES or GET_SERVICES or GET_RECEIVERS or GET_PROVIDERS
    val packageInfo = try {
        pm.getPackageInfo(info.packageName, flag)
    } catch (e: Exception) {
        pm.getPackageArchiveInfo(info.sourceDir, flag) ?: return emptyList()
    }

    val processSet = TreeSet<ProcessInfo>(compareBy({ it.name }, { it.isIsolated }))
    processSet += packageInfo.activities.toProcessList()
    processSet += packageInfo.services.toServiceProcesses()
    processSet += packageInfo.receivers.toProcessList()
    processSet += packageInfo.providers.toProcessList()
    return processSet.toList()
}
