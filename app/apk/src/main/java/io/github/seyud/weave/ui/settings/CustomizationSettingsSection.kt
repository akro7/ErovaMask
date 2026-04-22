package io.github.seyud.weave.ui.settings

import android.app.Activity
import android.os.Build
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.AddToHomeScreen
import androidx.compose.material.icons.rounded.Adb
import androidx.compose.material.icons.rounded.Apps
import androidx.compose.material.icons.rounded.AspectRatio
import androidx.compose.material.icons.rounded.BlurOn
import androidx.compose.material.icons.rounded.CallToAction
import androidx.compose.material.icons.rounded.Colorize
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Language
import androidx.compose.material.icons.rounded.Palette
import androidx.compose.material.icons.rounded.RoundedCorner
import androidx.compose.material.icons.rounded.WaterDrop
import androidx.compose.ui.unit.dp
import io.github.seyud.weave.core.App as CoreApp
import io.github.seyud.weave.core.Config
import io.github.seyud.weave.core.integration.AppIconManager
import io.github.seyud.weave.core.integration.AppIconVariant
import io.github.seyud.weave.ui.theme.MonetPresetPalette
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.Slider
import top.yukonga.miuix.kmp.basic.SliderDefaults
import top.yukonga.miuix.kmp.basic.SmallTitle
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.preference.ArrowPreference
import top.yukonga.miuix.kmp.preference.OverlayDropdownPreference
import top.yukonga.miuix.kmp.preference.SwitchPreference
import top.yukonga.miuix.kmp.theme.MiuixTheme.colorScheme
import io.github.seyud.weave.core.R as CoreR

