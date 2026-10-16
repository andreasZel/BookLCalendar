package com.andreaszel.booklcalendar.ui

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters

object DateLabels {
    private val months = listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")
    private val weekdays = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")

    fun month(date: LocalDate) = months[date.monthValue - 1]

    fun weekday(date: LocalDate) = weekdays[date.dayOfWeek.value - 1]

    fun weekStart(date: LocalDate): LocalDate = date.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))

    fun full(date: LocalDate) = "${weekday(date)}, ${date.dayOfMonth} ${month(date)} ${date.year}"

    fun day(date: LocalDate, today: LocalDate): String {
        if (date == today) return "Today"
        if (date == today.minusDays(1)) return "Yesterday"
        val year = if (date.year == today.year) "" else " ${date.year}"
        return "${weekday(date)}, ${date.dayOfMonth} ${month(date)}$year"
    }

    fun week(monday: LocalDate, today: LocalDate): String {
        if (monday == weekStart(today)) return "This week"
        val sunday = monday.plusDays(6)
        val year = if (sunday.year == today.year) "" else " ${sunday.year}"
        if (monday.month == sunday.month) return "${monday.dayOfMonth} – ${sunday.dayOfMonth} ${month(sunday)}$year"
        return "${monday.dayOfMonth} ${month(monday)} – ${sunday.dayOfMonth} ${month(sunday)}$year"
    }
}
