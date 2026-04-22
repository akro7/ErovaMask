package io.github.seyud.weave.core

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.core.content.IntentCompat
import io.github.seyud.weave.core.base.BaseReceiver
import io.github.seyud.weave.core.di.ServiceLocator
import io.github.seyud.weave.core.download.DownloadEngine
import io.github.seyud.weave.core.download.Subject
import io.github.seyud.weave.core.integration.AppNotifications
import io.github.seyud.weave.core.integration.AppShortcuts
import com.topjohnwu.superuser.Shell
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

open class Receiver : BaseReceiver() {

    private val policyDB get() = ServiceLocator.policyDB

    @SuppressLint("InlinedApi")
    private fun getPkg(intent: Intent): String? {
        val pkg = intent.getStringExtra(Intent.EXTRA_PACKAGE_NAME)
        return pkg ?: intent.data?.schemeSpecificPart
    }

    private fun getUid(intent: Intent): Int? {
        val uid = intent.getIntExtra(Intent.EXTRA_UID, -1)
        return if (uid == -1) null else uid
    }

    override fun onReceive(context: Context, intent: Intent?) {
        intent ?: return
        super.onReceive(context, intent)

        @OptIn(DelicateCoroutinesApi::class)
        fun rmPolicy(uid: Int) = GlobalScope.launch {
            policyDB.delete(uid)
        }

        when (intent.action ?: return) {
            DownloadEngine.ACTION -> {
                IntentCompat.getParcelableExtra(
                    intent, DownloadEngine.SUBJECT_KEY, Subject::class.java)?.let {
                        DownloadEngine.start(context, it)
                    }
            }
            Intent.ACTION_PACKAGE_REPLACED -> {
                // This will only work pre-O
                if (Config.suReAuth)
                    getUid(intent)?.let { rmPolicy(it) }
            }
            Intent.ACTION_UID_REMOVED -> {
                getUid(intent)?.let { rmPolicy(it) }
            }
            Intent.ACTION_PACKAGE_FULLY_REMOVED -> {
                getPkg(intent)?.let { Shell.cmd("magisk --denylist rm $it").submit() }
            }
            Intent.ACTION_LOCALE_CHANGED -> AppShortcuts.setupDynamic(context)
            Intent.ACTION_MY_PACKAGE_REPLACED -> {
                @Suppress("DEPRECATION")
                val installer = context.packageManager.getInstallerPackageName(context.packageName)
                if (installer == context.packageName) {
                    AppNotifications.updateDone()
                }
            }
        }
    }
}
