package com.andreaszel.booklcalendar.ui.add

import com.andreaszel.booklcalendar.ui.DateLabels
import java.time.LocalDate

fun suggestCopy(hasPhoto: Boolean, hasTitle: Boolean, suggested: Boolean, busy: Boolean): SuggestCopy {
    val enabled = (hasPhoto || hasTitle) && !busy
    val label = when {
        busy -> "Thinking…"
        suggested -> "Suggest another"
        hasPhoto && !hasTitle -> "Suggest title & description"
        hasTitle -> "Suggest description"
        else -> "Suggest"
    }
    val hint = when {
        suggested -> "Filled in by suggestion — tap again for another, or Undo."
        hasPhoto && !hasTitle -> "Reads your cover photo and fills both fields. You can edit anything."
        hasTitle -> "Writes a short description from the title. You can edit it."
        else -> "Add a photo or type a title, and I can fill in the rest."
    }
    return SuggestCopy(label, hint, enabled)
}

fun shelfHint(date: LocalDate, today: LocalDate): String {
    if (date == today) return "Today"
    if (date == today.minusDays(1)) return "Yesterday"
    val monday = DateLabels.weekStart(date)
    val sunday = monday.plusDays(6)
    return "the week of ${monday.dayOfMonth} ${DateLabels.month(monday)} – ${sunday.dayOfMonth} ${DateLabels.month(sunday)} ${sunday.year}"
}

data class SuggestCopy(val label: String, val hint: String, val enabled: Boolean)
