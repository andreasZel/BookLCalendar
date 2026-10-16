package com.andreaszel.booklcalendar.ui.shelf

import com.andreaszel.booklcalendar.data.Book
import com.andreaszel.booklcalendar.ui.DateLabels
import java.time.LocalDate

fun groupBooks(books: List<Book>, level: ZoomLevel, today: LocalDate): List<ShelfGroup> {
    val sorted = books.sortedWith(compareByDescending<Book> { it.readDate }.thenByDescending { it.createdAt })
    val groups = mutableListOf<ShelfGroup>()
    for (book in sorted) {
        val key = when (level) {
            ZoomLevel.Days -> book.readDate.toString()
            ZoomLevel.Weeks -> DateLabels.weekStart(book.readDate).toString()
            ZoomLevel.Years -> book.readDate.year.toString()
        }
        val last = groups.lastOrNull()
        if (last != null && last.key == key) {
            groups[groups.lastIndex] = last.copy(books = last.books + book)
            continue
        }
        val label = when (level) {
            ZoomLevel.Days -> DateLabels.day(book.readDate, today)
            ZoomLevel.Weeks -> DateLabels.week(DateLabels.weekStart(book.readDate), today)
            ZoomLevel.Years -> key
        }
        groups += ShelfGroup(key, label, listOf(book))
    }
    return groups
}

fun countLabel(count: Int) = if (count == 1) "1 book" else "$count books"

enum class ZoomLevel(val columns: Int) {
    Days(2),
    Weeks(3),
    Years(5);

    fun zoomIn() = entries[(ordinal - 1).coerceAtLeast(0)]
    fun zoomOut() = entries[(ordinal + 1).coerceAtMost(entries.lastIndex)]
}

data class ShelfGroup(val key: String, val label: String, val books: List<Book>)
