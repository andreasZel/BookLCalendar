package com.andreaszel.booklcalendar.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.andreaszel.booklcalendar.ui.theme.Palette
import com.andreaszel.booklcalendar.ui.theme.TextStyles

@Composable
fun InfoPill(text: String, icon: ImageVector, fill: Color, fontSize: TextUnit, iconSize: Dp, vertical: Dp, horizontal: Dp, modifier: Modifier = Modifier) {
    Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically, modifier = modifier.background(fill, CircleShape).border(2.dp, Palette.Ink, CircleShape).padding(horizontal = horizontal, vertical = vertical)) {
        ComicIcon(icon, size = iconSize)
        Text(text, style = TextStyles.Strong.copy(fontSize = fontSize))
    }
}
