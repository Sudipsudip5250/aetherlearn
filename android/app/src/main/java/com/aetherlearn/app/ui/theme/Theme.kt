package com.aetherlearn.app.ui.theme

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import com.aetherlearn.app.data.ReadingTheme
import com.aetherlearn.app.data.ThemeMode

private val LightDefault = lightColorScheme(
    primary = Color(0xFF4F46E5),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFEDE9FE),
    onPrimaryContainer = Color(0xFF312E81),
    secondary = Color(0xFF0F766E),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFD1FAE5),
    onSecondaryContainer = Color(0xFF065F46),
    background = Color(0xFFFAF8FF),
    surface = Color(0xFFFAF8FF),
    surfaceVariant = Color(0xFFF3F0FF),
    error = Color(0xFFB42318),
    errorContainer = Color(0xFFFFE8E2),
    onErrorContainer = Color(0xFF7A3418),
)

private val DarkDefault = darkColorScheme(
    primary = Color(0xFFB9B5FF),
    onPrimary = Color(0xFF1C1A4B),
    primaryContainer = Color(0xFF2E2A5A),
    onPrimaryContainer = Color(0xFFE4E0FF),
    secondary = Color(0xFF79D8CA),
    onSecondary = Color(0xFF003731),
    secondaryContainer = Color(0xFF134E4A),
    onSecondaryContainer = Color(0xFFCCFBF1),
    background = Color(0xFF121016),
    surface = Color(0xFF121016),
    surfaceVariant = Color(0xFF1C1830),
    error = Color(0xFFFFB4AB),
    errorContainer = Color(0xFF3A221C),
    onErrorContainer = Color(0xFFFFD4C8),
)

private val LightWarm = lightColorScheme(
    primary = Color(0xFF8A4B2A),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFF3DFC9),
    onPrimaryContainer = Color(0xFF3B2416),
    secondary = Color(0xFF6B4F32),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFE8D4B8),
    onSecondaryContainer = Color(0xFF3A2A18),
    background = Color(0xFFF6EFE4),
    surface = Color(0xFFFBF4E8),
    surfaceVariant = Color(0xFFEDE1D0),
    outlineVariant = Color(0xFFD4C4AE),
    error = Color(0xFFB42318),
    errorContainer = Color(0xFFFFE8E2),
    onErrorContainer = Color(0xFF7A3418),
    onBackground = Color(0xFF3B2A1E),
    onSurface = Color(0xFF3B2A1E),
)

private val DarkWarm = darkColorScheme(
    primary = Color(0xFFE2B48A),
    onPrimary = Color(0xFF3B2416),
    primaryContainer = Color(0xFF4A3224),
    onPrimaryContainer = Color(0xFFF6EFE4),
    secondary = Color(0xFFD4B896),
    onSecondary = Color(0xFF2A1C12),
    secondaryContainer = Color(0xFF3A2A1C),
    onSecondaryContainer = Color(0xFFF0E2CC),
    background = Color(0xFF1C1612),
    surface = Color(0xFF241C16),
    surfaceVariant = Color(0xFF32281E),
    outlineVariant = Color(0xFF5A4634),
    error = Color(0xFFFFB4AB),
    errorContainer = Color(0xFF3A221C),
    onErrorContainer = Color(0xFFFFD4C8),
    onBackground = Color(0xFFF3E6D6),
    onSurface = Color(0xFFF3E6D6),
)

private val LightCool = lightColorScheme(
    primary = Color(0xFF215F8A),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFD5E8F5),
    onPrimaryContainer = Color(0xFF12324A),
    secondary = Color(0xFF2F6B72),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFD2EEF0),
    onSecondaryContainer = Color(0xFF16373A),
    background = Color(0xFFF2F6FA),
    surface = Color(0xFFF7FAFC),
    surfaceVariant = Color(0xFFE3ECF3),
    outlineVariant = Color(0xFFC5D4E0),
    error = Color(0xFFB42318),
    errorContainer = Color(0xFFFFE8E2),
    onErrorContainer = Color(0xFF7A3418),
    onBackground = Color(0xFF1C2833),
    onSurface = Color(0xFF1C2833),
)

