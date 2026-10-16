package com.andreaszel.booklcalendar.settings

import android.content.Context
import androidx.core.content.edit
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

// Own prefs file so backup rules can exclude it
class ApiKeys(context: Context) {
    private val prefs = context.getSharedPreferences(FILE, Context.MODE_PRIVATE)
    private val _googleBooks = MutableStateFlow(prefs.getString(GOOGLE_BOOKS, null))
    val googleBooks: StateFlow<String?> = _googleBooks

    fun setGoogleBooks(key: String?) {
        val value = key?.trim()?.ifEmpty { null }
        prefs.edit { if (value == null) remove(GOOGLE_BOOKS) else putString(GOOGLE_BOOKS, value) }
        _googleBooks.value = value
    }

    private companion object {
        const val FILE = "api_keys"
        const val GOOGLE_BOOKS = "google_books"
    }
}
