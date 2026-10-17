package com.andreaszel.booklcalendar.ui.add

import android.content.ActivityNotFoundException
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.andreaszel.booklcalendar.data.CoverSource
import com.andreaszel.booklcalendar.lookup.IsbnScanner
import com.andreaszel.booklcalendar.ui.DateLabels
import com.andreaszel.booklcalendar.ui.appViewModel
import com.andreaszel.booklcalendar.ui.components.BookCover
import com.andreaszel.booklcalendar.ui.components.ComicButton
import com.andreaszel.booklcalendar.ui.components.ComicChip
import com.andreaszel.booklcalendar.ui.components.ComicIcon
import com.andreaszel.booklcalendar.ui.components.ComicIconButton
import com.andreaszel.booklcalendar.ui.components.ComicTextField
import com.andreaszel.booklcalendar.ui.components.InfoPill
import com.andreaszel.booklcalendar.ui.icons.ComicIcons
import com.andreaszel.booklcalendar.ui.theme.Dimens
import com.andreaszel.booklcalendar.ui.theme.Palette
import com.andreaszel.booklcalendar.ui.theme.TextStyles
import com.andreaszel.booklcalendar.ui.theme.comic
import com.andreaszel.booklcalendar.ui.theme.dashedComic
import com.andreaszel.booklcalendar.ui.theme.topRule
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBookScreen(bookId: Long, onDone: () -> Unit) {
    val vm = appViewModel(key = "edit-$bookId") { AddBookViewModel(bookId, it.books, it.covers, it.lookup, it.coverReader) }
    val state by vm.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val today = remember { LocalDate.now() }
    var cameraUri by rememberSaveable { mutableStateOf<String?>(null) }
    var picking by rememberSaveable { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val close = { vm.discard(); onDone() }
    val scan: () -> Unit = {
        scope.launch {
            val raw = try {
                IsbnScanner.scan(context)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Toast.makeText(context, "The scanner is getting ready. Try again in a moment.", Toast.LENGTH_SHORT).show()
                null
            }
            if (raw != null) vm.lookupIsbn(raw)
        }
    }

    val camera = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { ok ->
        val uri = cameraUri
        if (ok && uri != null) vm.setCover(Uri.parse(uri), CoverSource.Camera)
    }
    val gallery = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) vm.setCover(uri, CoverSource.Gallery)
    }

    LaunchedEffect(state.saved) { if (state.saved) onDone() }
    BackHandler { close() }

    val hasTitle = state.title.isNotBlank()
    val copy = lookupCopy(hasPhoto = state.coverPath != null, hasQuery = hasTitle || state.author.isNotBlank(), found = state.undo != null, busy = state.busy)
    val showError = state.attempted && !hasTitle

    Column(modifier = Modifier.fillMaxSize().safeDrawingPadding()) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth().height(76.dp).padding(horizontal = 16.dp)) {
            ComicIconButton(icon = ComicIcons.Close, label = "Close", onClick = close)
            Text(if (state.editing) "Edit book" else "New book", style = TextStyles.Heading.copy(fontSize = 24.sp), modifier = Modifier.semantics { heading() })
        }

        Column(verticalArrangement = Arrangement.spacedBy(18.dp), modifier = Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 24.dp)) {
            if (state.coverPath == null) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(14.dp), modifier = Modifier.fillMaxWidth().dashedComic(Palette.White).padding(start = 16.dp, end = 16.dp, top = 22.dp, bottom = 18.dp)) {
                    ComicIcon(ComicIcons.AddCover, size = 92.dp)
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Add a cover photo", style = TextStyles.Label.copy(fontSize = 19.sp))
                        Text("Optional — books without one get a doodle cover", style = TextStyles.Body.copy(fontSize = 13.sp, color = Palette.Muted))
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth().padding(end = 3.dp, bottom = 3.dp)) {
                        ComicButton(text = "Camera", icon = ComicIcons.Camera, fill = Palette.Sky, modifier = Modifier.weight(1f), onClick = {
                            val uri = vm.newCameraUri()
                            cameraUri = uri.toString()
                            try {
                                camera.launch(uri)
                            } catch (e: ActivityNotFoundException) {
                                Toast.makeText(context, "No camera app found", Toast.LENGTH_SHORT).show()
                            }
                        })
                        ComicButton(text = "Gallery", icon = ComicIcons.Gallery, fill = Palette.Yellow, modifier = Modifier.weight(1f), onClick = { gallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) })
                    }
                }
            } else {
                Row(horizontalArrangement = Arrangement.spacedBy(14.dp), verticalAlignment = Alignment.Bottom, modifier = Modifier.padding(top = 6.dp)) {
                    Box(modifier = Modifier.width(150.dp).aspectRatio(3f / 4f).rotate(-2f).comic(Palette.Paper, shadow = 4.dp)) {
                        BookCover(title = state.title, coverPath = state.coverPath, initialSize = 40.sp)
                    }
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.padding(bottom = 4.dp)) {
                        InfoPill(text = when (state.coverSource) { CoverSource.Camera -> "Taken with camera"; CoverSource.Online -> "Found online"; else -> "Picked from gallery" }, icon = ComicIcons.Check, fill = Palette.White, fontSize = 12.sp, iconSize = 14.dp, vertical = 4.dp, horizontal = 10.dp)
                        ComicButton(text = "Change photo", onClick = vm::clearCover, fill = Palette.White, height = Dimens.Touch, shadow = 2.dp, fontSize = 15.sp)
                    }
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth().comic(Palette.Cream, radius = Dimens.RadiusBig, shadow = 0.dp).padding(14.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth().padding(end = 3.dp, bottom = 3.dp)) {
                    ComicButton(text = copy.label, onClick = vm::findOnline, enabled = copy.enabled, icon = ComicIcons.Sparkle, fill = if (copy.enabled || state.busy) Palette.Lilac else Palette.DisabledLight, height = 46.dp, radius = 23.dp, fontSize = 15.sp, padding = PaddingValues(horizontal = 10.dp), gap = 6.dp, modifier = Modifier.weight(1f).alpha(if (copy.enabled || state.busy) 1f else 0.6f))
                    ComicButton(text = "Scan ISBN", onClick = scan, enabled = !state.busy, icon = ComicIcons.Barcode, fill = Palette.Sky, height = 46.dp, radius = 23.dp, fontSize = 15.sp, padding = PaddingValues(horizontal = 10.dp), gap = 6.dp)
                }
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text(copy.hint, style = TextStyles.Body.copy(fontSize = 13.sp, color = Palette.MutedDeep), modifier = Modifier.weight(1f))
                    if (state.undo != null) {
                        Text("Undo", style = TextStyles.Strong.copy(fontSize = 14.sp, textDecoration = TextDecoration.Underline), modifier = Modifier.heightIn(min = Dimens.Touch).clip(RoundedCornerShape(Dimens.Radius)).clickable(role = Role.Button, onClick = vm::undo).padding(horizontal = 10.dp, vertical = 12.dp))
                    }
                }
                if (state.notice != null) {
                    Text(state.notice.orEmpty(), style = TextStyles.Strong.copy(fontSize = 13.sp), modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite })
                }
                if (state.matches.size > 1) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(end = 2.dp, bottom = 2.dp)) {
                        Text("Matches, tap to switch", style = TextStyles.Label.copy(fontSize = 14.sp))
                        state.matches.forEachIndexed { index, match ->
                            MatchRow(match = match, picked = index == state.picked, onClick = { vm.pick(index) })
                        }
                    }
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Row(verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("Title", style = TextStyles.Label.copy(fontSize = 16.sp))
                    Text("*", style = TextStyles.Label.copy(fontSize = 16.sp, color = Palette.Error))
                    Text("required", style = TextStyles.Strong.copy(fontSize = 12.sp, color = Palette.Muted), modifier = Modifier.padding(start = 2.dp, bottom = 2.dp))
                }
                ComicTextField(value = state.title, onChange = vm::onTitle, placeholder = "e.g. The Little Prince", fontSize = 17.sp, isError = showError)
                if (showError) {
                    Text("Every book needs a title before it goes on the shelf.", style = TextStyles.Strong.copy(fontSize = 13.sp, color = Palette.Error), modifier = Modifier.semantics { liveRegion = LiveRegionMode.Assertive })
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Row(verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("Author", style = TextStyles.Label.copy(fontSize = 16.sp))
                    Text("optional", style = TextStyles.Strong.copy(fontSize = 12.sp, color = Palette.Muted), modifier = Modifier.padding(bottom = 2.dp))
                }
                ComicTextField(value = state.author, onChange = vm::onAuthor, placeholder = "e.g. Antoine de Saint-Exupéry", fontSize = 16.sp, capitalization = KeyboardCapitalization.Words)
            }

            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Row(verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("Total pages", style = TextStyles.Label.copy(fontSize = 16.sp))
                    Text("optional, shows your progress", style = TextStyles.Strong.copy(fontSize = 12.sp, color = Palette.Muted), modifier = Modifier.padding(bottom = 2.dp))
                }
                ComicTextField(value = state.totalPages, onChange = vm::onTotalPages, placeholder = "e.g. 96", fontSize = 16.sp, keyboardType = KeyboardType.Number, label = "Total pages", modifier = Modifier.width(160.dp))
            }

            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Row(verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("Description", style = TextStyles.Label.copy(fontSize = 16.sp))
                    Text("optional", style = TextStyles.Strong.copy(fontSize = 12.sp, color = Palette.Muted), modifier = Modifier.padding(bottom = 2.dp))
                }
                ComicTextField(value = state.description, onChange = vm::onDescription, placeholder = "What was it about? How did it feel?", fontSize = 15.sp, singleLine = false, minLines = 4)
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("When did you read it?", style = TextStyles.Label.copy(fontSize = 16.sp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    ComicChip(label = "Today", selected = state.date == today, onClick = { vm.onDate(today) })
                    ComicChip(label = "Yesterday", selected = state.date == today.minusDays(1), onClick = { vm.onDate(today.minusDays(1)) })
                }
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth().heightIn(min = 52.dp).comic(Palette.White).clickable(role = Role.Button, onClickLabel = "Pick a date") { picking = true }.padding(horizontal = 14.dp)) {
                    ComicIcon(ComicIcons.Calendar, size = 22.dp)
                    Text(DateLabels.full(state.date), style = TextStyles.Strong.copy(fontSize = 16.sp))
                }
                Text("It will show up under ${shelfHint(state.date, today)} on your shelf.", style = TextStyles.Body.copy(fontSize = 13.sp, color = Palette.Muted))
            }
        }

        Box(modifier = Modifier.fillMaxWidth().topRule().padding(16.dp)) {
            ComicButton(text = if (state.editing) "Save changes" else "Add to shelf", onClick = vm::save, icon = if (hasTitle) ComicIcons.Check else null, fill = if (hasTitle) Palette.Coral else Palette.Disabled, height = 60.dp, radius = Dimens.RadiusBig, shadow = if (hasTitle) 4.dp else 0.dp, fontSize = 19.sp, padding = PaddingValues(0.dp), modifier = Modifier.fillMaxWidth().padding(end = 4.dp, bottom = 4.dp))
        }
    }

    if (picking) {
        val pickerState = rememberDatePickerState(initialSelectedDateMillis = state.date.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli(), selectableDates = PastDates(today))
        DatePickerDialog(onDismissRequest = { picking = false }, confirmButton = {
            TextButton(onClick = {
                pickerState.selectedDateMillis?.let { vm.onDate(Instant.ofEpochMilli(it).atZone(ZoneOffset.UTC).toLocalDate()) }
                picking = false
            }) { Text("Done", style = TextStyles.Label) }
        }, dismissButton = {
            TextButton(onClick = { picking = false }) { Text("Cancel", style = TextStyles.Label) }
        }) {
            DatePicker(state = pickerState)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
private class PastDates(private val today: LocalDate) : SelectableDates {
    override fun isSelectableDate(utcTimeMillis: Long) = !Instant.ofEpochMilli(utcTimeMillis).atZone(ZoneOffset.UTC).toLocalDate().isAfter(today)

    override fun isSelectableYear(year: Int) = year <= today.year
}
