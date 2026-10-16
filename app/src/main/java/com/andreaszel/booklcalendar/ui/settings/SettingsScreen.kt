package com.andreaszel.booklcalendar.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.andreaszel.booklcalendar.ui.appViewModel
import com.andreaszel.booklcalendar.ui.components.ComicButton
import com.andreaszel.booklcalendar.ui.components.ComicIconButton
import com.andreaszel.booklcalendar.ui.components.ComicTextField
import com.andreaszel.booklcalendar.ui.components.InfoPill
import com.andreaszel.booklcalendar.ui.icons.ComicIcons
import com.andreaszel.booklcalendar.ui.theme.Dimens
import com.andreaszel.booklcalendar.ui.theme.Palette
import com.andreaszel.booklcalendar.ui.theme.TextStyles
import com.andreaszel.booklcalendar.ui.theme.comic

@Composable
fun SettingsScreen(onBack: () -> Unit) {
    val vm = appViewModel { SettingsViewModel(it.keys) }
    val state by vm.state.collectAsStateWithLifecycle()
    val status = statusCopy.getValue(state.status)

    Column(modifier = Modifier.fillMaxSize().safeDrawingPadding()) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth().padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp)) {
            ComicIconButton(icon = ComicIcons.Back, label = "Back to shelf", onClick = onBack)
            Text("Settings", style = TextStyles.Heading.copy(fontSize = 24.sp), modifier = Modifier.semantics { heading() })
        }

        Column(verticalArrangement = Arrangement.spacedBy(14.dp), modifier = Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(16.dp)) {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth().comic(Palette.White, radius = Dimens.RadiusBig).padding(16.dp)) {
                Text("Book lookup", style = TextStyles.Label.copy(fontSize = 20.sp))
                Text("Titles, authors, descriptions and covers come from Google Books, Open Library and Wikipedia. They're free and work without any setup.", style = TextStyles.Body.copy(fontSize = 14.sp, color = Palette.MutedDeep))

                Row(verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.padding(top = 6.dp)) {
                    Text("Google Books API key", style = TextStyles.Label.copy(fontSize = 16.sp))
                    Text("optional", style = TextStyles.Strong.copy(fontSize = 12.sp, color = Palette.Muted), modifier = Modifier.padding(bottom = 2.dp))
                }
                Text("Without a key, Google Books shares a small free quota with everyone and lookups may fall back to Open Library. A free key from Google Cloud gives you your own quota. It stays on this phone and is left out of backups.", style = TextStyles.Body.copy(fontSize = 13.sp, color = Palette.Muted))
                ComicTextField(value = state.draft, onChange = vm::onDraft, placeholder = "Paste your key", fontSize = 15.sp, capitalization = KeyboardCapitalization.None)

                Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth().padding(end = 3.dp, bottom = 3.dp)) {
                    ComicButton(text = if (state.busy) "Testing…" else "Save & test", onClick = vm::save, enabled = !state.busy, icon = ComicIcons.Check, fill = Palette.Coral, modifier = Modifier.weight(1f))
                    if (state.saved) {
                        ComicButton(text = "Remove", onClick = vm::remove, enabled = !state.busy, icon = ComicIcons.Trash, fill = Palette.White)
                    }
                }

                InfoPill(text = status.text, icon = status.icon, fill = status.fill, fontSize = 13.sp, iconSize = 16.dp, vertical = 6.dp, horizontal = 12.dp, modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite })
            }
        }
    }
}

private val statusCopy = mapOf(KeyStatus.None to Status("No key, using the shared quota", ComicIcons.Book, Palette.Paper), KeyStatus.Saved to Status("Key saved on this phone", ComicIcons.Key, Palette.Sky), KeyStatus.Working to Status("Key saved and working", ComicIcons.Check, Palette.Sky), KeyStatus.Untested to Status("Saved, couldn't test it offline", ComicIcons.Key, Palette.Yellow), KeyStatus.Rejected to Status("Google rejected this key, not saved", ComicIcons.Close, Palette.Coral))

private data class Status(val text: String, val icon: ImageVector, val fill: Color)
