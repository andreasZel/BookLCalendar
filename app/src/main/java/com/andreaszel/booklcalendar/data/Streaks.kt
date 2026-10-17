package com.andreaszel.booklcalendar.data

import java.time.LocalDate

data class Streak(val current: Int = 0, val best: Int = 0)

// Today without a log keeps yesterday's streak alive
fun currentStreak(dates: Collection<LocalDate>, today: LocalDate): Int {
    val days = dates.toHashSet()
    var day = if (today in days) today else today.minusDays(1)
    var count = 0
    while (day in days) {
        count++
        day = day.minusDays(1)
    }
    return count
}

fun bestStreak(dates: Collection<LocalDate>, today: LocalDate): Int {
    var best = 0
    var run = 0
    var previous: LocalDate? = null
    dates.filter { !it.isAfter(today) }.distinct().sorted().forEach { day ->
        run = if (previous?.plusDays(1) == day) run + 1 else 1
        best = maxOf(best, run)
        previous = day
    }
    return best
}

fun streakOf(dates: Collection<LocalDate>, today: LocalDate): Streak = Streak(current = currentStreak(dates, today), best = bestStreak(dates, today))
