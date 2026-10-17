package com.andreaszel.booklcalendar

import com.andreaszel.booklcalendar.data.ReadLog
import com.andreaszel.booklcalendar.ui.detail.readingSummary
import com.andreaszel.booklcalendar.ui.log.appendNote
import com.andreaszel.booklcalendar.ui.log.cleanPages
import com.andreaszel.booklcalendar.ui.log.cutNote
import com.andreaszel.booklcalendar.ui.log.parsePages
import com.andreaszel.booklcalendar.ui.log.parseTotalPages
import com.andreaszel.booklcalendar.ui.log.stepPages
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class LogFormTest {
    @Test
    fun cleanPagesKeepsDigitsAndDropsLeadingZeros() {
        assertEquals("42", cleanPages("4a2"))
        assertEquals("7", cleanPages("007"))
        assertEquals("0", cleanPages("000"))
        assertEquals("", cleanPages(""))
        assertEquals("", cleanPages("-"))
        assertEquals("1234", cleanPages("123456"))
    }

    @Test
    fun parsePagesTreatsBlankAsZero() {
        assertEquals(0, parsePages(""))
        assertEquals(35, parsePages("35"))
    }

    @Test
    fun stepPagesStaysInRange() {
        assertEquals("1", stepPages("", 1))
        assertEquals("0", stepPages("0", -1))
        assertEquals("9", stepPages("10", -1))
        assertEquals("9999", stepPages("9999", 1))
    }

    @Test
    fun cutNoteStopsAtLimit() {
        assertEquals(ReadLog.NOTE_LIMIT, cutNote("a".repeat(400)).length)
        assertEquals("short", cutNote("short"))
    }

    @Test
    fun appendNoteJoinsSpokenTextWithOneSpace() {
        assertEquals("Ωραίο κεφάλαιο", appendNote("", "  Ωραίο κεφάλαιο "))
        assertEquals("Great start. Loved it", appendNote("Great start.", "Loved it"))
        assertEquals("Line one\nLoved it", appendNote("Line one\n", "Loved it"))
        assertEquals("Loved it", appendNote("   ", "Loved it"))
        assertEquals("Kept", appendNote("Kept", "  "))
    }

    @Test
    fun appendNoteCutsAtLimit() {
        val note = "a".repeat(ReadLog.NOTE_LIMIT - 3)
        assertEquals(note + " bb", appendNote(note, "bbbbbb"))
        assertEquals(ReadLog.NOTE_LIMIT, appendNote("a".repeat(ReadLog.NOTE_LIMIT), "more").length)
    }

    @Test
    fun totalPagesNeedsAPositiveNumber() {
        assertNull(parseTotalPages(""))
        assertNull(parseTotalPages("0"))
        assertEquals(320, parseTotalPages("320"))
    }

    @Test
    fun readingSummaryCoversEachState() {
        assertEquals("No pages logged yet. Add the total pages in Edit to see your progress.", readingSummary(pagesRead = 0, days = 0, totalPages = null))
        assertEquals("No pages logged yet, 300 pages to go.", readingSummary(pagesRead = 0, days = 0, totalPages = 300))
        assertEquals("45 pages on 2 days", readingSummary(pagesRead = 45, days = 2, totalPages = null))
        assertEquals("120 of 300 pages on 1 day", readingSummary(pagesRead = 120, days = 1, totalPages = 300))
        assertEquals("Finished! 310 of 300 pages on 9 days", readingSummary(pagesRead = 310, days = 9, totalPages = 300))
    }
}
