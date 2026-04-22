package io.github.seyud.weave.core.base

import android.app.job.JobService
import android.content.Context
import io.github.seyud.weave.core.patch

abstract class BaseJobService : JobService() {
    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base.patch())
    }
}
