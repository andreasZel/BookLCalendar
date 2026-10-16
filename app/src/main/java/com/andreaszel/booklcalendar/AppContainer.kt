package com.andreaszel.booklcalendar

import android.content.Context
import com.andreaszel.booklcalendar.data.AppDatabase
import com.andreaszel.booklcalendar.data.BookRepository
import com.andreaszel.booklcalendar.data.CoverStore

class AppContainer(context: Context) {
    private val database = AppDatabase.create(context)
    val covers = CoverStore(context)
    val books = BookRepository(database.bookDao(), covers)
}
