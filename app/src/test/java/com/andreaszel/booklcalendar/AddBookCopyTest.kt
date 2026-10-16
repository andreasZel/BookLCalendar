package com.andreaszel.booklcalendar

import com.andreaszel.booklcalendar.ui.add.lookupCopy
import com.andreaszel.booklcalendar.ui.add.shelfHint
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate

class AddBookCopyTest {
    private val today = LocalDate.of(2026, 10, 16)

    @Test
    fun lookupDisabledWithoutPhotoOrTitle() {
        val copy = lookupCopy(hasPhoto = false, hasQuery = false, found = false, busy = false)
        assertFalse(copy.enabled)
        assertEquals("Scan the barcode, add a cover photo or type a title, and I can fill in the rest.", copy.hint)
    }

    @Test
    fun lookupFromPhotoReadsCover() {
        val copy = lookupCopy(hasPhoto = true, hasQuery = false, found = false, busy = false)
        assertTrue(copy.enabled)
        assertEquals("Read the cover", copy.label)
    }

    @Test
    fun typedTitleWinsOverPhoto() {
        assertEquals("Find by title", lookupCopy(hasPhoto = true, hasQuery = true, found = false, busy = false).label)
    }

    @Test
    fun afterLookupOffersOtherMatches() {
        assertEquals("Filled in from online book info. Not it? Pick another match, or Undo.", lookupCopy(hasPhoto = false, hasQuery = true, found = true, busy = false).hint)
    }

    @Test
    fun busyDisablesLookup() {
        val copy = lookupCopy(hasPhoto = true, hasQuery = true, found = false, busy = true)
        assertFalse(copy.enabled)
        assertEquals("Looking up…", copy.label)
    }

    @Test
    fun shelfHintNamesGroup() {
        assertEquals("Today", shelfHint(today, today))
        assertEquals("Yesterday", shelfHint(today.minusDays(1), today))
        assertEquals("the week of 28 Sep – 4 Oct 2026", shelfHint(LocalDate.of(2026, 9, 29), today))
    }
}
