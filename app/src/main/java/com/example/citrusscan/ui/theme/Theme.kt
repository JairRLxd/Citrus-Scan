package com.example.citrusscan.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColors = lightColorScheme(
    primary = CitrusOrange,
    onPrimary = CitrusCard,
    primaryContainer = CitrusPeel,
    onPrimaryContainer = CitrusText,
    secondary = CitrusLeaf,
    onSecondary = CitrusCard,
    secondaryContainer = CitrusCream,
    onSecondaryContainer = CitrusText,
    tertiary = CitrusGold,
    onTertiary = CitrusText,
    background = CitrusCream,
    onBackground = CitrusText,
    surface = CitrusCard,
    onSurface = CitrusText,
    error = CitrusError,
)

private val DarkColors = darkColorScheme(
    primary = CitrusGold,
    secondary = CitrusLeaf,
    tertiary = CitrusOrange,
)

@Composable
fun CitrusScanTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = CitrusScanTypography,
        content = content,
    )
}
