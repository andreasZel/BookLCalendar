package com.andreaszel.booklcalendar.ui.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.andreaszel.booklcalendar.data.CoverSource
import com.andreaszel.booklcalendar.ui.DateLabels
import com.andreaszel.booklcalendar.ui.appViewModel
import com.andreaszel.booklcalendar.ui.components.BookCover
import com.andreaszel.booklcalendar.ui.components.ComicButton
import com.andreaszel.booklcalendar.ui.components.ComicIconButton
import com.andreaszel.booklcalendar.ui.components.ComicProgress
import com.andreaszel.booklcalendar.ui.components.InfoPill
import com.andreaszel.booklcalendar.ui.icons.ComicIcons
import com.andreaszel.booklcalendar.ui.log.LogReadSheet
import com.andreaszel.booklcalendar.ui.theme.Dimens
import com.andreaszel.booklcalendar.ui.theme.Palette
import com.andreaszel.booklcalendar.ui.theme.TextStyles
import com.andreaszel.booklcalendar.ui.theme.bleed
import com.andreaszel.booklcalendar.ui.theme.comic
import java.time.LocalDate
import kotlin.math.roundToInt

@Composable
fun BookDetailScreen(bookId: Long, onBack: () -> Unit, onEdit: (Long) -> Unit) {
    val vm = appViewModel(key = "detail-$bookId") { BookDetailViewModel(bookId, it.books, it.logs) }
    val state by vm.state.collectAsStateWithLifecycle()
    var confirming by rememberSaveable { mutableStateOf(false) }
    var logging by rememberSaveable { mutableStateOf(false) }
    val book = state.book
    val progress = state.progress

    Column(modifier = Modifier.fillMaxSize().statusBarsPadding()) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth().padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp)) {
            ComicIconButton(icon = ComicIcons.Back, label = "Back to shelf", onClick = onBack)
            Spacer(modifier = Modifier.weight(1f))
            if (book != null) {
                ComicIconButton(icon = ComicIcons.Edit, label = "Edit book", fill = Palette.Yellow, onClick = { onEdit(book.id) })
                ComicIconButton(icon = ComicIcons.Trash, label = "Delete book", onClick = { confirming = true })
            }
        }

        if (state.loaded && book == null) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize().padding(24.dp)) {
                Text("This book is no longer on your shelf.", style = TextStyles.Label.copy(fontSize = 18.sp))
            }
        }

        if (book != null) BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val sheetMin = (maxHeight - 324.dp).coerceAtLeast(0.dp)
            Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth().padding(top = 18.dp, bottom = 26.dp)) {
                    Box(modifier = Modifier.width(210.dp).aspectRatio(3f / 4f).rotate(-2f).comic(Palette.Paper, shadow = 6.dp)) {
                        BookCover(title = book.title, coverPath = book.coverPath, initialSize = 96.sp)
                    }
                }

                Column(verticalArrangement = Arrangement.spacedBy(14.dp), modifier = Modifier.fillMaxWidth().heightIn(min = sheetMin).bleed().background(Palette.White, RoundedCornerShape(topStart = 22.dp, topEnd = 22.dp)).border(Dimens.Outline, Palette.Ink, RoundedCornerShape(topStart = 22.dp, topEnd = 22.dp)).navigationBarsPadding().padding(start = 23.dp, end = 23.dp, top = 22.dp, bottom = 32.dp)) {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(book.title, style = TextStyles.Heading.copy(fontSize = 32.sp, lineHeight = 35.sp), modifier = Modifier.semantics { heading() })
                        if (book.author.isNotBlank()) {
                            Text("by ${book.author}", style = TextStyles.Strong.copy(fontSize = 16.sp, color = Palette.MutedDeep))
                        }
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        InfoPill(text = "Read ${DateLabels.full(book.readDate)}", icon = ComicIcons.Calendar, fill = Palette.Sky, fontSize = 13.sp, iconSize = 16.dp, vertical = 6.dp, horizontal = 12.dp)
                        if (book.coverSource != null) {
                            InfoPill(text = when (book.coverSource) { CoverSource.Camera -> "Photo from camera"; CoverSource.Online -> "Cover found online"; else -> "Photo from gallery" }, icon = when (book.coverSource) { CoverSource.Camera -> ComicIcons.Camera; CoverSource.Online -> ComicIcons.Book; else -> ComicIcons.Gallery }, fill = Palette.Paper, fontSize = 13.sp, iconSize = 16.dp, vertical = 6.dp, horizontal = 12.dp)
                        }
                    }
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth().comic(Palette.Paper, shadow = 0.dp).padding(14.dp)) {
                        Row(verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text("Reading", style = TextStyles.Label.copy(fontSize = 15.sp), modifier = Modifier.weight(1f))
                            if (progress != null) {
                                Text("${(progress * 100).roundToInt()}%", style = TextStyles.Label.copy(fontSize = 15.sp))
                            }
                        }
                        if (progress != null) {
                            ComicProgress(progress = progress)
                        }
                        Text(readingSummary(pagesRead = state.pagesRead, days = state.days, totalPages = book.totalPages), style = TextStyles.Body.copy(fontSize = 14.sp, color = if (state.days == 0) Palette.Muted else Palette.Ink))
                        ComicButton(text = "Log reading", onClick = { logging = true }, icon = ComicIcons.Plus, fill = Palette.Yellow, height = 48.dp, fontSize = 16.sp, modifier = Modifier.fillMaxWidth().padding(end = 3.dp, bottom = 3.dp))
                    }
                    Column(modifier = Modifier.fillMaxWidth().comic(Palette.Cream, shadow = 0.dp).padding(14.dp)) {
                        Text("Description", style = TextStyles.Label.copy(fontSize = 15.sp), modifier = Modifier.padding(bottom = 4.dp))
                        Text(book.description.ifBlank { "No description yet. Tap the pencil to add one." }, style = TextStyles.Body.copy(fontSize = 15.sp, lineHeight = 22.sp, color = if (book.description.isBlank()) Palette.Muted else Palette.Ink))
                    }
                }
            }
        }
    }

    if (logging && book != null) {
        LogReadSheet(bookId = book.id, date = LocalDate.now(), onDismiss = { logging = false })
    }

    if (confirming && book != null) {
        Dialog(onDismissRequest = { confirming = false }) {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.padding(end = 4.dp, bottom = 4.dp).comic(Palette.White, radius = Dimens.RadiusBig, shadow = 4.dp).padding(20.dp)) {
                Text("Remove “${book.title}”?", style = TextStyles.Heading.copy(fontSize = 22.sp))
                Text("It will be taken off your shelf, along with its cover photo.", style = TextStyles.Body.copy(fontSize = 15.sp, color = Palette.MutedDeep))
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.padding(end = 3.dp, bottom = 3.dp)) {
                    ComicButton(text = "Keep it", onClick = { confirming = false }, fill = Palette.White, modifier = Modifier.weight(1f))
                    ComicButton(text = "Delete", onClick = { confirming = false; vm.delete(onBack) }, icon = ComicIcons.Trash, fill = Palette.Coral, modifier = Modifier.weight(1f))
                }
            }
        }
    }
}
