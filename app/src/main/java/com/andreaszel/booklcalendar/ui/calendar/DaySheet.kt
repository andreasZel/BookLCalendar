package com.andreaszel.booklcalendar.ui.calendar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.andreaszel.booklcalendar.ui.DateLabels
import com.andreaszel.booklcalendar.ui.components.ComicIcon
import com.andreaszel.booklcalendar.ui.icons.ComicIcons
import com.andreaszel.booklcalendar.ui.theme.Palette
import com.andreaszel.booklcalendar.ui.theme.TextStyles
import com.andreaszel.booklcalendar.ui.theme.dashedComic
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DaySheet(date: LocalDate, entries: List<DayEntry>, today: LocalDate, onDismiss: () -> Unit, onOpen: (Long) -> Unit) {
    val sheet = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val pages = entries.sumOf { it.log.pagesRead }
    ModalBottomSheet(onDismissRequest = onDismiss, sheetState = sheet, containerColor = Palette.Paper, contentColor = Palette.Ink, shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState()).padding(start = 16.dp, end = 16.dp, bottom = 24.dp)) {
            Column {
                Text(DateLabels.day(date, today), style = TextStyles.Heading.copy(fontSize = 22.sp), modifier = Modifier.semantics { heading() })
                Text(if (entries.isEmpty()) DateLabels.full(date) else "${if (entries.size == 1) "1 book" else "${entries.size} books"} · ${pagesLabel(pages)}", style = TextStyles.Strong.copy(fontSize = 13.sp, color = Palette.Muted))
            }
            if (entries.isEmpty()) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth().dashedComic(Palette.White).padding(horizontal = 20.dp, vertical = 22.dp)) {
                    ComicIcon(ComicIcons.Book, size = 36.dp)
                    Text("No reading logged", textAlign = TextAlign.Center, style = TextStyles.Label.copy(fontSize = 17.sp))
                    Text("Pages you log for this day show up here.", textAlign = TextAlign.Center, style = TextStyles.Body.copy(fontSize = 14.sp, color = Palette.Muted))
                }
            }
            entries.forEach { entry ->
                LogRow(book = entry.book, log = entry.log, onClick = { onOpen(entry.book.id) })
            }
        }
    }
}
