package com.andreaszel.booklcalendar.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.andreaszel.booklcalendar.data.Streak
import com.andreaszel.booklcalendar.ui.icons.ComicIcons
import com.andreaszel.booklcalendar.ui.theme.Palette

@Composable
fun StreakPill(streak: Streak, modifier: Modifier = Modifier) {
    InfoPill(text = "${streak.current}", icon = ComicIcons.Flame, fill = Palette.Yellow, fontSize = 12.sp, iconSize = 14.dp, vertical = 2.dp, horizontal = 8.dp, modifier = modifier.clearAndSetSemantics { contentDescription = streakLabel(streak) })
}

fun streakLabel(streak: Streak): String {
    val current = if (streak.current == 1) "1-day reading streak" else "${streak.current}-day reading streak"
    return if (streak.best > streak.current) "$current, best ${streak.best} days" else current
}