@Composable
internal fun CustomizationSettingsSection(
    viewModel: SettingsViewModel,
    visibility: SettingsVisibility,
    onNavigateToAppLanguage: () -> Unit,
    onAddShortcut: () -> Unit,
) {
    val context = LocalContext.current
    val activity = context as Activity
    val res = context.resources
    val showScaleDialog = rememberSaveable { mutableStateOf(false) }
    var enableBlur by rememberSaveable { mutableStateOf(Config.enableBlur) }
    var enableFloatingBottomBar by rememberSaveable { mutableStateOf(Config.enableFloatingBottomBar) }
    var enableFloatingBottomBarBlur by rememberSaveable { mutableStateOf(Config.enableFloatingBottomBarBlur) }
    var enableSmoothCorner by rememberSaveable { mutableStateOf(Config.enableSmoothCorner) }
    var homeLayoutMode by rememberSaveable { mutableIntStateOf(Config.homeLayoutMode) }
    var themeMode by rememberSaveable { mutableIntStateOf(Config.colorMode) }
    var sliderValue by rememberSaveable { mutableStateOf(Config.pageScale) }
    val themeModeSystem = stringResource(CoreR.string.settings_theme_mode_system)
    val themeModeLight = stringResource(CoreR.string.settings_theme_mode_light)
    val themeModeDark = stringResource(CoreR.string.settings_theme_mode_dark)
    val themeModeMonetSystem = stringResource(CoreR.string.settings_theme_mode_monet_system)
    val themeModeMonetLight = stringResource(CoreR.string.settings_theme_mode_monet_light)
    val themeModeMonetDark = stringResource(CoreR.string.settings_theme_mode_monet_dark)
    val themeItems = remember(
        themeModeSystem,
        themeModeLight,
        themeModeDark,
        themeModeMonetSystem,
        themeModeMonetLight,
        themeModeMonetDark,
    ) {
        listOf(
            themeModeSystem,
            themeModeLight,
            themeModeDark,
            themeModeMonetSystem,
            themeModeMonetLight,
            themeModeMonetDark,
        )
    }
    val keyColorDefault = stringResource(CoreR.string.settings_key_color_default)
    val colorRed = stringResource(CoreR.string.color_red)
    val colorPink = stringResource(CoreR.string.color_pink)
    val colorPurple = stringResource(CoreR.string.color_purple)
    val colorDeepPurple = stringResource(CoreR.string.color_deep_purple)
    val colorIndigo = stringResource(CoreR.string.color_indigo)
    val colorBlue = stringResource(CoreR.string.color_blue)
    val colorCyan = stringResource(CoreR.string.color_cyan)
    val colorTeal = stringResource(CoreR.string.color_teal)
    val colorGreen = stringResource(CoreR.string.color_green)
    val colorYellow = stringResource(CoreR.string.color_yellow)
    val colorAmber = stringResource(CoreR.string.color_amber)
    val colorOrange = stringResource(CoreR.string.color_orange)
    val colorBrown = stringResource(CoreR.string.color_brown)
    val colorBlueGrey = stringResource(CoreR.string.color_blue_grey)
    val colorSakura = stringResource(CoreR.string.color_sakura)
    val colorItems = remember(
        keyColorDefault,
        colorRed,
        colorPink,
        colorPurple,
        colorDeepPurple,
        colorIndigo,
        colorBlue,
        colorCyan,
        colorTeal,
        colorGreen,
        colorYellow,
        colorAmber,
        colorOrange,
        colorBrown,
        colorBlueGrey,
        colorSakura,
    ) {
        listOf(
            keyColorDefault,
            colorRed,
            colorPink,
            colorPurple,
            colorDeepPurple,
            colorIndigo,
            colorBlue,
            colorCyan,
            colorTeal,
            colorGreen,
            colorYellow,
            colorAmber,
            colorOrange,
            colorBrown,
            colorBlueGrey,
            colorSakura,
        )
    }
    val colorValues = remember {
        listOf(0) + MonetPresetPalette.presetKeyColors
    }
    val homeLayoutClassic = stringResource(CoreR.string.settings_home_layout_classic)
    val homeLayoutWeavsk = stringResource(CoreR.string.settings_home_layout_weavsk)
    val iconCurrent = stringResource(CoreR.string.settings_app_icon_current)
    val iconLegacyWeave = stringResource(CoreR.string.settings_app_icon_legacy_weave)
    val iconLegacyMask = stringResource(CoreR.string.settings_app_icon_legacy_mask)
    val homeLayoutItems = remember(homeLayoutClassic, homeLayoutWeavsk) {
        listOf(homeLayoutClassic, homeLayoutWeavsk)
    }
    val appIconVariants = remember { AppIconVariant.entries }
    val appIconItems = remember(iconCurrent, iconLegacyWeave, iconLegacyMask) {
        listOf(iconCurrent, iconLegacyWeave, iconLegacyMask)
    }
    var appIconIndex by rememberSaveable {
        mutableIntStateOf(appIconVariants.indexOf(AppIconManager.currentVariant()).coerceAtLeast(0))
    }
    val supportsAppIconSelection = remember(context.packageName) {
        AppIconManager.isSupported(context)
    }

    SmallTitle(text = stringResource(CoreR.string.settings_customization))
    Card(modifier = Modifier.fillMaxWidth()) {
        OverlayDropdownPreference(
            title = stringResource(CoreR.string.settings_theme),
            summary = stringResource(CoreR.string.settings_theme_summary),
            items = themeItems,
            startAction = {
                Icon(
                    Icons.Rounded.Palette,
                    modifier = Modifier.padding(end = 6.dp),
                    contentDescription = null,
                    tint = colorScheme.onBackground,
                )
            },
            selectedIndex = themeMode,
            onSelectedIndexChange = { index ->
                Config.colorMode = index
                themeMode = index
            },
        )

        AnimatedVisibility(visible = themeMode in 3..5) {
            var keyColorIndex by rememberSaveable {
                mutableIntStateOf(
                    colorValues.indexOf(Config.keyColor).takeIf { it >= 0 } ?: 0,
                )
            }

            OverlayDropdownPreference(
                title = stringResource(CoreR.string.settings_key_color),
                summary = stringResource(CoreR.string.settings_key_color_summary),
                items = colorItems,
                startAction = {
                    Icon(
                        Icons.Rounded.Colorize,
                        modifier = Modifier.padding(end = 6.dp),
                        contentDescription = null,
                        tint = colorScheme.onBackground,
                    )
                },
                selectedIndex = keyColorIndex,
                onSelectedIndexChange = { index ->
                    Config.keyColor = colorValues[index]
                    keyColorIndex = index
                },
            )
        }

        OverlayDropdownPreference(
            title = stringResource(CoreR.string.settings_home_layout),
            summary = stringResource(CoreR.string.settings_home_layout_summary),
            items = homeLayoutItems,
            startAction = {
                Icon(
                    Icons.Rounded.Home,
                    modifier = Modifier.padding(end = 6.dp),
                    contentDescription = null,
                    tint = colorScheme.onBackground,
                )
            },
            selectedIndex = homeLayoutMode.coerceIn(0, homeLayoutItems.lastIndex),
            onSelectedIndexChange = { index ->
                Config.homeLayoutMode = index
                homeLayoutMode = Config.homeLayoutMode
            },
        )

        if (supportsAppIconSelection) {
            OverlayDropdownPreference(
                title = stringResource(CoreR.string.settings_app_icon_title),
                summary = stringResource(CoreR.string.settings_app_icon_summary),
                items = appIconItems,
                startAction = {
                    Icon(
                        Icons.Rounded.Apps,
                        modifier = Modifier.padding(end = 6.dp),
                        contentDescription = null,
                        tint = colorScheme.onBackground,
                    )
                },
                selectedIndex = appIconIndex.coerceIn(0, appIconItems.lastIndex),
                onSelectedIndexChange = { index ->
                    val variant = appIconVariants[index]
                    if (viewModel.updateAppIcon(context, variant)) {
                        appIconIndex = index
                    }
                },
            )
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            SwitchPreference(
                title = stringResource(CoreR.string.settings_enable_blur),
                summary = stringResource(CoreR.string.settings_enable_blur_summary),
                startAction = {
                    Icon(
                        Icons.Rounded.WaterDrop,
                        modifier = Modifier.padding(end = 6.dp),
                        contentDescription = stringResource(CoreR.string.settings_enable_blur),
                        tint = colorScheme.onBackground,
                    )
                },
                checked = enableBlur,
                onCheckedChange = {
                    Config.enableBlur = it
                    enableBlur = Config.enableBlur
                    enableFloatingBottomBarBlur = Config.enableFloatingBottomBarBlur
                },
            )
        }

        SwitchPreference(
            title = stringResource(CoreR.string.settings_floating_bottom_bar),
            summary = stringResource(CoreR.string.settings_floating_bottom_bar_summary),
            startAction = {
                Icon(
                    Icons.Rounded.CallToAction,
                    modifier = Modifier.padding(end = 6.dp),
                    contentDescription = stringResource(CoreR.string.settings_floating_bottom_bar),
                    tint = colorScheme.onBackground,
                )
            },
            checked = enableFloatingBottomBar,
            onCheckedChange = {
                Config.enableFloatingBottomBar = it
                enableFloatingBottomBar = it
            },
        )

        AnimatedVisibility(
            visible = enableFloatingBottomBar && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU,
        ) {
            SwitchPreference(
                title = stringResource(CoreR.string.settings_enable_glass),
                summary = stringResource(CoreR.string.settings_enable_glass_summary),
                startAction = {
                    Icon(
                        Icons.Rounded.BlurOn,
                        modifier = Modifier.padding(end = 6.dp),
                        contentDescription = stringResource(CoreR.string.settings_enable_glass),
                        tint = colorScheme.onBackground,
                    )
                },
                checked = enableFloatingBottomBarBlur,
                onCheckedChange = {
                    Config.enableFloatingBottomBarBlur = it
                    enableFloatingBottomBarBlur = Config.enableFloatingBottomBarBlur
                    enableBlur = Config.enableBlur
                },
            )
        }

        SwitchPreference(
            title = stringResource(CoreR.string.settings_smooth_corner),
            summary = stringResource(CoreR.string.settings_smooth_corner_summary),
            startAction = {
                Icon(
                    Icons.Rounded.RoundedCorner,
                    modifier = Modifier.padding(end = 6.dp),
                    contentDescription = stringResource(CoreR.string.settings_smooth_corner),
                    tint = colorScheme.onBackground,
                )
            },
            checked = enableSmoothCorner,
            onCheckedChange = {
                Config.enableSmoothCorner = it
                enableSmoothCorner = it
            },
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            var enablePredictiveBack by rememberSaveable { mutableStateOf(Config.enablePredictiveBack) }
            SwitchPreference(
                title = stringResource(CoreR.string.settings_enable_predictive_back),
                summary = stringResource(CoreR.string.settings_enable_predictive_back_summary),
                startAction = {
                    Icon(
                        Icons.Rounded.Adb,
                        modifier = Modifier.padding(end = 6.dp),
                        contentDescription = stringResource(CoreR.string.settings_enable_predictive_back),
                        tint = colorScheme.onBackground,
                    )
                },
                checked = enablePredictiveBack,
                onCheckedChange = {
                    Config.enablePredictiveBack = it
                    enablePredictiveBack = it
                    CoreApp.setEnableOnBackInvokedCallback(context.applicationInfo, it)
                    activity.recreate()
                },
            )
        }

        ArrowPreference(
            title = stringResource(CoreR.string.settings_page_scale),
            summary = stringResource(CoreR.string.settings_page_scale_summary),
            startAction = {
                Icon(
                    Icons.Rounded.AspectRatio,
                    modifier = Modifier.padding(end = 6.dp),
                    contentDescription = stringResource(CoreR.string.settings_page_scale),
                    tint = colorScheme.onBackground,
                )
            },
            endActions = {
                Text(
                    text = "${(sliderValue * 100).toInt()}%",
                    color = colorScheme.onSurfaceVariantActions,
                )
            },
            onClick = { showScaleDialog.value = !showScaleDialog.value },
            holdDownState = showScaleDialog.value,
            bottomAction = {
                Slider(
                    value = sliderValue,
                    onValueChange = { sliderValue = it },
                    onValueChangeFinished = {
                        Config.pageScale = sliderValue
                    },
                    valueRange = 0.8f..1.1f,
                    showKeyPoints = true,
                    keyPoints = listOf(0.8f, 0.9f, 1f, 1.1f),
                    magnetThreshold = 0.01f,
                    hapticEffect = SliderDefaults.SliderHapticEffect.Step,
                )
            },
        )
        ScaleDialog(
            showDialog = showScaleDialog,
            scaleState = { Config.pageScale },
            onScaleChange = {
                Config.pageScale = it
                sliderValue = it
            },
        )

        ArrowPreference(
            title = stringResource(CoreR.string.language),
            summary = appLanguageSummary(res),
            startAction = {
                Icon(
                    Icons.Rounded.Language,
                    modifier = Modifier.padding(end = 6.dp),
                    contentDescription = null,
                    tint = colorScheme.onBackground,
                )
            },
            onClick = onNavigateToAppLanguage,
        )

        if (visibility.showAddShortcut) {
            ArrowPreference(
                title = stringResource(CoreR.string.add_shortcut_title),
                summary = stringResource(CoreR.string.setting_add_shortcut_summary),
                startAction = {
                    Icon(
                        Icons.AutoMirrored.Rounded.AddToHomeScreen,
                        modifier = Modifier.padding(end = 6.dp),
                        contentDescription = null,
                        tint = colorScheme.onBackground,
                    )
                },
                onClick = onAddShortcut,
            )
        }
    }
}
