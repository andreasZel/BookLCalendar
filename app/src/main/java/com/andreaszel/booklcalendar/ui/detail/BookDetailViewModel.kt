package com.andreaszel.booklcalendar.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andreaszel.booklcalendar.data.Book
import com.andreaszel.booklcalendar.data.BookRepository
import com.andreaszel.booklcalendar.data.ReadLogRepository
import com.andreaszel.booklcalendar.data.progressOf
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class BookDetailViewModel(bookId: Long, private val repository: BookRepository, logs: ReadLogRepository) : ViewModel() {
    val state: StateFlow<BookDetailUiState> = combine(repository.observe(bookId), logs.observeForBook(bookId)) { book, list ->
        BookDetailUiState(loaded = true, book = book, pagesRead = list.sumOf { it.pagesRead }, days = list.size, progress = book?.let { progressOf(it, list) })
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), BookDetailUiState())

    fun delete(onDeleted: () -> Unit) {
        val book = state.value.book ?: return
        viewModelScope.launch {
            repository.delete(book)
            onDeleted()
        }
    }
}

data class BookDetailUiState(val loaded: Boolean = false, val book: Book? = null, val pagesRead: Int = 0, val days: Int = 0, val progress: Float? = null)
