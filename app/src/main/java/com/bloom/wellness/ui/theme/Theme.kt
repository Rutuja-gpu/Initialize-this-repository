package com.bloom.wellness.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val BloomColorScheme = lightColorScheme(
    primary = BloomMaroon,
    onPrimary = BloomCream,
    primaryContainer = BloomRoseTint,
    onPrimaryContainer = BloomMaroonDark,
    secondary = BloomGold,
    onSecondary = BloomCream,
    background = BloomCream,
    onBackground = BloomTextPrimary,
    surface = BloomSurface,
    onSurface = BloomTextPrimary,
    surfaceVariant = BloomRoseTint,
    onSurfaceVariant = BloomTextSecondary,
    outline = BloomDivider,
    error = BloomMaroonDark
)

/**
 * Bloom always renders its own warm, light palette regardless of system dark mode —
 * the wellness content (cycle/mood colors) is tuned specifically for the cream/maroon
 * scheme in the Figma file, so we intentionally don't fork a dark variant for this demo.
 */
@Composable
fun BloomTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = BloomColorScheme,
        typography = BloomTypography,
        content = content
    )
}
