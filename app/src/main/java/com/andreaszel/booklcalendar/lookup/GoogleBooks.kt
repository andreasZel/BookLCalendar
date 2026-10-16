package com.andreaszel.booklcalendar.lookup

import org.json.JSONObject

object GoogleBooks {
    private const val BASE = "https://www.googleapis.com/books/v1/volumes"
    private const val PROBE_ISBN = "9780156012195"

    suspend fun search(query: String, key: String?, limit: Int = 5): List<BookMatch> = parse(Http.get(url(query, key, limit)))

    suspend fun check(key: String): Boolean = try {
        Http.get(url("isbn:$PROBE_ISBN", key, 1))
        true
    } catch (e: HttpException) {
        if (e.code in 400..403) false else throw e
    }

    fun parse(json: String): List<BookMatch> {
        val items = JSONObject(json).optJSONArray("items") ?: return emptyList()
        return (0 until items.length()).mapNotNull { i ->
            val info = items.getJSONObject(i).optJSONObject("volumeInfo") ?: return@mapNotNull null
            val title = info.optString("title").trim()
            if (title.isEmpty()) return@mapNotNull null
            val authors = info.optJSONArray("authors")
            val author = if (authors == null) "" else (0 until authors.length()).map { authors.getString(it) }.take(2).joinToString(", ")
            val ids = info.optJSONArray("industryIdentifiers")
            val isbns = if (ids == null) emptyMap() else (0 until ids.length()).map { ids.getJSONObject(it) }.associate { it.optString("type") to it.optString("identifier") }
            val thumbnail = info.optJSONObject("imageLinks")?.optString("thumbnail").orEmpty()
            val cover = thumbnail.replace("http://", "https://").replace("&edge=curl", "")
            BookMatch(title = title, author = author, description = cleanDescription(info.optString("description")), isbn = isbns["ISBN_13"] ?: isbns["ISBN_10"], coverUrls = listOfNotNull(cover.ifEmpty { null }))
        }
    }

    private fun url(query: String, key: String?, limit: Int) = "$BASE?q=${Http.encode(query)}&maxResults=$limit&printType=books" + (key?.let { "&key=${Http.encode(it)}" } ?: "")
}
