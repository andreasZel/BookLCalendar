package com.andreaszel.booklcalendar.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun ComicIcon(icon: ImageVector, size: Dp = 22.dp, tint: Color? = null, modifier: Modifier = Modifier) {
    Image(imageVector = icon, contentDescription = null, colorFilter = tint?.let { ColorFilter.tint(it) }, modifier = modifier.size(size))
}
