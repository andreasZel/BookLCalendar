package com.andreaszel.booklcalendar.ui.shelf

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.andreaszel.booklcalendar.data.Book
import com.andreaszel.booklcalendar.ui.components.BookCover
import com.andreaszel.booklcalendar.ui.components.pressable
import com.andreaszel.booklcalendar.ui.theme.Palette
import com.andreaszel.booklcalendar.ui.theme.TextStyles
import com.andreaszel.booklcalendar.ui.theme.topRule

@Composable
fun BookCard(book: Book, showTitle: Boolean, titleSize: TextUnit, initialSize: TextUnit, shadow: Dp, onClick: () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth().pressable(fill = Palette.White, shadow = shadow, role = Role.Button, onClick = onClick).semantics(mergeDescendants = true) { contentDescription = book.title }) {
        Box(modifier = Modifier.fillMaxWidth().aspectRatio(3f / 4f)) {
            BookCover(title = book.title, coverPath = book.coverPath, initialSize = initialSize)
        }
        if (showTitle) {
            Text(book.title, minLines = 2, maxLines = 2, overflow = TextOverflow.Ellipsis, style = TextStyles.Strong.copy(fontSize = titleSize, lineHeight = titleSize * 1.2f), modifier = Modifier.fillMaxWidth().topRule().padding(start = 8.dp, end = 8.dp, top = 9.dp, bottom = 8.dp))
        }
    }
}
