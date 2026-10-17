package com.andreaszel.booklcalendar.ui.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andreaszel.booklcalendar.data.Book
import com.andreaszel.booklcalendar.data.BookRepository
import com.andreaszel.booklcalendar.data.ReadLog
import com.andreaszel.booklcalendar.data.ReadLogRepository
import com.andreaszel.booklcalendar.data.Streak
import com.andreaszel.booklcalendar.data.pagesPerDay
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDate
import java.time.YearMonth

@OptIn(ExperimentalCoroutinesApi::class)
class CalendarViewModel(books: BookRepository, logs: ReadLogRepository) : ViewModel() {
    private val month = MutableStateFlow(YearMonth.now())
    private val selected = MutableStateFlow<LocalDate?>(null)
    private val monthLogs = month.flatMapLatest { current -> logs.observeMonth(current).map { current to it } }

    val state: StateFlow<CalendarUiState> = combine(monthLogs, books.observeAll(), selected, logs.observeStreak()) { (current, list), shelf, day, streak ->
        val byId = shelf.associateBy { it.id }
        val entries = if (day == null) emptyList() else list.filter { it.date == day }.mapNotNull { log -> byId[log.bookId]?.let { DayEntry(log, it) } }
        CalendarUiState(loaded = true, month = current, pages = pagesPerDay(list, current), selected = day, day = entries, streak = streak)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), CalendarUiState())

    fun previous() = show(month.value.minusMonths(1))

    fun next() = show(month.value.plusMonths(1))

    fun openDay(date: LocalDate) {
        selected.value = date
    }

    fun closeDay() {
        selected.value = null
    }

    private fun show(next: YearMonth) {
        selected.value = null
        month.value = next
    }
}

data class CalendarUiState(val loaded: Boolean = false, val month: YearMonth = YearMonth.now(), val pages: Map<LocalDate, Int> = emptyMap(), val selected: LocalDate? = null, val day: List<DayEntry> = emptyList(), val streak: Streak = Streak())

data class DayEntry(val log: ReadLog, val book: Book)
