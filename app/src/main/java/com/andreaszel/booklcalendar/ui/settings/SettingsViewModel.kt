package com.andreaszel.booklcalendar.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andreaszel.booklcalendar.lookup.GoogleBooks
import com.andreaszel.booklcalendar.settings.ApiKeys
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewModel(private val keys: ApiKeys) : ViewModel() {
    private val _state = MutableStateFlow(SettingsUiState(draft = keys.googleBooks.value.orEmpty(), saved = keys.googleBooks.value != null, status = if (keys.googleBooks.value != null) KeyStatus.Saved else KeyStatus.None))
    val state: StateFlow<SettingsUiState> = _state

    fun onDraft(value: String) = _state.update { it.copy(draft = value) }

    fun save() {
        val key = _state.value.draft.trim()
        if (_state.value.busy) return
        if (key.isEmpty()) return remove()
        _state.update { it.copy(busy = true) }
        viewModelScope.launch {
            val valid = try {
                GoogleBooks.check(key)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                null
            }
            if (valid == false) {
                _state.update { it.copy(busy = false, status = KeyStatus.Rejected) }
                return@launch
            }
            keys.setGoogleBooks(key)
            _state.update { it.copy(draft = key, saved = true, busy = false, status = if (valid == true) KeyStatus.Working else KeyStatus.Untested) }
        }
    }

    fun remove() {
        keys.setGoogleBooks(null)
        _state.update { it.copy(draft = "", saved = false, status = KeyStatus.None) }
    }
}

data class SettingsUiState(val draft: String = "", val saved: Boolean = false, val busy: Boolean = false, val status: KeyStatus = KeyStatus.None)

enum class KeyStatus { None, Saved, Working, Untested, Rejected }
