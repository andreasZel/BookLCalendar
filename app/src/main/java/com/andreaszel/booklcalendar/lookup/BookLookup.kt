package com.andreaszel.booklcalendar.lookup

interface BookLookup {
    suspend fun byIsbn(isbn: String): List<BookMatch>

    suspend fun search(query: String): List<BookMatch>

    suspend fun complete(match: BookMatch): BookMatch
}

data class BookMatch(val title: String, val author: String, val description: String, val isbn: String?, val coverUrls: List<String>, val workKey: String? = null)
