package com.andreaszel.booklcalendar.ui.add

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andreaszel.booklcalendar.data.Book
import com.andreaszel.booklcalendar.data.BookRepository
import com.andreaszel.booklcalendar.data.CoverSource
import com.andreaszel.booklcalendar.data.CoverStore
import com.andreaszel.booklcalendar.lookup.BookLookup
import com.andreaszel.booklcalendar.lookup.BookMatch
import com.andreaszel.booklcalendar.lookup.CoverReader
import com.andreaszel.booklcalendar.lookup.coverQuery
import com.andreaszel.booklcalendar.lookup.normalizeIsbn
import com.andreaszel.booklcalendar.ui.log.cleanPages
import com.andreaszel.booklcalendar.ui.log.parseTotalPages
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

class AddBookViewModel(private val bookId: Long, private val repository: BookRepository, private val covers: CoverStore, private val lookup: BookLookup, private val coverReader: CoverReader) : ViewModel() {
    private val _state = MutableStateFlow(AddBookUiState(loaded = bookId == 0L, editing = bookId != 0L))
    val state: StateFlow<AddBookUiState> = _state

    private val imported = mutableSetOf<String>()
    private var original: Book? = null

    init {
        if (bookId != 0L) viewModelScope.launch {
            val book = repository.get(bookId)
            original = book
            _state.update { s -> if (book == null) s.copy(loaded = true) else s.copy(loaded = true, coverPath = book.coverPath, coverSource = book.coverSource, title = book.title, author = book.author, description = book.description, isbn = book.isbn, date = book.readDate, totalPages = book.totalPages?.toString() ?: "") }
        }
    }

    fun newCameraUri(): Uri = covers.newCameraUri()

    fun setCover(uri: Uri, source: CoverSource) {
        viewModelScope.launch {
            val path = runCatching { covers.import(uri) }.getOrNull() ?: return@launch
            dropImported(_state.value.coverPath)
            imported += path
            _state.update { it.copy(coverPath = path, coverSource = source, undo = it.undo?.copy(coverPath = path, coverSource = source)) }
        }
    }

    fun clearCover() {
        dropImported(_state.value.coverPath)
        _state.update { it.copy(coverPath = null, coverSource = null, undo = it.undo?.copy(coverPath = null, coverSource = null)) }
    }

    fun onTitle(value: String) = _state.update { it.copy(title = value, attempted = false) }

    fun onAuthor(value: String) = _state.update { it.copy(author = value) }

    fun onDescription(value: String) = _state.update { it.copy(description = value) }

    fun onTotalPages(value: String) = _state.update { it.copy(totalPages = cleanPages(value)) }

    fun onDate(value: LocalDate) = _state.update { it.copy(date = value) }

    fun findOnline() {
        val s = _state.value
        val typed = "${s.title} ${s.author}".trim()
        val cover = s.coverPath
        if (s.busy || (typed.isEmpty() && cover == null)) return
        runLookup {
            val query = typed.ifEmpty { coverQuery(coverReader.read(cover!!)) }
            if (query.isBlank()) throw NothingRead()
            lookup.search(query)
        }
    }

    fun lookupIsbn(raw: String) {
        if (_state.value.busy) return
        val isbn = normalizeIsbn(raw)
        if (isbn == null) {
            _state.update { it.copy(notice = NOT_ISBN) }
            return
        }
        runLookup { lookup.byIsbn(isbn) }
    }

    fun pick(index: Int) {
        val s = _state.value
        if (s.busy || index == s.picked || index !in s.matches.indices) return
        _state.update { it.copy(busy = true, notice = null) }
        viewModelScope.launch { applyMatch(index) }
    }

    fun undo() {
        val s = _state.value
        val draft = s.undo ?: return
        if (s.coverPath != draft.coverPath) dropImported(s.coverPath)
        _state.update { it.copy(title = draft.title, author = draft.author, description = draft.description, isbn = draft.isbn, coverPath = draft.coverPath, coverSource = draft.coverSource, undo = null, matches = emptyList(), picked = -1, notice = null) }
    }

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
            val book = base?.copy(title = s.title.trim(), author = s.author.trim(), description = s.description.trim(), isbn = s.isbn, readDate = s.date, coverPath = s.coverPath, coverSource = s.coverSource, totalPages = parseTotalPages(s.totalPages)) ?: Book(title = s.title.trim(), author = s.author.trim(), description = s.description.trim(), isbn = s.isbn, readDate = s.date, coverPath = s.coverPath, coverSource = s.coverSource, totalPages = parseTotalPages(s.totalPages))
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

    private fun runLookup(find: suspend () -> List<BookMatch>) {
        _state.update { it.copy(busy = true, notice = null) }
        viewModelScope.launch {
            val found = try {
                find()
            } catch (e: CancellationException) {
                throw e
            } catch (e: NothingRead) {
                _state.update { it.copy(busy = false, notice = UNREADABLE) }
                return@launch
            } catch (e: Exception) {
                _state.update { it.copy(busy = false, notice = OFFLINE) }
                return@launch
            }
            if (found.isEmpty()) {
                _state.update { it.copy(busy = false, notice = NO_MATCH) }
                return@launch
            }
            _state.update { it.copy(matches = found, picked = -1) }
            applyMatch(0)
        }
    }

    private suspend fun applyMatch(index: Int) {
        val s = _state.value
        val match = try {
            lookup.complete(s.matches[index])
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            s.matches[index]
        }
        val before = s.undo ?: Draft(s.title, s.author, s.description, s.isbn, s.coverPath, s.coverSource)
        val ownPhoto = s.coverPath != null && s.coverSource != CoverSource.Online
        var coverPath = s.coverPath
        var coverSource = s.coverSource
        if (!ownPhoto) {
            val downloaded = covers.download(match.coverUrls)
            if (s.coverPath != before.coverPath) dropImported(s.coverPath)
            if (downloaded != null) imported += downloaded
            coverPath = downloaded ?: before.coverPath
            coverSource = if (downloaded != null) CoverSource.Online else before.coverSource
        }
        _state.update { it.copy(title = match.title, author = match.author, description = match.description.ifBlank { s.description }, isbn = match.isbn, coverPath = coverPath, coverSource = coverSource, undo = before, picked = index, attempted = false, busy = false, notice = if (match.description.isBlank()) NO_DESCRIPTION else null) }
    }

    private fun dropImported(path: String?) {
        if (path == null || !imported.remove(path)) return
        viewModelScope.launch { covers.delete(path) }
    }

    private class NothingRead : Exception()

    private companion object {
        const val NOT_ISBN = "That barcode isn't a book ISBN. Try the one on the back cover."
        const val UNREADABLE = "I couldn't read the cover. Type the title and try again."
        const val OFFLINE = "Couldn't reach the book databases. Check your connection and try again."
        const val NO_MATCH = "No matches found. Check the spelling, or fill it in yourself."
        const val NO_DESCRIPTION = "Found it, but there's no description online. Add your own!"
    }
}

data class AddBookUiState(val loaded: Boolean = false, val editing: Boolean = false, val coverPath: String? = null, val coverSource: CoverSource? = null, val title: String = "", val author: String = "", val description: String = "", val isbn: String? = null, val date: LocalDate = LocalDate.now(), val totalPages: String = "", val attempted: Boolean = false, val undo: Draft? = null, val matches: List<BookMatch> = emptyList(), val picked: Int = -1, val notice: String? = null, val busy: Boolean = false, val saved: Boolean = false)

data class Draft(val title: String, val author: String, val description: String, val isbn: String?, val coverPath: String?, val coverSource: CoverSource?)
