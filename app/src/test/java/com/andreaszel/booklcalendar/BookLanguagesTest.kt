package com.andreaszel.booklcalendar

import com.andreaszel.booklcalendar.settings.BookLanguages
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class BookLanguagesTest {
    @Test
    fun greekLocaleDefaultsToBoth() {
        assertEquals(BookLanguages.Both, BookLanguages.forLocale("el"))
        assertEquals(BookLanguages.Both, BookLanguages.forLocale("EL"))
    }

    @Test
    fun otherLocalesDefaultToEnglish() {
        assertEquals(BookLanguages.English, BookLanguages.forLocale("en"))
        assertEquals(BookLanguages.English, BookLanguages.forLocale("de"))
        assertEquals(BookLanguages.English, BookLanguages.forLocale(""))
    }

    @Test
    fun keysRoundTrip() {
        BookLanguages.entries.forEach { assertEquals(it, BookLanguages.fromKey(it.key)) }
        assertNull(BookLanguages.fromKey(null))
        assertNull(BookLanguages.fromKey("fr"))
    }

    @Test
    fun speechTagFollowsGreek() {
        assertEquals("en-US", BookLanguages.English.speechTag)
        assertEquals("el-GR", BookLanguages.Greek.speechTag)
        assertEquals("el-GR", BookLanguages.Both.speechTag)
    }
}
