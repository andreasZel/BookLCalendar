package com.andreaszel.booklcalendar.ui.add

import com.andreaszel.booklcalendar.ui.DateLabels
import java.time.LocalDate

fun lookupCopy(hasPhoto: Boolean, hasQuery: Boolean, found: Boolean, busy: Boolean): LookupCopy {
    val enabled = (hasPhoto || hasQuery) && !busy
    val label = when {
        busy -> "Looking up…"
        hasQuery -> "Find by title"
        hasPhoto -> "Read the cover"
        else -> "Find online"
    }
    val hint = when {
        found -> "Filled in from online book info. Not it? Pick another match, or Undo."
        hasQuery -> "Looks the title up in free book databases and fills in the rest."
        hasPhoto -> "Reads the text on your cover photo and looks the book up."
        else -> "Scan the barcode, add a cover photo or type a title, and I can fill in the rest."
    }
    return LookupCopy(label, hint, enabled)
}

fun shelfHint(date: LocalDate, today: LocalDate): String {
    if (date == today) return "Today"
    if (date == today.minusDays(1)) return "Yesterday"
    val monday = DateLabels.weekStart(date)
    val sunday = monday.plusDays(6)
    return "the week of ${monday.dayOfMonth} ${DateLabels.month(monday)} – ${sunday.dayOfMonth} ${DateLabels.month(sunday)} ${sunday.year}"
}

data class LookupCopy(val label: String, val hint: String, val enabled: Boolean)
