package com.andreaszel.booklcalendar

import com.andreaszel.booklcalendar.data.Book
import com.andreaszel.booklcalendar.data.ReadLog
import com.andreaszel.booklcalendar.data.pagesPerDay
import com.andreaszel.booklcalendar.data.pagesReadFor
import com.andreaszel.booklcalendar.data.progressOf
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import java.time.LocalDate
import java.time.YearMonth

class ReadLogStatsTest {
    private val logs = listOf(
        log(1, "2026-10-01", 20),
        log(2, "2026-10-01", 15),
        log(1, "2026-10-02", 30),
        log(1, "2026-10-31", 10),
        log(2, "2026-09-30", 50),
        log(1, "2026-11-01", 40),
    )

    @Test
    fun pagesPerDaySumsBooksOnTheSameDay() {
        val days = pagesPerDay(logs, YearMonth.of(2026, 10))
        assertEquals(mapOf(LocalDate.parse("2026-10-01") to 35, LocalDate.parse("2026-10-02") to 30, LocalDate.parse("2026-10-31") to 10), days)
    }

    @Test
    fun pagesPerDayKeepsOnlyTheMonthInOrder() {
        val days = pagesPerDay(logs, YearMonth.of(2026, 10))
        assertEquals(listOf("2026-10-01", "2026-10-02", "2026-10-31"), days.keys.map { it.toString() })
        assertEquals(emptyMap<LocalDate, Int>(), pagesPerDay(logs, YearMonth.of(2026, 12)))
    }

    @Test
    fun pagesReadForSumsAcrossMonths() {
        assertEquals(100, pagesReadFor(logs, 1))
        assertEquals(65, pagesReadFor(logs, 2))
        assertEquals(0, pagesReadFor(logs, 3))
    }

    @Test
    fun progressNeedsTotalPagesAndIsCapped() {
        assertNull(progressOf(book(1, null), logs))
        assertNull(progressOf(book(1, 0), logs))
        assertEquals(0.5f, progressOf(book(1, 200), logs)!!, 0.0001f)
        assertEquals(1f, progressOf(book(1, 80), logs)!!, 0.0001f)
    }

    private fun log(bookId: Long, date: String, pages: Int) = ReadLog(bookId = bookId, date = LocalDate.parse(date), pagesRead = pages)

    private fun book(id: Long, totalPages: Int?) = Book(id = id, title = "Book $id", description = "", readDate = LocalDate.parse("2026-10-01"), coverPath = null, coverSource = null, totalPages = totalPages)
}
