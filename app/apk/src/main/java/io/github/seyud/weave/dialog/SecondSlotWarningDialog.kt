package io.github.seyud.weave.dialog

import io.github.seyud.weave.core.R
import io.github.seyud.weave.events.DialogBuilder
import io.github.seyud.weave.ui.dialog.WeaveDialog

class SecondSlotWarningDialog : DialogBuilder {

    override fun build(dialog: WeaveDialog) {
        dialog.apply {
            setTitle(android.R.string.dialog_alert_title)
            setMessage(R.string.install_inactive_slot_msg)
            setButton(WeaveDialog.ButtonType.POSITIVE) {
                text = android.R.string.ok
            }
            setCancelable(true)
        }
    }
}
