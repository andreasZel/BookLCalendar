package com.andreaszel.booklcalendar.lookup

import org.json.JSONObject

object Wikipedia {
    private const val BASE = "https://en.wikipedia.org/api/rest_v1/page/summary/"
    private val bookish = listOf("novel", "book", "memoir", "short stor", "collection", "poem", "play by", "graphic", "comic", "manga", "fiction", "biography")

    suspend fun summary(title: String, author: String): String? {
        for (page in listOf(title, "$title (novel)", "$title (book)")) {
            val text = runCatching { parseSummary(Http.get(BASE + Http.encode(page.trim().replace(' ', '_'))), author) }.getOrNull()
            if (text != null) return text
        }
        return null
    }

    fun parseSummary(json: String, author: String): String? {
        val page = JSONObject(json)
        if (page.optString("type") != "standard") return null
        val about = page.optString("description").lowercase()
        val surname = author.substringBefore(',').trim().substringAfterLast(' ').lowercase()
        val matches = bookish.any { it in about } || (surname.length > 1 && surname in about)
        if (!matches) return null
        return cleanDescription(page.optString("extract")).ifEmpty { null }
    }
}
