package com.andreaszel.booklcalendar.ui.shelf

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.calculateZoom
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput

fun Modifier.pinchSteps(onZoomIn: () -> Unit, onZoomOut: () -> Unit): Modifier = pointerInput(Unit) {
    awaitEachGesture {
        awaitFirstDown(requireUnconsumed = false, pass = PointerEventPass.Initial)
        var scale = 1f
        do {
            val event = awaitPointerEvent(PointerEventPass.Initial)
            val fingers = event.changes.count { it.pressed }
            if (fingers >= 2) {
                scale *= event.calculateZoom()
                if (scale > 1.3f) {
                    onZoomIn()
                    scale = 1f
                } else if (scale < 0.77f) {
                    onZoomOut()
                    scale = 1f
                }
                event.changes.forEach { it.consume() }
            }
        } while (event.changes.any { it.pressed })
    }
}
