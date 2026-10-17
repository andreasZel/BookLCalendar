package com.andreaszel.booklcalendar.ui.calendar

import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp

fun Modifier.monthSwipe(onPrevious: () -> Unit, onNext: () -> Unit): Modifier = pointerInput(Unit) {
    val threshold = 64.dp.toPx()
    var drag = 0f
    detectHorizontalDragGestures(onDragStart = { drag = 0f }, onDragEnd = { if (drag > threshold) onPrevious() else if (drag < -threshold) onNext() }) { change, amount ->
        change.consume()
        drag += amount
    }
}
