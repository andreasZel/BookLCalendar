package com.andreaszel.booklcalendar

import com.andreaszel.booklcalendar.ui.calendar.heatLevel
import com.andreaszel.booklcalendar.ui.calendar.monthCells
import com.andreaszel.booklcalendar.ui.calendar.pagesLabel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import java.time.LocalDate
import java.time.YearMonth

class CalendarMonthTest {
    @Test
    fun monthStartingThursdayHasThreeLeadingBlanks() {
        val cells = monthCells(YearMonth.of(2026, 10))
        assertEquals(35, cells.size)
        assertEquals(listOf(null, null, null), cells.take(3))
        assertEquals(LocalDate.of(2026, 10, 1), cells[3])
        assertEquals(LocalDate.of(2026, 10, 31), cells[33])
        assertNull(cells[34])
    }

    @Test
    fun monthStartingMondayFillsExactWeeks() {
        val cells = monthCells(YearMonth.of(2021, 2))
        assertEquals(28, cells.size)
        assertEquals(LocalDate.of(2021, 2, 1), cells.first())
        assertEquals(LocalDate.of(2021, 2, 28), cells.last())
    }

    @Test
    fun monthStartingSundayNeedsSixWeeks() {
        val cells = monthCells(YearMonth.of(2026, 3))
        assertEquals(42, cells.size)
        assertEquals(LocalDate.of(2026, 3, 1), cells[6])
        assertEquals(31, cells.filterNotNull().size)
    }

    @Test
    fun leapFebruaryHas29Days() {
        assertEquals(29, monthCells(YearMonth.of(2028, 2)).filterNotNull().size)
    }

    @Test
    fun heatLevelsFollowPageThresholds() {
        assertEquals(0, heatLevel(null))
        assertEquals(1, heatLevel(0))
        assertEquals(1, heatLevel(19))
        assertEquals(2, heatLevel(20))
        assertEquals(2, heatLevel(49))
        assertEquals(3, heatLevel(50))
        assertEquals(3, heatLevel(900))
    }

    @Test
    fun pagesLabelUsesSingular() {
        assertEquals("1 page", pagesLabel(1))
        assertEquals("12 pages", pagesLabel(12))
    }
}
