package com.andreaszel.booklcalendar.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight

private val colors = lightColorScheme(primary = Palette.Coral, onPrimary = Palette.Ink, primaryContainer = Palette.Sky, onPrimaryContainer = Palette.Ink, secondary = Palette.Lilac, onSecondary = Palette.Ink, background = Palette.Paper, onBackground = Palette.Ink, surface = Palette.White, onSurface = Palette.Ink, surfaceContainerHigh = Palette.White, onSurfaceVariant = Palette.Muted, outline = Palette.Ink, error = Palette.Error)

private val base = Typography()

private val typography = Typography(
    displayLarge = base.displayLarge.copy(fontFamily = Fonts.Fredoka, fontWeight = FontWeight.Bold),
    headlineLarge = base.headlineLarge.copy(fontFamily = Fonts.Fredoka, fontWeight = FontWeight.Bold),
    headlineMedium = base.headlineMedium.copy(fontFamily = Fonts.Fredoka, fontWeight = FontWeight.Bold),
    headlineSmall = base.headlineSmall.copy(fontFamily = Fonts.Fredoka, fontWeight = FontWeight.SemiBold),
    titleLarge = base.titleLarge.copy(fontFamily = Fonts.Fredoka, fontWeight = FontWeight.SemiBold),
    titleMedium = base.titleMedium.copy(fontFamily = Fonts.Fredoka, fontWeight = FontWeight.SemiBold),
    titleSmall = base.titleSmall.copy(fontFamily = Fonts.Fredoka, fontWeight = FontWeight.SemiBold),
    bodyLarge = base.bodyLarge.copy(fontFamily = Fonts.Nunito, fontWeight = FontWeight.Bold),
    bodyMedium = base.bodyMedium.copy(fontFamily = Fonts.Nunito, fontWeight = FontWeight.Bold),
    bodySmall = base.bodySmall.copy(fontFamily = Fonts.Nunito, fontWeight = FontWeight.Bold),
    labelLarge = base.labelLarge.copy(fontFamily = Fonts.Fredoka, fontWeight = FontWeight.SemiBold),
    labelMedium = base.labelMedium.copy(fontFamily = Fonts.Nunito, fontWeight = FontWeight.ExtraBold),
    labelSmall = base.labelSmall.copy(fontFamily = Fonts.Nunito, fontWeight = FontWeight.ExtraBold),
)

@Composable
fun ComicTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = colors, typography = typography, content = content)
}

object TextStyles {
    val Heading = TextStyle(fontFamily = Fonts.Fredoka, fontWeight = FontWeight.Bold, color = Palette.Ink)
    val Label = TextStyle(fontFamily = Fonts.Fredoka, fontWeight = FontWeight.SemiBold, color = Palette.Ink)
    val Body = TextStyle(fontFamily = Fonts.Nunito, fontWeight = FontWeight.Bold, color = Palette.Ink)
    val Strong = TextStyle(fontFamily = Fonts.Nunito, fontWeight = FontWeight.ExtraBold, color = Palette.Ink)
}
