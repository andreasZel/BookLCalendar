package com.andreaszel.booklcalendar.ui.shelf

import com.andreaszel.booklcalendar.data.Book
import java.text.Normalizer
import java.util.Locale

fun filterBooks(books: List<Book>, query: String): List<Book> {
    val terms = fold(query).split(Regex("\\s+")).filter { it.isNotEmpty() }
    if (terms.isEmpty()) return books
    return books.filter { book ->
        val text = fold("${book.title} ${book.author} ${book.description}")
        terms.all { text.contains(it) }
    }
}

private val marks = Regex("\\p{Mn}+")

private fun fold(text: String) = marks.replace(Normalizer.normalize(text, Normalizer.Form.NFD), "").lowercase(Locale.ROOT)
