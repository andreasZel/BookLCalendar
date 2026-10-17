package com.andreaszel.booklcalendar

import com.andreaszel.booklcalendar.data.Streak
import com.andreaszel.booklcalendar.data.bestStreak
import com.andreaszel.booklcalendar.data.currentStreak
import com.andreaszel.booklcalendar.data.streakOf
import com.andreaszel.booklcalendar.ui.components.streakLabel
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate

class StreaksTest {
    private val today = LocalDate.parse("2026-10-17")

    @Test
    fun noLogsMeansNoStreak() {
        assertEquals(Streak(0, 0), streakOf(emptyList(), today))
    }

    @Test
    fun countsBackFromToday() {
        assertEquals(3, currentStreak(dates("2026-10-15", "2026-10-16", "2026-10-17"), today))
    }

    @Test
    fun todayWithoutLogKeepsYesterdaysStreak() {
        assertEquals(2, currentStreak(dates("2026-10-15", "2026-10-16"), today))
    }

    @Test
    fun missedYesterdayBreaksTheStreak() {
        assertEquals(0, currentStreak(dates("2026-10-14", "2026-10-15"), today))
    }

    @Test
    fun gapStopsTheCount() {
        assertEquals(2, currentStreak(dates("2026-10-12", "2026-10-13", "2026-10-16", "2026-10-17"), today))
    }

    @Test
    fun runsAcrossMonthAndYearEdges() {
        assertEquals(4, currentStreak(dates("2026-09-29", "2026-09-30", "2026-10-01", "2026-10-02"), LocalDate.parse("2026-10-02")))
        assertEquals(3, bestStreak(dates("2025-12-30", "2025-12-31", "2026-01-01"), today))
        assertEquals(3, currentStreak(dates("2024-02-28", "2024-02-29", "2024-03-01"), LocalDate.parse("2024-03-01")))
    }

    @Test
    fun bestPicksTheLongestRun() {
        val logged = dates("2026-08-01", "2026-08-02", "2026-08-03", "2026-08-04", "2026-09-10", "2026-10-16", "2026-10-17")
        assertEquals(Streak(2, 4), streakOf(logged, today))
    }

    @Test
    fun duplicatesAndUnsortedDatesAreFine() {
        assertEquals(3, bestStreak(dates("2026-10-03", "2026-10-01", "2026-10-02", "2026-10-02"), today))
    }

    @Test
    fun futureDatesAreIgnored() {
        assertEquals(Streak(1, 1), streakOf(dates("2026-10-17", "2026-10-18", "2026-10-19"), today))
    }

    @Test
    fun labelMentionsBestOnlyWhenHigher() {
        assertEquals("1-day reading streak", streakLabel(Streak(1, 1)))
        assertEquals("3-day reading streak, best 9 days", streakLabel(Streak(3, 9)))
    }

    private fun dates(vararg values: String) = values.map { LocalDate.parse(it) }
}
