package com.andreaszel.booklcalendar

import com.andreaszel.booklcalendar.ui.add.shelfHint
import com.andreaszel.booklcalendar.ui.add.suggestCopy
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate

class AddBookCopyTest {
    private val today = LocalDate.of(2026, 10, 16)

    @Test
    fun suggestDisabledWithoutPhotoOrTitle() {
        val copy = suggestCopy(hasPhoto = false, hasTitle = false, suggested = false, busy = false)
        assertFalse(copy.enabled)
        assertEquals("Add a photo or type a title, and I can fill in the rest.", copy.hint)
    }

    @Test
    fun suggestFromPhotoFillsBoth() {
        val copy = suggestCopy(hasPhoto = true, hasTitle = false, suggested = false, busy = false)
        assertTrue(copy.enabled)
        assertEquals("Suggest title & description", copy.label)
    }

    @Test
    fun suggestFromTitleWritesDescription() {
        assertEquals("Suggest description", suggestCopy(hasPhoto = false, hasTitle = true, suggested = false, busy = false).label)
    }

    @Test
    fun afterSuggestionOffersAnother() {
        val copy = suggestCopy(hasPhoto = true, hasTitle = true, suggested = true, busy = false)
        assertEquals("Suggest another", copy.label)
        assertEquals("Filled in by suggestion — tap again for another, or Undo.", copy.hint)
    }

    @Test
    fun busyDisablesSuggest() {
        assertFalse(suggestCopy(hasPhoto = true, hasTitle = true, suggested = false, busy = true).enabled)
    }

    @Test
    fun shelfHintNamesGroup() {
        assertEquals("Today", shelfHint(today, today))
        assertEquals("Yesterday", shelfHint(today.minusDays(1), today))
        assertEquals("the week of 28 Sep – 4 Oct 2026", shelfHint(LocalDate.of(2026, 9, 29), today))
    }
}
