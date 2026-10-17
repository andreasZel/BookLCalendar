package com.andreaszel.booklcalendar.ui.log

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andreaszel.booklcalendar.data.Book
import com.andreaszel.booklcalendar.data.BookRepository
import com.andreaszel.booklcalendar.data.ReadLog
import com.andreaszel.booklcalendar.data.ReadLogRepository
import com.andreaszel.booklcalendar.ui.shelf.filterBooks
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

class LogReadViewModel(bookId: Long, private val date: LocalDate, books: BookRepository, private val logs: ReadLogRepository) : ViewModel() {
    private val form = MutableStateFlow(LogDraft())

    val state: StateFlow<LogReadUiState> = combine(books.observeAll(), form) { shelf, draft ->
        LogReadUiState(loaded = true, date = date, shelfEmpty = shelf.isEmpty(), matches = filterBooks(shelf, draft.query), book = shelf.firstOrNull { it.id == draft.bookId }, query = draft.query, pages = draft.pages, note = draft.note, existing = draft.existing, busy = draft.busy, done = draft.done)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), LogReadUiState(date = date))

    init {
        if (bookId != 0L) pick(bookId)
    }

    fun onQuery(value: String) = form.update { it.copy(query = value) }

    fun onPages(value: String) = form.update { it.copy(pages = cleanPages(value)) }

    fun step(delta: Int) = form.update { it.copy(pages = stepPages(it.pages, delta)) }

    fun onNote(value: String) = form.update { it.copy(note = cutNote(value)) }

    // An existing log for that day replaces the typed values
    fun pick(id: Long) {
        form.update { it.copy(bookId = id, busy = true) }
        viewModelScope.launch {
            val existing = logs.get(id, date)
            form.update { d ->
                if (d.bookId != id) return@update d
                when {
                    existing != null -> d.copy(existing = existing, pages = existing.pagesRead.toString(), note = existing.note, busy = false)
                    d.existing != null -> d.copy(existing = null, pages = "", note = "", busy = false)
                    else -> d.copy(busy = false)
                }
            }
        }
    }

    fun changeBook() = form.update { it.copy(bookId = 0L, query = "") }

    fun save() {
        val d = form.value
        if (d.busy || d.done || d.bookId == 0L) return
        form.update { it.copy(busy = true) }
        viewModelScope.launch {
            logs.save(ReadLog(bookId = d.bookId, date = date, pagesRead = parsePages(d.pages), note = d.note))
            form.update { it.copy(busy = false, done = true) }
        }
    }

    fun delete() {
        val d = form.value
        val log = d.existing ?: return
        if (d.busy || d.done) return
        form.update { it.copy(busy = true) }
        viewModelScope.launch {
            logs.delete(log)
            form.update { it.copy(busy = false, done = true) }
        }
    }
}

data class LogReadUiState(val loaded: Boolean = false, val date: LocalDate, val shelfEmpty: Boolean = false, val matches: List<Book> = emptyList(), val book: Book? = null, val query: String = "", val pages: String = "", val note: String = "", val existing: ReadLog? = null, val busy: Boolean = false, val done: Boolean = false)

data class LogDraft(val bookId: Long = 0L, val query: String = "", val pages: String = "", val note: String = "", val existing: ReadLog? = null, val busy: Boolean = false, val done: Boolean = false)
