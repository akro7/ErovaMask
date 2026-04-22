package io.github.seyud.weave.core.download

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Parcelable
import androidx.core.net.toUri
import io.github.seyud.weave.core.Info
import io.github.seyud.weave.core.model.UpdateInfo
import io.github.seyud.weave.core.model.module.OnlineModule
import io.github.seyud.weave.core.utils.MediaStoreUtils
import io.github.seyud.weave.core.integration.AppNotifications
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import java.io.File
import java.util.UUID

abstract class Subject : Parcelable {

    abstract val url: String
    abstract val file: Uri
    abstract val title: String
    abstract val notifyId: Int
    open val autoLaunch: Boolean get() = true

    open fun pendingIntent(context: Context): PendingIntent? = null

    abstract class Module : Subject() {
        abstract val module: OnlineModule
        final override val url: String get() = module.zipUrl
        final override val title: String get() = module.downloadFilename
        final override val file by lazy {
            MediaStoreUtils.getFile(title).uri
        }
    }

    @Parcelize
    class App(
        private val json: UpdateInfo = Info.update,
        override val notifyId: Int = AppNotifications.nextId()
    ) : Subject() {
        override val title: String get() = "WeaveMask-${json.version}(${json.versionCode})"
        override val url: String get() = json.link

        @IgnoredOnParcel
        override val file by lazy {
            MediaStoreUtils.getFile("${title}.apk").uri
        }

        @IgnoredOnParcel
        var intent: Intent? = null

        override fun pendingIntent(context: Context): PendingIntent? {
            return (intent ?: createInstallIntent()).toPending(context)
        }

        private fun createInstallIntent(): Intent {
            return Intent(Intent.ACTION_VIEW)
                .setDataAndType(file, "application/vnd.android.package-archive")
                .addFlags(
                    Intent.FLAG_ACTIVITY_NEW_TASK or
                        Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
        }
    }

    @Parcelize
    class Test(
        override val notifyId: Int = AppNotifications.nextId(),
        override val title: String = UUID.randomUUID().toString().substring(0, 6)
    ) : Subject() {
        override val url get() = "https://link.testfile.org/250MB"
        override val file get() = File("/dev/null").toUri()
        override val autoLaunch get() = false
    }

    @SuppressLint("InlinedApi")
    protected fun Intent.toPending(context: Context): PendingIntent {
        return PendingIntent.getActivity(context, notifyId, this,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_ONE_SHOT)
    }
}
