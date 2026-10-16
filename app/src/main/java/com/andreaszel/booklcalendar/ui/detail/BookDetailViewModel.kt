package com.andreaszel.booklcalendar.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andreaszel.booklcalendar.data.Book
import com.andreaszel.booklcalendar.data.BookRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class BookDetailViewModel(bookId: Long, private val repository: BookRepository) : ViewModel() {
    val state: StateFlow<BookDetailUiState> = repository.observe(bookId).map { BookDetailUiState(loaded = true, book = it) }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), BookDetailUiState())

    fun delete(onDeleted: () -> Unit) {
        val book = state.value.book ?: return
        viewModelScope.launch {
            repository.delete(book)
            onDeleted()
        }
    }
}

data class BookDetailUiState(val loaded: Boolean = false, val book: Book? = null)
