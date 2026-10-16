package com.andreaszel.booklcalendar.ui.shelf

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andreaszel.booklcalendar.data.Book
import com.andreaszel.booklcalendar.data.BookRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class ShelfViewModel(repository: BookRepository) : ViewModel() {
    private val level = MutableStateFlow(ZoomLevel.Weeks)

    val state: StateFlow<ShelfUiState> = combine(repository.observeAll(), level) { books, zoom ->
        ShelfUiState(loaded = true, level = zoom, books = books)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), ShelfUiState())

    fun setLevel(next: ZoomLevel) {
        level.value = next
    }

    fun zoomIn() = setLevel(level.value.zoomIn())

    fun zoomOut() = setLevel(level.value.zoomOut())
}

data class ShelfUiState(val loaded: Boolean = false, val level: ZoomLevel = ZoomLevel.Weeks, val books: List<Book> = emptyList())
