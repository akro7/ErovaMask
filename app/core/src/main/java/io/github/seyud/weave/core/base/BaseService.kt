package io.github.seyud.weave.core.base

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import io.github.seyud.weave.core.patch

open class BaseService : Service() {
    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base.patch())
    }
    override fun onBind(intent: Intent?): IBinder? = null
}
