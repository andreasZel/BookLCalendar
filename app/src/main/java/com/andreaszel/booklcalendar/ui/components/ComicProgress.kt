package com.andreaszel.booklcalendar.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.progressBarRangeInfo
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.andreaszel.booklcalendar.ui.theme.Dimens
import com.andreaszel.booklcalendar.ui.theme.Palette
import com.andreaszel.booklcalendar.ui.theme.comic

@Composable
fun ComicProgress(progress: Float, modifier: Modifier = Modifier) {
    val value = progress.coerceIn(0f, 1f)
    Box(modifier = modifier.fillMaxWidth().height(18.dp).comic(Palette.White, radius = Dimens.RadiusPill, shadow = 0.dp).semantics { progressBarRangeInfo = ProgressBarRangeInfo(value, 0f..1f) }) {
        if (value > 0f) Box(modifier = Modifier.fillMaxWidth(value).fillMaxHeight().background(Palette.Coral))
    }
}