private val DarkCool = darkColorScheme(
    primary = Color(0xFF9EC9E8),
    onPrimary = Color(0xFF12324A),
    primaryContainer = Color(0xFF1C3D55),
    onPrimaryContainer = Color(0xFFD5E8F5),
    secondary = Color(0xFF8FD0D6),
    onSecondary = Color(0xFF16373A),
    secondaryContainer = Color(0xFF1C4448),
    onSecondaryContainer = Color(0xFFD2EEF0),
    background = Color(0xFF10161C),
    surface = Color(0xFF151C24),
    surfaceVariant = Color(0xFF1E2833),
    outlineVariant = Color(0xFF3A4A58),
    error = Color(0xFFFFB4AB),
    errorContainer = Color(0xFF3A221C),
    onErrorContainer = Color(0xFFFFD4C8),
    onBackground = Color(0xFFE6EEF5),
    onSurface = Color(0xFFE6EEF5),
)

private val LightHighContrast = lightColorScheme(
    primary = Color(0xFF000000),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFE6E6E6),
    onPrimaryContainer = Color(0xFF000000),
    secondary = Color(0xFF000000),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFD9D9D9),
    onSecondaryContainer = Color(0xFF000000),
    background = Color(0xFFFFFFFF),
    surface = Color(0xFFFFFFFF),
    surfaceVariant = Color(0xFFF2F2F2),
    outline = Color(0xFF000000),
    outlineVariant = Color(0xFF000000),
    error = Color(0xFF9B0000),
    errorContainer = Color(0xFFFFD6D6),
    onErrorContainer = Color(0xFF3B0000),
    onBackground = Color(0xFF000000),
    onSurface = Color(0xFF000000),
)

private val DarkHighContrast = darkColorScheme(
    primary = Color(0xFFFFFFFF),
    onPrimary = Color(0xFF000000),
    primaryContainer = Color(0xFF2A2A2A),
    onPrimaryContainer = Color(0xFFFFFFFF),
    secondary = Color(0xFFFFFFFF),
    onSecondary = Color(0xFF000000),
    secondaryContainer = Color(0xFF2A2A2A),
    onSecondaryContainer = Color(0xFFFFFFFF),
    background = Color(0xFF000000),
    surface = Color(0xFF000000),
    surfaceVariant = Color(0xFF161616),
    outline = Color(0xFFFFFFFF),
    outlineVariant = Color(0xFFFFFFFF),
    error = Color(0xFFFFB4AB),
    errorContainer = Color(0xFF3A221C),
    onErrorContainer = Color(0xFFFFD4C8),
    onBackground = Color(0xFFFFFFFF),
    onSurface = Color(0xFFFFFFFF),
)

internal fun isDarkTheme(themeMode: ThemeMode, systemDark: Boolean): Boolean = when (themeMode) {
    ThemeMode.SYSTEM -> systemDark
    ThemeMode.LIGHT -> false
    ThemeMode.DARK -> true
}

private fun colorsFor(dark: Boolean, reading: ReadingTheme) = when (reading) {
    ReadingTheme.DEFAULT, ReadingTheme.SOFT_PATTERN -> if (dark) DarkDefault else LightDefault
    ReadingTheme.WARM_PAPER -> if (dark) DarkWarm else LightWarm
    ReadingTheme.COOL -> if (dark) DarkCool else LightCool
    ReadingTheme.HIGH_CONTRAST -> if (dark) DarkHighContrast else LightHighContrast
}

@Composable
fun AetherLearnTheme(
    themeMode: ThemeMode,
    readingTheme: ReadingTheme = ReadingTheme.DEFAULT,
    content: @Composable () -> Unit,
) {
    val darkTheme = isDarkTheme(themeMode, isSystemInDarkTheme())
    MaterialTheme(
        colorScheme = colorsFor(darkTheme, readingTheme),
        content = {
            ReadingBackdrop(enabled = readingTheme == ReadingTheme.SOFT_PATTERN, dark = darkTheme) {
                content()
            }
        },
    )
}

@Composable
internal fun ReadingBackdrop(
    enabled: Boolean,
    dark: Boolean,
    content: @Composable () -> Unit,
) {
    if (!enabled) {
        content()
        return
    }
    val paper = if (dark) Color(0xFF121016) else Color(0xFFFAF8FF)
    val dot = if (dark) Color(0x33E4E0FF) else Color(0x334F46E5)
    Box(modifier = Modifier.fillMaxSize().background(paper)) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val step = 28f
            var y = step
            while (y < size.height) {
                var x = step
                while (x < size.width) {
                    drawCircle(color = dot, radius = 1.6f, center = Offset(x, y))
                    x += step
                }
                y += step
            }
        }
        Box(modifier = Modifier.fillMaxSize()) {
            content()
        }
    }
}