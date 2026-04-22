package io.github.seyud.weave.ui.settings

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Block
import androidx.compose.material.icons.rounded.Memory
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.Storage
import androidx.compose.ui.unit.dp
import com.topjohnwu.superuser.Shell
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.SmallTitle
import top.yukonga.miuix.kmp.preference.ArrowPreference
import top.yukonga.miuix.kmp.preference.SwitchPreference
import top.yukonga.miuix.kmp.theme.MiuixTheme.colorScheme
import io.github.seyud.weave.core.Config
import io.github.seyud.weave.core.Info
import io.github.seyud.weave.core.R as CoreR

@Composable
internal fun MagiskSettingsSection(
    viewModel: SettingsViewModel,
    visibility: SettingsVisibility,
) {
    if (!visibility.showMagiskSection) {
        return
    }

    SmallTitle(text = stringResource(CoreR.string.magisk))
    Card(modifier = Modifier.fillMaxWidth()) {
        ArrowPreference(
            title = stringResource(CoreR.string.settings_hosts_title),
            summary = stringResource(CoreR.string.settings_hosts_summary),
            startAction = {
                Icon(
                    Icons.Rounded.Storage,
                    modifier = Modifier.padding(end = 6.dp),
                    contentDescription = null,
                    tint = colorScheme.onBackground,
                )
            },
            onClick = { viewModel.createHosts() },
        )

        if (visibility.showZygisk) {
            var zygiskEnabled by rememberSaveable { mutableStateOf(Config.zygisk) }
            val zygiskMismatch = zygiskEnabled != Info.isZygiskEnabled
            SwitchPreference(
                title = stringResource(CoreR.string.zygisk),
                summary = if (zygiskMismatch) {
                    stringResource(CoreR.string.reboot_apply_change)
                } else {
                    stringResource(CoreR.string.settings_zygisk_summary)
                },
                checked = zygiskEnabled,
                onCheckedChange = {
                    Config.zygisk = it
                    zygiskEnabled = it
                },
                startAction = {
                    Icon(
                        Icons.Rounded.Memory,
                        modifier = Modifier.padding(end = 6.dp),
                        contentDescription = null,
                        tint = colorScheme.onBackground,
                    )
                },
            )

            var denyListEnabled by rememberSaveable { mutableStateOf(Config.denyList) }
            SwitchPreference(
                title = stringResource(CoreR.string.settings_denylist_title),
                summary = stringResource(CoreR.string.settings_denylist_summary),
                checked = denyListEnabled,
                onCheckedChange = { checked ->
                    val cmd = if (checked) "enable" else "disable"
                    Shell.cmd("magisk --denylist $cmd").submit { result ->
                        if (result.isSuccess) {
                            Config.denyList = checked
                            denyListEnabled = checked
                        }
                    }
                },
                startAction = {
                    Icon(
                        Icons.Rounded.Block,
                        modifier = Modifier.padding(end = 6.dp),
                        contentDescription = null,
                        tint = colorScheme.onBackground,
                    )
                },
            )

            ArrowPreference(
                title = stringResource(CoreR.string.settings_denylist_config_title),
                summary = stringResource(CoreR.string.settings_denylist_config_summary),
                startAction = {
                    Icon(
                        Icons.Rounded.Settings,
                        modifier = Modifier.padding(end = 6.dp),
                        contentDescription = null,
                        tint = colorScheme.onBackground,
                    )
                },
                onClick = { viewModel.navigateToDenyListConfig() },
            )
        }
    }
}
