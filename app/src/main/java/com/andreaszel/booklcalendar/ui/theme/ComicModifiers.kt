package com.andreaszel.booklcalendar.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.constrainHeight
import androidx.compose.ui.unit.constrainWidth
import androidx.compose.ui.unit.dp

fun Modifier.comicShadow(shadow: Dp, radius: Dp = Dimens.Radius): Modifier = if (shadow <= 0.dp) this else drawBehind {
    drawRoundRect(Palette.Ink, topLeft = Offset(shadow.toPx(), shadow.toPx()), size = size, cornerRadius = CornerRadius(radius.toPx()))
}

fun Modifier.comic(fill: Color, radius: Dp = Dimens.Radius, shadow: Dp = 3.dp, outline: Dp = Dimens.Outline, edge: Color = Palette.Ink): Modifier {
    val shape = RoundedCornerShape(radius)
    return comicShadow(shadow, radius).clip(shape).background(fill).border(outline, edge, shape)
}

fun Modifier.dashedComic(fill: Color, radius: Dp = Dimens.RadiusBig, outline: Dp = Dimens.Outline): Modifier = clip(RoundedCornerShape(radius)).background(fill).drawBehind {
    val w = outline.toPx()
    drawRoundRect(Palette.Ink, topLeft = Offset(w / 2, w / 2), size = Size(size.width - w, size.height - w), cornerRadius = CornerRadius(radius.toPx() - w / 2), style = Stroke(width = w, pathEffect = PathEffect.dashPathEffect(floatArrayOf(w * 3, w * 2))))
}

fun Modifier.focusRing(focused: Boolean, radius: Dp = Dimens.Radius): Modifier = if (!focused) this else drawBehind {
    val gap = 4.dp.toPx()
    drawRoundRect(Palette.Sky, topLeft = Offset(-gap, -gap), size = Size(size.width + gap * 2, size.height + gap * 2), cornerRadius = CornerRadius(radius.toPx() + gap), style = Stroke(width = 3.dp.toPx()))
}

fun Modifier.dotted(): Modifier = drawBehind {
    val step = 10.dp.toPx()
    val dot = 1.6.dp.toPx()
    val color = Palette.Ink.copy(alpha = 0.22f)
    var y = step / 2
    while (y < size.height) {
        var x = step / 2
        while (x < size.width) {
            drawCircle(color, dot, Offset(x, y))
            x += step
        }
        y += step
    }
}

fun Modifier.topRule(outline: Dp = Dimens.Outline, color: Color = Palette.Ink): Modifier = drawBehind {
    drawRect(color, size = Size(size.width, outline.toPx()))
}

fun Modifier.bleed(outline: Dp = Dimens.Outline): Modifier = layout { measurable, constraints ->
    val extra = outline.roundToPx()
    val maxHeight = if (constraints.hasBoundedHeight) constraints.maxHeight + extra else constraints.maxHeight
    val placeable = measurable.measure(constraints.copy(minWidth = constraints.minWidth + extra * 2, maxWidth = constraints.maxWidth + extra * 2, minHeight = constraints.minHeight + extra, maxHeight = maxHeight))
    layout(constraints.constrainWidth(placeable.width - extra * 2), constraints.constrainHeight(placeable.height - extra)) {
        placeable.place(-extra, 0)
    }
}
