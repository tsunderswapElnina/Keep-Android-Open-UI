package org.keepandroidopen.app.settings

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat

enum class ThemeMode {
    SYSTEM,
    LIGHT,
    DARK,
    AMOLED,
}

/** Accent seeds are a hue angle in degrees used to re-tint the brand palette. */
enum class Accent(val hue: Float) {
    DEFAULT(-1f),
    RED(2f),
    ORANGE(28f),
    YELLOW(48f),
    GREEN(128f),
    TEAL(174f),
    BLUE(218f),
    PURPLE(272f),
    PINK(332f),
}

data class AppSettings(
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
    val accent: Accent = Accent.DEFAULT,
) {
    companion object {
        val DEFAULT = AppSettings()
    }
}

object SettingsStore {
    private const val PREFS = "kao_settings"
    private const val KEY_THEME = "theme_mode"
    private const val KEY_ACCENT = "accent"

    private fun prefs(context: Context): SharedPreferences =
        context.applicationContext.getSharedPreferences(PREFS, Context.MODE_PRIVATE)

    fun read(context: Context): AppSettings {
        val p = prefs(context)
        val theme = runCatching {
            ThemeMode.valueOf(p.getString(KEY_THEME, ThemeMode.SYSTEM.name)!!)
        }.getOrDefault(ThemeMode.SYSTEM)
        val accent = runCatching {
            Accent.valueOf(p.getString(KEY_ACCENT, Accent.DEFAULT.name)!!)
        }.getOrDefault(Accent.DEFAULT)
        return AppSettings(theme, accent)
    }

    fun writeTheme(context: Context, mode: ThemeMode) {
        prefs(context).edit().putString(KEY_THEME, mode.name).apply()
    }

    fun writeAccent(context: Context, accent: Accent) {
        prefs(context).edit().putString(KEY_ACCENT, accent.name).apply()
    }
}

object LanguageSettings {

    /** Returns the BCP-47 tag currently applied, or "" when following the system. */
    fun currentTag(): String =
        AppCompatDelegate.getApplicationLocales().get(0)?.toLanguageTag().orEmpty()

    fun apply(tag: String) {
        val locales = if (tag.isBlank()) {
            LocaleListCompat.getEmptyLocaleList()
        } else {
            LocaleListCompat.forLanguageTags(tag)
        }
        AppCompatDelegate.setApplicationLocales(locales)
    }
}

object SupportedLocales {

    /**
     * The languages offered in the in-app picker and declared in locales_config.xml.
     * Keep this in sync with res/xml/locales_config.xml and the generated values-* folders.
     */
    val tags: List<String> = listOf(
        "af", "am", "ar", "as", "az", "be", "bg", "bn", "bs", "ca",
        "cs", "cy", "da", "de", "de-AT", "de-CH", "el", "en", "en-AU", "en-CA",
        "en-GB", "en-IN", "en-NZ", "en-ZA", "es", "es-419", "es-MX", "es-US", "et", "eu",
        "fa", "fi", "fil", "fr", "fr-CA", "fr-CH", "ga", "gl", "gu", "ha",
        "he", "hi", "hr", "hu", "hy", "id", "is", "it", "ja", "jv",
        "ka", "kk", "km", "kn", "ko", "ky", "lo", "lt", "lv", "mk",
        "ml", "mn", "mr", "ms", "my", "nb", "ne", "nl", "or", "pa",
        "pl", "pt", "pt-BR", "pt-PT", "ro", "ru", "si", "sk", "sl", "sq",
        "sr", "sv", "sw", "ta", "te", "th", "tl", "tr", "uk", "ur",
        "uz", "vi", "zh-CN", "zh-HK", "zh-TW", "zu",
    )
}
