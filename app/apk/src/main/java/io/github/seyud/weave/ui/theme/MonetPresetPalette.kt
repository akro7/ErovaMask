package io.github.seyud.weave.ui.theme

import io.github.seyud.weave.R

internal object MonetPresetPalette {

    private val splashThemesByKeyColor = linkedMapOf(
        0xFFF44336.toInt() to R.style.Theme_WeaveMagisk_Splash_MonetPreset_Red,
        0xFFE91E63.toInt() to R.style.Theme_WeaveMagisk_Splash_MonetPreset_Pink,
        0xFF9C27B0.toInt() to R.style.Theme_WeaveMagisk_Splash_MonetPreset_Purple,
        0xFF673AB7.toInt() to R.style.Theme_WeaveMagisk_Splash_MonetPreset_DeepPurple,
        0xFF3F51B5.toInt() to R.style.Theme_WeaveMagisk_Splash_MonetPreset_Indigo,
        0xFF2196F3.toInt() to R.style.Theme_WeaveMagisk_Splash_MonetPreset_Blue,
        0xFF00BCD4.toInt() to R.style.Theme_WeaveMagisk_Splash_MonetPreset_Cyan,
        0xFF009688.toInt() to R.style.Theme_WeaveMagisk_Splash_MonetPreset_Teal,
        0xFF4FAF50.toInt() to R.style.Theme_WeaveMagisk_Splash_MonetPreset_Green,
        0xFFFFEB3B.toInt() to R.style.Theme_WeaveMagisk_Splash_MonetPreset_Yellow,
        0xFFFFC107.toInt() to R.style.Theme_WeaveMagisk_Splash_MonetPreset_Amber,
        0xFFFF9800.toInt() to R.style.Theme_WeaveMagisk_Splash_MonetPreset_Orange,
        0xFF795548.toInt() to R.style.Theme_WeaveMagisk_Splash_MonetPreset_Brown,
        0xFF607D8F.toInt() to R.style.Theme_WeaveMagisk_Splash_MonetPreset_BlueGrey,
        0xFFFF9CA8.toInt() to R.style.Theme_WeaveMagisk_Splash_MonetPreset_Sakura,
    )

    val presetKeyColors: List<Int> = splashThemesByKeyColor.keys.toList()

    fun contains(keyColor: Int): Boolean = splashThemesByKeyColor.containsKey(keyColor)

    fun splashThemeResFor(keyColor: Int): Int? = splashThemesByKeyColor[keyColor]
}
