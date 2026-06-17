package org.example.roundapp.core.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightPrimary = Color(0xFF005BAC)
private val LightOnPrimary = Color(0xFFFFFFFF)
private val LightSecondary = Color(0xFF00B4D8)
private val LightBackground = Color(0xFFF8F9FA)
private val LightSurface = Color(0xFFFFFFFF)
private val LightError = Color(0xFFD32F2F)
private val LightOnBackgroundSurface = Color(0xFF1D1D1D)

private val DarkPrimary = Color(0xFF82C3FF)
private val DarkOnPrimary = Color(0xFF001A33)
private val DarkSecondary = Color(0xFF48CAE4)
private val DarkBackground = Color(0xFF121212)
private val DarkSurface = Color(0xFF1E1E1E)
private val DarkError = Color(0xFFCF6679)
private val DarkOnBackgroundSurface = Color(0xFFE0E0E0)

private val LightColors = lightColorScheme(
    primary = LightPrimary,
    onPrimary = LightOnPrimary,
    primaryContainer = LightPrimary,
    onPrimaryContainer = LightOnPrimary,
    inversePrimary = LightPrimary,
    secondary = LightSecondary,
    onSecondary = LightOnPrimary,
    secondaryContainer = LightSecondary,
    onSecondaryContainer = LightOnPrimary,
    tertiary = LightSecondary,
    onTertiary = LightOnPrimary,
    tertiaryContainer = LightSecondary,
    onTertiaryContainer = LightOnPrimary,
    background = LightBackground,
    onBackground = LightOnBackgroundSurface,
    surface = LightSurface,
    onSurface = LightOnBackgroundSurface,
    surfaceVariant = LightSurface,
    onSurfaceVariant = LightOnBackgroundSurface,
    surfaceTint = LightPrimary,
    inverseSurface = LightOnBackgroundSurface,
    inverseOnSurface = LightBackground,
    error = LightError,
    onError = LightOnPrimary,
    errorContainer = LightError,
    onErrorContainer = LightOnPrimary,
    outline = LightOnBackgroundSurface,
    outlineVariant = LightOnBackgroundSurface,
    scrim = LightOnBackgroundSurface,
)

private val DarkColors = darkColorScheme(
    primary = DarkPrimary,
    onPrimary = DarkOnPrimary,
    primaryContainer = DarkPrimary,
    onPrimaryContainer = DarkOnPrimary,
    inversePrimary = DarkPrimary,
    secondary = DarkSecondary,
    onSecondary = DarkOnPrimary,
    secondaryContainer = DarkSecondary,
    onSecondaryContainer = DarkOnPrimary,
    tertiary = DarkSecondary,
    onTertiary = DarkOnPrimary,
    tertiaryContainer = DarkSecondary,
    onTertiaryContainer = DarkOnPrimary,
    background = DarkBackground,
    onBackground = DarkOnBackgroundSurface,
    surface = DarkSurface,
    onSurface = DarkOnBackgroundSurface,
    surfaceVariant = DarkSurface,
    onSurfaceVariant = DarkOnBackgroundSurface,
    surfaceTint = DarkPrimary,
    inverseSurface = DarkOnBackgroundSurface,
    inverseOnSurface = DarkBackground,
    error = DarkError,
    onError = DarkOnPrimary,
    errorContainer = DarkError,
    onErrorContainer = DarkOnPrimary,
    outline = DarkOnBackgroundSurface,
    outlineVariant = DarkOnBackgroundSurface,
    scrim = DarkOnBackgroundSurface,
)

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        content = content,
    )
}
