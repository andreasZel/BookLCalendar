package com.andreaszel.booklcalendar

import com.andreaszel.booklcalendar.lookup.CoverLine
import com.andreaszel.booklcalendar.lookup.cleanDescription
import com.andreaszel.booklcalendar.lookup.coverQuery
import com.andreaszel.booklcalendar.lookup.normalizeIsbn
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class LookupTextTest {
    @Test
    fun stripsHtmlAndEntities() {
        assertEquals("A pilot & a prince. \"Hello\" – it's 'fine'.", cleanDescription("<p>A pilot &amp; a prince.</p><br/><b>&quot;Hello&quot;</b> &#8211; it&#39;s &apos;fine&apos;."))
    }

    @Test
    fun dropsOpenLibraryFootnotes() {
        assertEquals("A desert planet. More here.", cleanDescription("A desert planet. ([source][1])\r\n\r\nMore here.\r\n----------\r\nAlso contained in:\r\n[Dune Saga][2]\r\n\r\n[1]: https://example.org"))
    }

    @Test
    fun cutsLongTextAtSentence() {
        val text = "First sentence is here. " + "Second one goes on for a while. ".repeat(30)
        val cleaned = cleanDescription(text, max = 120)
        assertTrue(cleaned.length <= 120)
        assertTrue(cleaned.endsWith("."))
    }

    @Test
    fun cutsAtWordWhenNoSentenceFits() {
        assertEquals("word word word…", cleanDescription("word ".repeat(40), max = 16))
    }

    @Test
    fun normalizesIsbns() {
        assertEquals("9780156012195", normalizeIsbn("978-0-15-601219-5"))
        assertEquals("015601219X", normalizeIsbn("0-15-601219-x"))
        assertNull(normalizeIsbn("5012345678900"))
        assertNull(normalizeIsbn("12345"))
    }

    @Test
    fun coverQueryPrefersBiggestText() {
        val lines = listOf(CoverLine("The New York Times Bestseller", 30), CoverLine("PROJECT", 120), CoverLine("HAIL MARY", 110), CoverLine("ANDY WEIR", 60), CoverLine("~", 200))
        assertEquals("PROJECT HAIL MARY ANDY WEIR", coverQuery(lines))
    }
}
