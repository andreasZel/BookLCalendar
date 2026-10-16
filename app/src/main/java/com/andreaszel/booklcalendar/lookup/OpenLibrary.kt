package com.andreaszel.booklcalendar.lookup

import org.json.JSONObject

object OpenLibrary {
    private const val BASE = "https://openlibrary.org"
    private const val FIELDS = "key,title,author_name,isbn,cover_i"

    suspend fun search(query: String, limit: Int = 5): List<BookMatch> = parseSearch(Http.get("$BASE/search.json?q=${Http.encode(query)}&limit=$limit&fields=$FIELDS"))

    suspend fun byIsbn(isbn: String): List<BookMatch> = parseSearch(Http.get("$BASE/search.json?isbn=$isbn&limit=1&fields=$FIELDS")).map { it.copy(isbn = isbn) }

    suspend fun description(workKey: String): String = parseWork(Http.get("$BASE$workKey.json"))

    fun coverByIsbn(isbn: String) = "https://covers.openlibrary.org/b/isbn/$isbn-L.jpg?default=false"

    fun parseSearch(json: String): List<BookMatch> {
        val docs = JSONObject(json).optJSONArray("docs") ?: return emptyList()
        return (0 until docs.length()).mapNotNull { i ->
            val doc = docs.getJSONObject(i)
            val title = doc.optString("title").trim()
            if (title.isEmpty()) return@mapNotNull null
            val authors = doc.optJSONArray("author_name")
            val author = if (authors == null) "" else (0 until authors.length()).map { authors.getString(it) }.take(2).joinToString(", ")
            val isbns = doc.optJSONArray("isbn")
            val isbn = if (isbns == null) null else (0 until isbns.length()).map { isbns.getString(it) }.let { all -> all.firstOrNull { it.length == 13 } ?: all.firstOrNull() }
            val coverId = doc.optLong("cover_i", 0L)
            val cover = if (coverId > 0) "https://covers.openlibrary.org/b/id/$coverId-L.jpg" else null
            BookMatch(title = title, author = author, description = "", isbn = isbn, coverUrls = listOfNotNull(cover), workKey = doc.optString("key").ifEmpty { null })
        }
    }

    fun parseWork(json: String): String {
        val work = JSONObject(json)
        val raw = work.optJSONObject("description")?.optString("value") ?: work.optString("description")
        return cleanDescription(raw)
    }
}
