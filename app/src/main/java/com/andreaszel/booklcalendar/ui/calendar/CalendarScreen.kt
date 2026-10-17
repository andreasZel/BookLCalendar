package com.andreaszel.booklcalendar.ui.calendar

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.andreaszel.booklcalendar.ui.DateLabels
import com.andreaszel.booklcalendar.ui.appViewModel
import com.andreaszel.booklcalendar.ui.components.ComicIcon
import com.andreaszel.booklcalendar.ui.components.ComicIconButton
import com.andreaszel.booklcalendar.ui.icons.ComicIcons
import com.andreaszel.booklcalendar.ui.theme.Dimens
import com.andreaszel.booklcalendar.ui.theme.Palette
import com.andreaszel.booklcalendar.ui.theme.TextStyles
import com.andreaszel.booklcalendar.ui.theme.comic
import com.andreaszel.booklcalendar.ui.theme.dashedComic
import java.time.LocalDate

@Composable
fun CalendarScreen(onBack: () -> Unit, onOpen: (Long) -> Unit) {
    val vm = appViewModel { CalendarViewModel(it.books, it.logs) }
    val state by vm.state.collectAsStateWithLifecycle()
    val today = remember { LocalDate.now() }
    val total = state.pages.values.sum()
    val selected = state.selected

    Column(modifier = Modifier.fillMaxSize().safeDrawingPadding()) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth().padding(start = 16.dp, end = 16.dp, top = 18.dp, bottom = 10.dp)) {
            ComicIconButton(icon = ComicIcons.Back, label = "Back to shelf", onClick = onBack)
            Column(modifier = Modifier.weight(1f)) {
                Text("Reading calendar", style = TextStyles.Heading.copy(fontSize = 26.sp, lineHeight = 29.sp), modifier = Modifier.semantics { heading() })
                Text(if (state.pages.isEmpty()) "Nothing logged in ${DateLabels.monthName(state.month)}" else "${pagesLabel(total)} on ${daysLabel(state.pages.size)}", style = TextStyles.Strong.copy(fontSize = 13.sp, color = Palette.Muted))
            }
        }

        Column(verticalArrangement = Arrangement.spacedBy(14.dp), modifier = Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 24.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                ComicIconButton(icon = ComicIcons.Back, label = "Previous month", fill = Palette.Sky, onClick = vm::previous)
                Text(DateLabels.monthYear(state.month), textAlign = TextAlign.Center, style = TextStyles.Label.copy(fontSize = 20.sp), modifier = Modifier.weight(1f).semantics { heading(); liveRegion = LiveRegionMode.Polite })
                ComicIconButton(icon = ComicIcons.Next, label = "Next month", fill = Palette.Sky, onClick = vm::next)
            }

            AnimatedContent(targetState = state.month, transitionSpec = { val forward = targetState > initialState; (slideInHorizontally { if (forward) it / 3 else -it / 3 } + fadeIn()) togetherWith (slideOutHorizontally { if (forward) -it / 3 else it / 3 } + fadeOut()) }, label = "month", modifier = Modifier.fillMaxWidth().padding(end = 3.dp, bottom = 3.dp)) { month ->
                val cells = remember(month) { monthCells(month) }
                Column(modifier = Modifier.fillMaxWidth().comic(Palette.White, radius = Dimens.RadiusBig).monthSwipe(onPrevious = vm::previous, onNext = vm::next).padding(horizontal = 6.dp, vertical = 10.dp)) {
                    Row(modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp)) {
                        DateLabels.weekdays.forEach { name ->
                            Text(name, textAlign = TextAlign.Center, style = TextStyles.Strong.copy(fontSize = 12.sp, color = Palette.Muted), modifier = Modifier.weight(1f).clearAndSetSemantics {})
                        }
                    }
                    cells.chunked(7).forEach { week ->
                        Row(modifier = Modifier.fillMaxWidth()) {
                            week.forEach { date ->
                                if (date == null) {
                                    Spacer(modifier = Modifier.weight(1f).height(54.dp))
                                } else {
                                    DayCell(date = date, pages = state.pages[date], today = date == today, picked = date == selected, future = date.isAfter(today), onClick = { vm.openDay(date) }, modifier = Modifier.weight(1f))
                                }
                            }
                        }
                    }
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clearAndSetSemantics { contentDescription = "Dot colour shows pages read: under 20, 20 to 49, 50 or more" }) {
                Text("Fewer pages", style = TextStyles.Strong.copy(fontSize = 12.sp, color = Palette.Muted))
                HeatDot(level = 1, size = 12.dp)
                HeatDot(level = 2, size = 12.dp)
                HeatDot(level = 3, size = 12.dp)
                Text("More", style = TextStyles.Strong.copy(fontSize = 12.sp, color = Palette.Muted))
            }

            if (state.loaded && state.pages.isEmpty()) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth().dashedComic(Palette.White).padding(horizontal = 20.dp, vertical = 24.dp)) {
                    ComicIcon(ComicIcons.Calendar, size = 40.dp)
                    Text("No reading in ${DateLabels.monthName(state.month)}", textAlign = TextAlign.Center, style = TextStyles.Label.copy(fontSize = 18.sp))
                    Text("Days you log pages get a dot. The more you read, the warmer it gets.", textAlign = TextAlign.Center, style = TextStyles.Body.copy(fontSize = 14.sp, color = Palette.Muted))
                }
            }
        }
    }

    if (selected != null) {
        DaySheet(date = selected, entries = state.day, today = today, onDismiss = vm::closeDay, onOpen = { vm.closeDay(); onOpen(it) })
    }
}
