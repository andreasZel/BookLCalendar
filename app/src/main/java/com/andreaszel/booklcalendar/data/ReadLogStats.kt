package com.andreaszel.booklcalendar.data

import java.time.LocalDate
import java.time.YearMonth

fun pagesPerDay(logs: List<ReadLog>, month: YearMonth): Map<LocalDate, Int> = logs.filter { YearMonth.from(it.date) == month }.groupBy { it.date }.mapValues { (_, day) -> day.sumOf { it.pagesRead } }.toSortedMap()

fun pagesReadFor(logs: List<ReadLog>, bookId: Long): Int = logs.filter { it.bookId == bookId }.sumOf { it.pagesRead }

fun progressOf(book: Book, logs: List<ReadLog>): Float? {
    val total = book.totalPages ?: return null
    if (total <= 0) return null
    return (pagesReadFor(logs, book.id).toFloat() / total).coerceIn(0f, 1f)
}
