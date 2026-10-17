package com.andreaszel.booklcalendar.ui.log

import com.andreaszel.booklcalendar.data.ReadLog

const val MAX_PAGES = 9999

fun cleanPages(text: String): String = text.filter { it.isDigit() }.trimStart('0').take(4).ifEmpty { if (text.any { it.isDigit() }) "0" else "" }

fun parsePages(text: String): Int = text.toIntOrNull()?.coerceIn(0, MAX_PAGES) ?: 0

fun stepPages(text: String, delta: Int): String = (parsePages(text) + delta).coerceIn(0, MAX_PAGES).toString()

fun cutNote(text: String): String = text.take(ReadLog.NOTE_LIMIT)

fun parseTotalPages(text: String): Int? = text.toIntOrNull()?.takeIf { it in 1..MAX_PAGES }
