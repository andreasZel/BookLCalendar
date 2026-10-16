package com.andreaszel.booklcalendar.ui.add

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andreaszel.booklcalendar.data.Book
import com.andreaszel.booklcalendar.data.BookRepository
import com.andreaszel.booklcalendar.data.CoverSource
import com.andreaszel.booklcalendar.data.CoverStore
import com.andreaszel.booklcalendar.suggest.SuggestionService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

class AddBookViewModel(private val bookId: Long, private val repository: BookRepository, private val covers: CoverStore, private val suggestions: SuggestionService) : ViewModel() {
    private val _state = MutableStateFlow(AddBookUiState(loaded = bookId == 0L, editing = bookId != 0L))
    val state: StateFlow<AddBookUiState> = _state

    private val imported = mutableSetOf<String>()
    private var original: Book? = null

    init {
        if (bookId != 0L) viewModelScope.launch {
            val book = repository.get(bookId)
            original = book
            _state.update { s -> if (book == null) s.copy(loaded = true) else s.copy(loaded = true, coverPath = book.coverPath, coverSource = book.coverSource, title = book.title, description = book.description, date = book.readDate) }
        }
    }

    fun newCameraUri(): Uri = covers.newCameraUri()

    fun setCover(uri: Uri, source: CoverSource) {
        viewModelScope.launch {
            val path = runCatching { covers.import(uri) }.getOrNull() ?: return@launch
            dropImported(_state.value.coverPath)
            imported += path
            _state.update { it.copy(coverPath = path, coverSource = source) }
        }
    }

    fun clearCover() {
        dropImported(_state.value.coverPath)
        _state.update { it.copy(coverPath = null, coverSource = null) }
    }

    fun onTitle(value: String) = _state.update { it.copy(title = value, attempted = false) }

    fun onDescription(value: String) = _state.update { it.copy(description = value) }

    fun onDate(value: LocalDate) = _state.update { it.copy(date = value) }

    fun suggest() {
        val s = _state.value
        val hasTitle = s.title.isNotBlank()
        if (s.busy || (s.coverPath == null && !hasTitle)) return
        val before = s.undo ?: Draft(s.title, s.description)
        _state.update { it.copy(busy = true) }
        viewModelScope.launch {
            if (s.coverPath != null && (!hasTitle || s.undo != null)) {
                val pick = suggestions.fromCover(s.coverPath, s.coverRound)
                _state.update { it.copy(title = pick.title, description = pick.description, undo = before, coverRound = s.coverRound + 1, attempted = false, busy = false) }
            } else {
                val text = suggestions.describe(s.title)
                _state.update { it.copy(description = text, undo = before, busy = false) }
            }
        }
    }

    fun undo() = _state.update { s -> s.undo?.let { s.copy(title = it.title, description = it.description, undo = null) } ?: s }

    fun save() {
        val s = _state.value
        if (s.busy || s.saved) return
        if (s.title.isBlank()) {
            _state.update { it.copy(attempted = true) }
            return
        }
        _state.update { it.copy(busy = true) }
        viewModelScope.launch {
            val base = original
            val book = base?.copy(title = s.title.trim(), description = s.description.trim(), readDate = s.date, coverPath = s.coverPath, coverSource = s.coverSource) ?: Book(title = s.title.trim(), description = s.description.trim(), readDate = s.date, coverPath = s.coverPath, coverSource = s.coverSource)
            repository.save(book)
            imported.remove(s.coverPath)
            imported.toList().forEach { covers.delete(it) }
            imported.clear()
            _state.update { it.copy(saved = true, busy = false) }
        }
    }

    fun discard() {
        val paths = imported.toList()
        imported.clear()
        viewModelScope.launch { paths.forEach { covers.delete(it) } }
    }

    private fun dropImported(path: String?) {
        if (path == null || !imported.remove(path)) return
        viewModelScope.launch { covers.delete(path) }
    }
}

data class AddBookUiState(val loaded: Boolean = false, val editing: Boolean = false, val coverPath: String? = null, val coverSource: CoverSource? = null, val title: String = "", val description: String = "", val date: LocalDate = LocalDate.now(), val attempted: Boolean = false, val undo: Draft? = null, val coverRound: Int = 0, val busy: Boolean = false, val saved: Boolean = false)

data class Draft(val title: String, val description: String)
