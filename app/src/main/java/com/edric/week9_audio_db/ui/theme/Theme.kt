package com.edric.week9_audio_db.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val AudioDbDarkColorScheme = darkColorScheme(
    primary = MusicGreen,
    secondary = MusicGreen,
    background = MusicBg,
    surface = MusicSurface,
    onPrimary = MusicBg,
    onSecondary = MusicBg,
    onBackground = MusicTextPrimary,
    onSurface = MusicTextPrimary,
    onSurfaceVariant = MusicTextMuted,
    outline = MusicBorder,
    error = MusicRed,
)

@Composable
fun Week9audiodbTheme(
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = AudioDbDarkColorScheme,
        typography = Typography,
        content = content,
    )
}
