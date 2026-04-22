package io.github.seyud.weave.ui

import android.os.Build
import io.github.seyud.weave.R
import io.github.seyud.weave.ui.theme.MonetPresetPalette

/**
 * محرك حل سمات الشاشة الافتتاحية (Splash Screen)
 * تم التعديل لدعم منطق الـ Premium UI المتوافق مع إصدارات أندرويد المختلفة
 */
internal object SplashThemeResolver {

    enum class Kind {
        DEFAULT,
        MONET_SYSTEM,
        MONET_PRESET,
    }

    data class Spec(
        val kind: Kind,
        val dark: Boolean,
        val keyColor: Int,
    )

    /**
     * تحديد مواصفات السمة بناءً على إعدادات اللون وإصدار النظام
     */
    fun resolve(
        colorMode: Int,
        keyColor: Int,
        sdkInt: Int = Build.VERSION.SDK_INT,
        isSystemDark: Boolean,
    ): Spec {
        // تحديد ما إذا كان الوضع الليلي مفعلًا
        // 2, 5: وضع ليلي دائم | 0, 3: يتبع النظام
        val dark = when (colorMode) {
            2, 5 -> true
            0, 3 -> isSystemDark
            else -> false
        }

        return when (colorMode) {
            3 -> { // وضع Monet (التكيفي)
                when {
                    MonetPresetPalette.contains(keyColor) -> {
                        Spec(kind = Kind.MONET_PRESET, dark = dark, keyColor = keyColor)
                    }
                    sdkInt >= Build.VERSION_CODES.S -> {
                        Spec(kind = Kind.MONET_SYSTEM, dark = dark, keyColor = 0)
                    }
                    else -> {
                        Spec(kind = Kind.DEFAULT, dark = dark, keyColor = 0)
                    }
                }
            }

            4, 5 -> { // وضع الألوان المحددة مسبقاً (Preset)
                if (MonetPresetPalette.contains(keyColor)) {
                    Spec(kind = Kind.MONET_PRESET, dark = dark, keyColor = keyColor)
                } else {
                    Spec(kind = Kind.DEFAULT, dark = dark, keyColor = 0)
                }
            }

            else -> Spec(kind = Kind.DEFAULT, dark = dark, keyColor = 0)
        }
    }

    /**
     * استخراج مورد السمة (Theme Resource ID) النهائي
     */
    fun resolveThemeRes(spec: Spec): Int {
        return when (spec.kind) {
            Kind.DEFAULT -> {
                // إذا كان الوضع ليلي، نستخدم سمة الـ Splash الداكنة (تتوافق مع Amoled Black)
                if (spec.dark) R.style.Theme_WeaveMagisk_Splash_Default_Dark 
                else R.style.Theme_WeaveMagisk_Splash_Default
            }
            Kind.MONET_SYSTEM -> {
                if (spec.dark) R.style.Theme_WeaveMagisk_Splash_MonetSystem_Dark
                else R.style.Theme_WeaveMagisk_Splash_MonetSystem
            }
            Kind.MONET_PRESET -> {
                MonetPresetPalette.splashThemeResFor(spec.keyColor, spec.dark)
                    ?: if (spec.dark) R.style.Theme_WeaveMagisk_Splash_Default_Dark 
                       else R.style.Theme_WeaveMagisk_Splash_Default
            }
        }
    }

    /**
     * دالة مساعدة للوصول المباشر للـ ResId
     */
    fun resolveThemeRes(
        colorMode: Int,
        keyColor: Int,
        sdkInt: Int = Build.VERSION.SDK_INT,
        isSystemDark: Boolean,
    ): Int = resolveThemeRes(resolve(colorMode, keyColor, sdkInt, isSystemDark))
}
