package com.andreaszel.booklcalendar

import android.content.Context
import com.andreaszel.booklcalendar.data.AppDatabase
import com.andreaszel.booklcalendar.data.BookRepository
import com.andreaszel.booklcalendar.data.CoverStore
import com.andreaszel.booklcalendar.lookup.BookLookup
import com.andreaszel.booklcalendar.lookup.CoverReader
import com.andreaszel.booklcalendar.lookup.OnlineBookLookup
import com.andreaszel.booklcalendar.settings.ApiKeys

class AppContainer(context: Context) {
    private val database = AppDatabase.create(context)
    val covers = CoverStore(context)
    val books = BookRepository(database.bookDao(), covers)
    val keys = ApiKeys(context)
    val lookup: BookLookup = OnlineBookLookup(keys)
    val coverReader = CoverReader(context)
}
