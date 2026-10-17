package com.andreaszel.booklcalendar.ui.detail

import com.andreaszel.booklcalendar.ui.calendar.daysLabel
import com.andreaszel.booklcalendar.ui.calendar.pagesLabel

fun readingSummary(pagesRead: Int, days: Int, totalPages: Int?): String {
    if (totalPages == null) return if (days == 0) "No pages logged yet. Add the total pages in Edit to see your progress." else "${pagesLabel(pagesRead)} on ${daysLabel(days)}"
    return when {
        days == 0 -> "No pages logged yet, ${pagesLabel(totalPages)} to go."
        pagesRead >= totalPages -> "Finished! $pagesRead of $totalPages pages on ${daysLabel(days)}"
        else -> "$pagesRead of $totalPages pages on ${daysLabel(days)}"
    }
}
