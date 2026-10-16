package com.andreaszel.booklcalendar.suggest

interface SuggestionService {
    suspend fun fromCover(coverPath: String, attempt: Int): Suggestion

    suspend fun describe(title: String): String
}

data class Suggestion(val title: String, val description: String)
