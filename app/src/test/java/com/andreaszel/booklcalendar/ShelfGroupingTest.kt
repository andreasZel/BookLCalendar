package com.andreaszel.booklcalendar

import com.andreaszel.booklcalendar.data.Book
import com.andreaszel.booklcalendar.ui.shelf.ZoomLevel
import com.andreaszel.booklcalendar.ui.shelf.countLabel
import com.andreaszel.booklcalendar.ui.shelf.groupBooks
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate

class ShelfGroupingTest {
    private val today = LocalDate.of(2026, 10, 16)

    private val books = listOf(
        book(1, "Piranesi", "2026-10-16"),
        book(2, "The Little Prince", "2026-10-16"),
        book(3, "Project Hail Mary", "2026-10-15"),
        book(4, "Klara and the Sun", "2026-10-13"),
        book(5, "Norwegian Wood", "2026-10-07"),
        book(6, "Dune", "2026-09-29"),
        book(7, "A Man Called Ove", "2025-12-28"),
    )

    @Test
    fun daysGroupsPerDayWithRelativeLabels() {
        val groups = groupBooks(books, ZoomLevel.Days, today)
        assertEquals(listOf("Today", "Yesterday", "Tue, 13 Oct", "Wed, 7 Oct", "Tue, 29 Sep", "Sun, 28 Dec 2025"), groups.map { it.label })
        assertEquals(2, groups.first().books.size)
    }

    @Test
    fun weeksGroupsMondayToSunday() {
        val groups = groupBooks(books, ZoomLevel.Weeks, today)
        assertEquals(listOf("This week", "5 – 11 Oct", "28 Sep – 4 Oct", "22 – 28 Dec 2025"), groups.map { it.label })
        assertEquals(listOf(4, 1, 1, 1), groups.map { it.books.size })
    }

    @Test
    fun yearsGroupsPerYear() {
        val groups = groupBooks(books, ZoomLevel.Years, today)
        assertEquals(listOf("2026", "2025"), groups.map { it.label })
        assertEquals(listOf(6, 1), groups.map { it.books.size })
    }

    @Test
    fun unsortedInputIsOrderedNewestFirst() {
        val groups = groupBooks(books.reversed(), ZoomLevel.Years, today)
        assertEquals(listOf(1L, 2L, 3L, 4L, 5L, 6L), groups.first().books.map { it.id }.sorted())
        assertEquals("2026", groups.first().label)
    }

    @Test
    fun zoomStepsAreClamped() {
        assertEquals(ZoomLevel.Days, ZoomLevel.Days.zoomIn())
        assertEquals(ZoomLevel.Years, ZoomLevel.Years.zoomOut())
        assertEquals(ZoomLevel.Years, ZoomLevel.Weeks.zoomOut())
    }

    @Test
    fun countLabelPluralises() {
        assertEquals("1 book", countLabel(1))
        assertEquals("3 books", countLabel(3))
    }

    private fun book(id: Long, title: String, date: String) = Book(id = id, title = title, description = "", readDate = LocalDate.parse(date), coverPath = null, coverSource = null, createdAt = id)
}
