package com.andreaszel.booklcalendar.settings

import android.content.Context
import androidx.core.content.edit
import java.util.Locale
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class LanguagePrefs(context: Context) {
    private val prefs = context.getSharedPreferences(FILE, Context.MODE_PRIVATE)
    private val _languages = MutableStateFlow(BookLanguages.fromKey(prefs.getString(LANGUAGES, null)) ?: BookLanguages.forLocale(Locale.getDefault().language))
    val languages: StateFlow<BookLanguages> = _languages

    fun set(value: BookLanguages) {
        prefs.edit { putString(LANGUAGES, value.key) }
        _languages.value = value
    }

    private companion object {
        const val FILE = "app_settings"
        const val LANGUAGES = "book_languages"
    }
}
