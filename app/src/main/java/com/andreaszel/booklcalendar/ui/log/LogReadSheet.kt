package com.andreaszel.booklcalendar.ui.log

import android.content.ActivityNotFoundException
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.andreaszel.booklcalendar.data.ReadLog
import com.andreaszel.booklcalendar.ui.DateLabels
import com.andreaszel.booklcalendar.ui.appViewModel
import com.andreaszel.booklcalendar.ui.components.ComicButton
import com.andreaszel.booklcalendar.ui.components.ComicIcon
import com.andreaszel.booklcalendar.ui.components.ComicIconButton
import com.andreaszel.booklcalendar.ui.components.ComicTextField
import com.andreaszel.booklcalendar.ui.icons.ComicIcons
import com.andreaszel.booklcalendar.ui.theme.Dimens
import com.andreaszel.booklcalendar.ui.theme.Palette
import com.andreaszel.booklcalendar.ui.theme.TextStyles
import com.andreaszel.booklcalendar.ui.theme.dashedComic
import java.time.LocalDate
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogReadSheet(bookId: Long, date: LocalDate, onDismiss: () -> Unit) {
    val session = rememberSaveable { UUID.randomUUID().toString() }
    val vm = appViewModel(key = "log-$session") { LogReadViewModel(bookId, date, it.books, it.logs, it.languages) }
    val state by vm.state.collectAsStateWithLifecycle()
    val sheet = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val today = remember { LocalDate.now() }
    var confirming by rememberSaveable { mutableStateOf(false) }
    val context = LocalContext.current
    var canSpeak by remember { mutableStateOf(SpeechInput.available(context)) }
    val speech = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result -> SpeechInput.text(result)?.let(vm::onSpoken) }
    val book = state.book
    val editing = state.existing != null

    LaunchedEffect(state.done) { if (state.done) onDismiss() }

    ModalBottomSheet(onDismissRequest = onDismiss, sheetState = sheet, containerColor = Palette.Paper, contentColor = Palette.Ink, shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState()).padding(start = 16.dp, end = 16.dp, bottom = 24.dp)) {
            Column {
                Text(if (editing) "Edit reading log" else "Log reading", style = TextStyles.Heading.copy(fontSize = 22.sp), modifier = Modifier.semantics { heading() })
                Text(if (date == today || date == today.minusDays(1)) "${DateLabels.day(date, today)} · ${DateLabels.full(date)}" else DateLabels.full(date), style = TextStyles.Strong.copy(fontSize = 13.sp, color = Palette.Muted))
            }

            if (book == null && state.loaded && state.shelfEmpty) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth().dashedComic(Palette.White).padding(horizontal = 20.dp, vertical = 22.dp)) {
                    ComicIcon(ComicIcons.Book, size = 36.dp)
                    Text("Your shelf is empty", textAlign = TextAlign.Center, style = TextStyles.Label.copy(fontSize = 17.sp))
                    Text("Add a book first, then log the pages you read.", textAlign = TextAlign.Center, style = TextStyles.Body.copy(fontSize = 14.sp, color = Palette.Muted))
                }
            }

            if (book == null && !state.shelfEmpty) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Which book?", style = TextStyles.Label.copy(fontSize = 16.sp))
                    ComicTextField(value = state.query, onChange = vm::onQuery, placeholder = "Search your shelf", fontSize = 16.sp, label = "Search your shelf")
                    if (state.loaded && state.matches.isEmpty()) {
                        Text("No books match “${state.query.trim()}”.", style = TextStyles.Body.copy(fontSize = 14.sp, color = Palette.Muted), modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite })
                    }
                    state.matches.forEach { match ->
                        BookPickRow(book = match, picked = false, onClick = { confirming = false; vm.pick(match.id) })
                    }
                }
            }

            if (book != null) {
                BookPickRow(book = book, picked = true, onClick = { confirming = false; vm.changeBook() })

                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("Pages read", style = TextStyles.Label.copy(fontSize = 16.sp))
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(end = 3.dp, bottom = 3.dp)) {
                        ComicIconButton(icon = ComicIcons.Minus, label = "One page less", onClick = { vm.step(-1) })
                        ComicTextField(value = state.pages, onChange = vm::onPages, placeholder = "0", fontSize = 20.sp, keyboardType = KeyboardType.Number, label = "Pages read", modifier = Modifier.weight(1f))
                        ComicIconButton(icon = ComicIcons.Plus, label = "One page more", fill = Palette.Yellow, onClick = { vm.step(1) })
                    }
                }

                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Row(verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text("Note", style = TextStyles.Label.copy(fontSize = 16.sp))
                        Text("optional", style = TextStyles.Strong.copy(fontSize = 12.sp, color = Palette.Muted), modifier = Modifier.padding(bottom = 2.dp))
                        Spacer(modifier = Modifier.weight(1f))
                        Text("${state.note.length} / ${ReadLog.NOTE_LIMIT}", style = TextStyles.Strong.copy(fontSize = 12.sp, color = if (state.note.length >= ReadLog.NOTE_LIMIT) Palette.Error else Palette.Muted), modifier = Modifier.padding(bottom = 2.dp))
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.Top, modifier = Modifier.padding(end = 3.dp, bottom = 3.dp)) {
                        ComicTextField(value = state.note, onChange = vm::onNote, placeholder = "A line you liked, how it felt…", fontSize = 15.sp, singleLine = false, minLines = 3, label = "Note", modifier = Modifier.weight(1f))
                        if (canSpeak) {
                            ComicIconButton(icon = ComicIcons.Mic, label = "Speak a note", fill = Palette.Sky, onClick = { try { speech.launch(SpeechInput.intent(vm.speechTag)) } catch (e: ActivityNotFoundException) { canSpeak = false } })
                        }
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth().padding(end = 4.dp, bottom = 4.dp)) {
                    if (editing) {
                        ComicButton(text = if (confirming) "Sure? Delete" else "Delete", onClick = { if (confirming) vm.delete() else confirming = true }, enabled = !state.busy, icon = ComicIcons.Trash, fill = if (confirming) Palette.Coral else Palette.White, height = 56.dp, modifier = Modifier.weight(1f))
                    }
                    ComicButton(text = if (editing) "Save" else "Log pages", onClick = vm::save, enabled = !state.busy, icon = ComicIcons.Check, fill = if (editing) Palette.Yellow else Palette.Coral, height = 56.dp, radius = Dimens.RadiusBig, shadow = 4.dp, fontSize = 18.sp, modifier = Modifier.weight(1f))
                }
            }
        }
    }
}
