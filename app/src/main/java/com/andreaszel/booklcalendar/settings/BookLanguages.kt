package com.andreaszel.booklcalendar.settings

enum class BookLanguages(val key: String, val english: Boolean, val greek: Boolean) {
    English("en", english = true, greek = false),
    Greek("el", english = false, greek = true),
    Both("en_el", english = true, greek = true);

    val speechTag: String get() = if (greek) "el-GR" else "en-US"

    companion object {
        fun fromKey(key: String?): BookLanguages? = entries.firstOrNull { it.key == key }

        fun forLocale(language: String): BookLanguages = if (language.lowercase() == "el") Both else English
    }
}
