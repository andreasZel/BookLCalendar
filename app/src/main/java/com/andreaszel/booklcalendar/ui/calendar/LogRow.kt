package com.andreaszel.booklcalendar.ui.calendar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.andreaszel.booklcalendar.data.Book
import com.andreaszel.booklcalendar.data.ReadLog
import com.andreaszel.booklcalendar.ui.components.BookCover
import com.andreaszel.booklcalendar.ui.components.InfoPill
import com.andreaszel.booklcalendar.ui.components.pressable
import com.andreaszel.booklcalendar.ui.icons.ComicIcons
import com.andreaszel.booklcalendar.ui.theme.Palette
import com.andreaszel.booklcalendar.ui.theme.TextStyles
import com.andreaszel.booklcalendar.ui.theme.comic

@Composable
fun LogRow(book: Book, log: ReadLog, onClick: () -> Unit) {
    Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.Top, modifier = Modifier.fillMaxWidth().padding(end = 3.dp, bottom = 3.dp).pressable(fill = Palette.White, shadow = 3.dp, role = Role.Button, onClick = onClick).padding(10.dp)) {
        Box(modifier = Modifier.width(48.dp).aspectRatio(3f / 4f).comic(Palette.Paper, shadow = 0.dp)) {
            BookCover(title = book.title, coverPath = book.coverPath, initialSize = 20.sp)
        }
        Column(verticalArrangement = Arrangement.spacedBy(4.dp), modifier = Modifier.weight(1f)) {
            Text(book.title, maxLines = 2, overflow = TextOverflow.Ellipsis, style = TextStyles.Label.copy(fontSize = 16.sp))
            if (book.author.isNotBlank()) {
                Text(book.author, maxLines = 1, overflow = TextOverflow.Ellipsis, style = TextStyles.Strong.copy(fontSize = 13.sp, color = Palette.Muted))
            }
            InfoPill(text = pagesLabel(log.pagesRead), icon = ComicIcons.Book, fill = Palette.Cream, fontSize = 12.sp, iconSize = 14.dp, vertical = 3.dp, horizontal = 8.dp)
            if (log.note.isNotBlank()) {
                Text(log.note, style = TextStyles.Body.copy(fontSize = 14.sp))
            }
        }
    }
}
