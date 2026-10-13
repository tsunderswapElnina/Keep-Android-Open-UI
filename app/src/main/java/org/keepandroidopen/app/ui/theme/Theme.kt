package org.keepandroidopen.app.ui.theme

import android.graphics.Color as AndroidColor
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialExpressiveTheme
import androidx.compose.material3.MotionScheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import org.keepandroidopen.app.settings.Accent
import org.keepandroidopen.app.settings.ThemeMode
import kotlin.math.roundToInt

private val LightColors = lightColorScheme(
    primary = md_light_primary,
    onPrimary = md_light_onPrimary,
    primaryContainer = md_light_primaryContainer,
    onPrimaryContainer = md_light_onPrimaryContainer,
    secondary = md_light_secondary,
    onSecondary = md_light_onSecondary,
    secondaryContainer = md_light_secondaryContainer,
    onSecondaryContainer = md_light_onSecondaryContainer,
    tertiary = md_light_tertiary,
    onTertiary = md_light_onTertiary,
    tertiaryContainer = md_light_tertiaryContainer,
    onTertiaryContainer = md_light_onTertiaryContainer,
    error = md_light_error,
    onError = md_light_onError,
    errorContainer = md_light_errorContainer,
    onErrorContainer = md_light_onErrorContainer,
    background = md_light_background,
    onBackground = md_light_onBackground,
    surface = md_light_surface,
    onSurface = md_light_onSurface,
    surfaceVariant = md_light_surfaceVariant,
    onSurfaceVariant = md_light_onSurfaceVariant,
    outline = md_light_outline,
    outlineVariant = md_light_outlineVariant,
    inverseSurface = md_light_inverseSurface,
    inverseOnSurface = md_light_inverseOnSurface,
    inversePrimary = md_light_inversePrimary,
    scrim = md_light_scrim,
)

private val DarkColors = darkColorScheme(
    primary = md_dark_primary,
    onPrimary = md_dark_onPrimary,
    primaryContainer = md_dark_primaryContainer,
    onPrimaryContainer = md_dark_onPrimaryContainer,
    secondary = md_dark_secondary,
    onSecondary = md_dark_onSecondary,
    secondaryContainer = md_dark_secondaryContainer,
    onSecondaryContainer = md_dark_onSecondaryContainer,
    tertiary = md_dark_tertiary,
    onTertiary = md_dark_onTertiary,
    tertiaryContainer = md_dark_tertiaryContainer,
    onTertiaryContainer = md_dark_onTertiaryContainer,
    error = md_dark_error,
    onError = md_dark_onError,
    errorContainer = md_dark_errorContainer,
    onErrorContainer = md_dark_onErrorContainer,
    background = md_dark_background,
    onBackground = md_dark_onBackground,
    surface = md_dark_surface,
    onSurface = md_dark_onSurface,
    surfaceVariant = md_dark_surfaceVariant,
    onSurfaceVariant = md_dark_onSurfaceVariant,
    outline = md_dark_outline,
    outlineVariant = md_dark_outlineVariant,
    inverseSurface = md_dark_inverseSurface,
    inverseOnSurface = md_dark_inverseOnSurface,
    inversePrimary = md_dark_inversePrimary,
    scrim = md_dark_scrim,
)

// The Material 3 expressive shape scale, with the extra "increased" stops.
private val KaoShapes = Shapes(
    extraSmall = RoundedCornerShape(8.dp),
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(24.dp),
    largeIncreased = RoundedCornerShape(32.dp),
    extraLarge = RoundedCornerShape(36.dp),
    extraLargeIncreased = RoundedCornerShape(44.dp),
    extraExtraLarge = RoundedCornerShape(52.dp),
)

private fun hueOf(color: Color): Float {
    val hsv = FloatArray(3)
    AndroidColor.colorToHSV(color.toArgb(), hsv)
    return hsv[0]
}

private fun rotateHue(color: Color, degrees: Float): Color {
    val hsv = FloatArray(3)
    AndroidColor.colorToHSV(color.toArgb(), hsv)
    if (hsv[1] < 0.05f) return color
    hsv[0] = ((hsv[0] + degrees) % 360f + 360f) % 360f
    val alpha = (color.alpha * 255f).roundToInt().coerceIn(0, 255)
    return Color(AndroidColor.HSVToColor(alpha, hsv))
}

private fun ColorScheme.recoloredTo(accentHue: Float): ColorScheme {
    val delta = accentHue - hueOf(primary)
    fun r(c: Color) = rotateHue(c, delta)
    return copy(
        primary = r(primary),
        onPrimary = r(onPrimary),
        primaryContainer = r(primaryContainer),
        onPrimaryContainer = r(onPrimaryContainer),
        secondary = r(secondary),
        onSecondary = r(onSecondary),
        secondaryContainer = r(secondaryContainer),
        onSecondaryContainer = r(onSecondaryContainer),
        tertiary = r(tertiary),
        onTertiary = r(onTertiary),
        tertiaryContainer = r(tertiaryContainer),
        onTertiaryContainer = r(onTertiaryContainer),
        inversePrimary = r(inversePrimary),
    )
}

private fun ColorScheme.asAmoled(): ColorScheme = copy(
    background = Color.Black,
    surface = Color.Black,
    surfaceContainerLowest = Color.Black,
    surfaceContainerLow = Color(0xFF070707),
    surfaceContainer = Color(0xFF0D0D0D),
    surfaceContainerHigh = Color(0xFF151515),
    surfaceContainerHighest = Color(0xFF1C1C1C),
    surfaceVariant = Color(0xFF1C1C1C),
)

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun KeepAndroidOpenTheme(
    themeMode: ThemeMode = ThemeMode.SYSTEM,
    accent: Accent = Accent.DEFAULT,
    systemDark: Boolean = androidx.compose.foundation.isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val isDark = when (themeMode) {
        ThemeMode.SYSTEM -> systemDark
        ThemeMode.LIGHT -> false
        ThemeMode.DARK, ThemeMode.AMOLED -> true
    }
    var scheme = if (isDark) DarkColors else LightColors
    if (themeMode == ThemeMode.AMOLED) scheme = scheme.asAmoled()
    if (accent != Accent.DEFAULT) scheme = scheme.recoloredTo(accent.hue)

    MaterialExpressiveTheme(
        colorScheme = scheme,
        motionScheme = MotionScheme.expressive(),
        shapes = KaoShapes,
        typography = KaoTypography,
        content = content,
    )
}
