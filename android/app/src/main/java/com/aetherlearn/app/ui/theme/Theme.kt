package com.aetherlearn.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.aetherlearn.app.data.ThemeMode

private val LightColors = lightColorScheme(
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

private val DarkColors = darkColorScheme(
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

@Composable
fun AetherLearnTheme(
    themeMode: ThemeMode,
    content: @Composable () -> Unit,
) {
    val darkTheme = when (themeMode) {
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
    }
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        content = content,
    )
}
