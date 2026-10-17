package com.andreaszel.booklcalendar

import android.content.Context
import com.andreaszel.booklcalendar.data.AppDatabase
import com.andreaszel.booklcalendar.data.BookRepository
import com.andreaszel.booklcalendar.data.CoverStore
import com.andreaszel.booklcalendar.data.ReadLogRepository
import com.andreaszel.booklcalendar.lookup.BookLookup
import com.andreaszel.booklcalendar.lookup.CoverReader
import com.andreaszel.booklcalendar.lookup.OnlineBookLookup
import com.andreaszel.booklcalendar.settings.ApiKeys
import com.andreaszel.booklcalendar.settings.LanguagePrefs

class AppContainer(context: Context) {
    private val database = AppDatabase.create(context)
    val covers = CoverStore(context)
    val books = BookRepository(database.bookDao(), covers)
    val logs = ReadLogRepository(database.readLogDao())
    val keys = ApiKeys(context)
    val languages = LanguagePrefs(context)
    val lookup: BookLookup = OnlineBookLookup(keys, languages)
    val coverReader = CoverReader(context)
}
