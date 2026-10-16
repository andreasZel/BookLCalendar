package com.andreaszel.booklcalendar.ui.shelf

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.andreaszel.booklcalendar.ui.appViewModel
import com.andreaszel.booklcalendar.ui.components.ComicButton
import com.andreaszel.booklcalendar.ui.components.ComicIcon
import com.andreaszel.booklcalendar.ui.icons.ComicIcons
import com.andreaszel.booklcalendar.ui.theme.Dimens
import com.andreaszel.booklcalendar.ui.theme.Palette
import com.andreaszel.booklcalendar.ui.theme.TextStyles
import com.andreaszel.booklcalendar.ui.theme.comic
import com.andreaszel.booklcalendar.ui.theme.dashedComic
import java.time.LocalDate

@Composable
fun ShelfScreen(onAdd: () -> Unit, onOpen: (Long) -> Unit) {
    val vm = appViewModel { ShelfViewModel(it.books) }
    val state by vm.state.collectAsStateWithLifecycle()
    val today = remember { LocalDate.now() }

    Box(modifier = Modifier.fillMaxSize().safeDrawingPadding()) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 18.dp, bottom = 10.dp)) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.size(Dimens.Touch).comic(Palette.Yellow, shadow = 2.dp)) {
                    ComicIcon(ComicIcons.Book, size = 24.dp)
                }
                Column {
                    Text("My Bookshelf", style = TextStyles.Heading.copy(fontSize = 26.sp, lineHeight = 29.sp), modifier = Modifier.semantics { heading() })
                    Text(if (state.books.size == 1) "1 book logged" else "${state.books.size} books logged", style = TextStyles.Strong.copy(fontSize = 13.sp, color = Palette.Muted))
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 8.dp)) {
                ZoomControl(level = state.level, onPick = vm::setLevel)
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
                    ComicIcon(ComicIcons.Pinch, size = 20.dp, tint = Palette.Muted)
                    Text("Pinch to zoom", style = TextStyles.Strong.copy(fontSize = 12.sp, color = Palette.Muted))
                }
            }

            if (state.loaded && state.books.isEmpty()) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.padding(16.dp).fillMaxWidth().dashedComic(Palette.White).padding(horizontal = 20.dp, vertical = 28.dp)) {
                    ComicIcon(ComicIcons.AddCover, size = 92.dp)
                    Text("Your shelf is empty", style = TextStyles.Label.copy(fontSize = 20.sp))
                    Text("Log a book you've read and it lands here, grouped by when you read it.", textAlign = TextAlign.Center, style = TextStyles.Body.copy(fontSize = 14.sp, color = Palette.Muted))
                }
            }

            AnimatedContent(targetState = state.level, transitionSpec = { fadeIn() togetherWith fadeOut() }, label = "zoom", modifier = Modifier.weight(1f).pinchSteps(onZoomIn = vm::zoomIn, onZoomOut = vm::zoomOut)) { level ->
                val look = looks.getValue(level)
                val groups = remember(state.books, level) { groupBooks(state.books, level, today) }
                LazyVerticalGrid(columns = GridCells.Fixed(level.columns), horizontalArrangement = Arrangement.spacedBy(look.gap.dp), verticalArrangement = Arrangement.spacedBy(look.gap.dp), contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 120.dp), modifier = Modifier.fillMaxSize()) {
                    groups.forEach { group ->
                        item(key = "group-${group.key}", span = { GridItemSpan(maxLineSpan) }) {
                            GroupHeader(label = group.label, count = group.books.size, fontSize = look.headSize.sp)
                        }
                        items(group.books, key = { it.id }) { book ->
                            BookCard(book = book, showTitle = level != ZoomLevel.Years, titleSize = look.titleSize.sp, initialSize = look.initialSize.sp, shadow = look.shadow.dp, onClick = { onOpen(book.id) })
                        }
                    }
                }
            }
        }

        ComicButton(text = "Add book", onClick = onAdd, fill = Palette.Coral, icon = ComicIcons.Plus, height = 60.dp, radius = Dimens.RadiusFab, shadow = 4.dp, fontSize = 18.sp, padding = PaddingValues(start = 16.dp, end = 22.dp), modifier = Modifier.align(Alignment.BottomEnd).padding(end = 16.dp, bottom = 24.dp).height(60.dp))
    }
}

private val looks = mapOf(ZoomLevel.Days to Look(gap = 14, titleSize = 15, initialSize = 64, headSize = 20, shadow = 3), ZoomLevel.Weeks to Look(gap = 10, titleSize = 12, initialSize = 40, headSize = 17, shadow = 3), ZoomLevel.Years to Look(gap = 7, titleSize = 0, initialSize = 24, headSize = 22, shadow = 2))

private data class Look(val gap: Int, val titleSize: Int, val initialSize: Int, val headSize: Int, val shadow: Int)
