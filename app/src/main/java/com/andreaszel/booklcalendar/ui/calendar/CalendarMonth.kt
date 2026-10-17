package com.andreaszel.booklcalendar.ui.calendar

import java.time.LocalDate
import java.time.YearMonth

// Monday-first weeks, blanks pad the first and last week
fun monthCells(month: YearMonth): List<LocalDate?> {
    val lead: List<LocalDate?> = List(month.atDay(1).dayOfWeek.value - 1) { null }
    val cells = lead + (1..month.lengthOfMonth()).map { month.atDay(it) }
    return cells + List((7 - cells.size % 7) % 7) { null }
}

fun heatLevel(pages: Int?): Int = when {
    pages == null -> 0
    pages < 20 -> 1
    pages < 50 -> 2
    else -> 3
}

fun pagesLabel(pages: Int) = if (pages == 1) "1 page" else "$pages pages"

fun daysLabel(days: Int) = if (days == 1) "1 day" else "$days days"
