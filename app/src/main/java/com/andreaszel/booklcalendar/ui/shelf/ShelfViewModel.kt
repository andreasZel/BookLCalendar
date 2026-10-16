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
    private val search = MutableStateFlow(Search())

    val state: StateFlow<ShelfUiState> = combine(repository.observeAll(), level, search) { books, zoom, find ->
        ShelfUiState(loaded = true, level = zoom, books = books, searching = find.open, query = find.query, matches = filterBooks(books, find.query))
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), ShelfUiState())

    fun setLevel(next: ZoomLevel) {
        level.value = next
    }

    fun zoomIn() = setLevel(level.value.zoomIn())

    fun zoomOut() = setLevel(level.value.zoomOut())

    fun openSearch() {
        search.value = search.value.copy(open = true)
    }

    fun setQuery(query: String) {
        search.value = search.value.copy(query = query)
    }

    fun closeSearch() {
        search.value = Search()
    }

    private data class Search(val open: Boolean = false, val query: String = "")
}

data class ShelfUiState(val loaded: Boolean = false, val level: ZoomLevel = ZoomLevel.Weeks, val books: List<Book> = emptyList(), val searching: Boolean = false, val query: String = "", val matches: List<Book> = emptyList())
