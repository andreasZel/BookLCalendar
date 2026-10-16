package com.andreaszel.booklcalendar.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import com.andreaszel.booklcalendar.ui.theme.Dimens
import com.andreaszel.booklcalendar.ui.theme.comic

fun Modifier.pressable(fill: Color, shadow: Dp, radius: Dp = Dimens.Radius, enabled: Boolean = true, role: Role? = null, onClick: () -> Unit): Modifier = composed {
    val source = remember { MutableInteractionSource() }
    val pressed by source.collectIsPressedAsState()
    val push = with(LocalDensity.current) { if (pressed) shadow.toPx() else 0f }
    graphicsLayer { translationX = push; translationY = push }
        .comic(fill, radius = radius, shadow = if (pressed) Dp(0f) else shadow)
        .clickable(interactionSource = source, indication = null, enabled = enabled, role = role, onClick = onClick)
}
