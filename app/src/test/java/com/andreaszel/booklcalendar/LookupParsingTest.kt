package com.andreaszel.booklcalendar

import com.andreaszel.booklcalendar.lookup.GoogleBooks
import com.andreaszel.booklcalendar.lookup.OpenLibrary
import com.andreaszel.booklcalendar.lookup.Wikipedia
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class LookupParsingTest {
    @Test
    fun parsesGoogleBooksVolume() {
        val json = """{"items":[{"volumeInfo":{"title":"The Little Prince","authors":["Antoine de Saint-Exupéry","Richard Howard","Someone Else"],"description":"A pilot <i>meets</i> a prince.","industryIdentifiers":[{"type":"ISBN_10","identifier":"0156012197"},{"type":"ISBN_13","identifier":"9780156012195"}],"imageLinks":{"thumbnail":"http://books.google.com/books/content?id=x&zoom=1&edge=curl&source=gbs_api"}}},{"volumeInfo":{"authors":["No Title"]}}]}"""
        val matches = GoogleBooks.parse(json)
        assertEquals(1, matches.size)
        val match = matches.single()
        assertEquals("The Little Prince", match.title)
        assertEquals("Antoine de Saint-Exupéry, Richard Howard", match.author)
        assertEquals("A pilot meets a prince.", match.description)
        assertEquals("9780156012195", match.isbn)
        assertEquals(listOf("https://books.google.com/books/content?id=x&zoom=1&source=gbs_api"), match.coverUrls)
    }

    @Test
    fun googleBooksWithoutItemsIsEmpty() {
        assertTrue(GoogleBooks.parse("""{"kind":"books#volumes","totalItems":0}""").isEmpty())
    }

    @Test
    fun parsesOpenLibrarySearch() {
        val json = """{"docs":[{"key":"/works/OL45804W","title":"Piranesi","author_name":["Susanna Clarke"],"isbn":["1635575634","9781635575637"],"cover_i":10001}]}"""
        val match = OpenLibrary.parseSearch(json).single()
        assertEquals("Piranesi", match.title)
        assertEquals("Susanna Clarke", match.author)
        assertEquals("9781635575637", match.isbn)
        assertEquals("/works/OL45804W", match.workKey)
        assertEquals(listOf("https://covers.openlibrary.org/b/id/10001-L.jpg"), match.coverUrls)
    }

    @Test
    fun parsesOpenLibraryWorkDescriptionInBothShapes() {
        assertEquals("Plain text.", OpenLibrary.parseWork("""{"description":"Plain text."}"""))
        assertEquals("Typed text.", OpenLibrary.parseWork("""{"description":{"type":"/type/text","value":"Typed text."}}"""))
        assertEquals("", OpenLibrary.parseWork("""{"title":"No description"}"""))
    }

    @Test
    fun acceptsWikipediaBookPages() {
        val json = """{"type":"standard","description":"1943 novella by Antoine de Saint-Exupéry","extract":"The Little Prince is a novella."}"""
        assertEquals("The Little Prince is a novella.", Wikipedia.parseSummary(json, ""))
    }

    @Test
    fun acceptsWikipediaPageNamingAuthor() {
        val json = """{"type":"standard","description":"2021 work by Andy Weir","extract":"Text."}"""
        assertEquals("Text.", Wikipedia.parseSummary(json, "Andy Weir"))
    }

    @Test
    fun rejectsWikipediaNonBookPages() {
        assertNull(Wikipedia.parseSummary("""{"type":"disambiguation","description":"","extract":"Dune may refer to"}""", ""))
        assertNull(Wikipedia.parseSummary("""{"type":"standard","description":"Hill of sand","extract":"A dune is a landform."}""", "Frank Herbert"))
    }
}
