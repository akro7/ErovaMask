package io.github.seyud.weave.ui.webui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import top.yukonga.miuix.kmp.theme.MiuixTheme
import java.util.concurrent.atomic.AtomicReference

object MonetColorsProvider {

    private val colorsCss = AtomicReference("")

    fun getColorsCss(): String {
        return colorsCss.get()
    }

    @Composable
    fun UpdateCss() {
        val colorScheme = MiuixTheme.colorScheme
        SideEffect {
            colorsCss.set(
                mapOf(
                    "primary" to colorScheme.primary.toCssValue(),
                    "onPrimary" to colorScheme.onPrimary.toCssValue(),
                    "primaryContainer" to colorScheme.primaryContainer.toCssValue(),
                    "onPrimaryContainer" to colorScheme.onPrimaryContainer.toCssValue(),
                    "inversePrimary" to colorScheme.primaryVariant.toCssValue(),
                    "secondary" to colorScheme.secondary.toCssValue(),
                    "onSecondary" to colorScheme.onSecondary.toCssValue(),
                    "secondaryContainer" to colorScheme.secondaryContainer.toCssValue(),
                    "onSecondaryContainer" to colorScheme.onSecondaryContainer.toCssValue(),
                    "tertiary" to colorScheme.tertiaryContainerVariant.toCssValue(),
                    "onTertiary" to colorScheme.tertiaryContainer.toCssValue(),
                    "tertiaryContainer" to colorScheme.tertiaryContainer.toCssValue(),
                    "onTertiaryContainer" to colorScheme.onTertiaryContainer.toCssValue(),
                    "background" to colorScheme.background.toCssValue(),
                    "onBackground" to colorScheme.onBackground.toCssValue(),
                    "surface" to colorScheme.surface.toCssValue(),
                    "tonalSurface" to colorScheme.surfaceContainer.toCssValue(),
                    "onSurface" to colorScheme.onSurface.toCssValue(),
                    "surfaceVariant" to colorScheme.surfaceVariant.toCssValue(),
                    "onSurfaceVariant" to colorScheme.onSurfaceVariantSummary.toCssValue(),
                    "surfaceTint" to colorScheme.primary.toCssValue(),
                    "inverseSurface" to colorScheme.disabledOnSurface.toCssValue(),
                    "inverseOnSurface" to colorScheme.surfaceContainer.toCssValue(),
                    "error" to colorScheme.error.toCssValue(),
                    "onError" to colorScheme.onError.toCssValue(),
                    "errorContainer" to colorScheme.errorContainer.toCssValue(),
                    "onErrorContainer" to colorScheme.onErrorContainer.toCssValue(),
                    "outline" to colorScheme.outline.toCssValue(),
                    "outlineVariant" to colorScheme.dividerLine.toCssValue(),
                    "scrim" to colorScheme.windowDimming.toCssValue(),
                    "surfaceBright" to colorScheme.surface.toCssValue(),
                    "surfaceDim" to colorScheme.surface.toCssValue(),
                    "surfaceContainer" to colorScheme.surfaceContainer.toCssValue(),
                    "surfaceContainerHigh" to colorScheme.surfaceContainerHigh.toCssValue(),
                    "surfaceContainerHighest" to colorScheme.surfaceContainerHighest.toCssValue(),
                    "surfaceContainerLow" to colorScheme.surfaceContainer.toCssValue(),
                    "surfaceContainerLowest" to colorScheme.surfaceContainer.toCssValue(),
                    "filledTonalButtonContentColor" to colorScheme.onPrimaryContainer.toCssValue(),
                    "filledTonalButtonContainerColor" to colorScheme.secondaryContainer.toCssValue(),
                    "filledTonalButtonDisabledContentColor" to colorScheme.onSurfaceVariantSummary.toCssValue(),
                    "filledTonalButtonDisabledContainerColor" to colorScheme.surfaceVariant.toCssValue(),
                    "filledCardContentColor" to colorScheme.onPrimaryContainer.toCssValue(),
                    "filledCardContainerColor" to colorScheme.primaryContainer.toCssValue(),
                    "filledCardDisabledContentColor" to colorScheme.onSurfaceVariantSummary.toCssValue(),
                    "filledCardDisabledContainerColor" to colorScheme.surfaceVariant.toCssValue()
                ).toCssVars()
            )
        }
    }

    private fun Map<String, String>.toCssVars(): String {
        return buildString {
            append(":root {\n")
            for ((key, value) in this@toCssVars) {
                append("  --$key: $value;\n")
            }
            append("}\n")
        }
    }

    private fun Color.toCssValue(): String {
        fun Float.toHex(): String {
            return (this * 255).toInt().coerceIn(0, 255).toString(16).padStart(2, '0')
        }
        return if (alpha == 1f) {
            "#${red.toHex()}${green.toHex()}${blue.toHex()}"
        } else {
            "#${red.toHex()}${green.toHex()}${blue.toHex()}${alpha.toHex()}"
        }
    }
}
