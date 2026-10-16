package com.andreaszel.booklcalendar

import com.andreaszel.booklcalendar.data.Book
import com.andreaszel.booklcalendar.ui.shelf.filterBooks
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate

class ShelfSearchTest {
    private val books = listOf(
        book(1, "Piranesi", "A man lives in an endless house of statues and tides."),
        book(2, "The Little Prince", "A pilot meets a boy from a tiny planet."),
        book(3, "Project Hail Mary", "A lone astronaut wakes up far from Earth."),
        book(4, "Les Misérables", "Jean Valjean and the barricades of Paris.", "Victor Hugo"),
    )

    @Test
    fun blankQueryKeepsEveryBook() {
        assertEquals(books, filterBooks(books, "   "))
    }

    @Test
    fun matchesTitleIgnoringCase() {
        assertEquals(listOf(2L), ids("little PRINCE"))
    }

    @Test
    fun matchesDescription() {
        assertEquals(listOf(3L), ids("astronaut"))
    }

    @Test
    fun everyWordMustMatchAcrossTitleAndDescription() {
        assertEquals(listOf(1L), ids("piranesi statues"))
        assertEquals(emptyList<Long>(), ids("piranesi planet"))
    }

    @Test
    fun matchesAuthor() {
        assertEquals(listOf(4L), ids("hugo"))
    }

    @Test
    fun ignoresAccents() {
        assertEquals(listOf(4L), ids("miserables"))
        assertEquals(listOf(4L), ids("Misérables"))
    }

    private fun ids(query: String) = filterBooks(books, query).map { it.id }

    private fun book(id: Long, title: String, description: String, author: String = "") = Book(id = id, title = title, author = author, description = description, readDate = LocalDate.of(2026, 10, 16), coverPath = null, coverSource = null)
}
