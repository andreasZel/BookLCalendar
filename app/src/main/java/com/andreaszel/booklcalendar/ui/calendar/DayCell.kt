package com.andreaszel.booklcalendar.ui.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.andreaszel.booklcalendar.ui.DateLabels
import com.andreaszel.booklcalendar.ui.theme.Dimens
import com.andreaszel.booklcalendar.ui.theme.Palette
import com.andreaszel.booklcalendar.ui.theme.TextStyles
import java.time.LocalDate

@Composable
fun DayCell(date: LocalDate, pages: Int?, today: Boolean, picked: Boolean, future: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val shape = RoundedCornerShape(Dimens.Radius)
    val label = "${if (today) "Today, " else ""}${DateLabels.full(date)}, ${if (pages == null) "nothing logged" else pagesLabel(pages)}"
    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(3.dp, Alignment.CenterVertically), modifier = modifier.height(54.dp).padding(2.dp).clip(shape).background(if (picked) Palette.Lilac else Color.Transparent).then(if (today) Modifier.border(Dimens.Outline, Palette.Ink, shape) else Modifier).clickable(enabled = !future, role = Role.Button, onClick = onClick).semantics { contentDescription = label; selected = picked }) {
        Text("${date.dayOfMonth}", style = TextStyles.Strong.copy(fontSize = 15.sp, color = if (future) Palette.Muted.copy(alpha = 0.5f) else Palette.Ink), modifier = Modifier.clearAndSetSemantics {})
        HeatDot(level = heatLevel(pages), size = 10.dp)
    }
}
