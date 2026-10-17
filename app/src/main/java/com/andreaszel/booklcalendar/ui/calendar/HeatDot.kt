package com.andreaszel.booklcalendar.ui.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.andreaszel.booklcalendar.ui.theme.Palette

@Composable
fun HeatDot(level: Int, size: Dp) {
    if (level <= 0) {
        Spacer(modifier = Modifier.size(size))
        return
    }
    Box(modifier = Modifier.size(size).background(heatFills[level.coerceAtMost(3)], CircleShape).border(2.dp, Palette.Ink, CircleShape))
}

private val heatFills = listOf(Color.Transparent, Color(0xFFFFF0A8), Palette.Yellow, Palette.Coral)
