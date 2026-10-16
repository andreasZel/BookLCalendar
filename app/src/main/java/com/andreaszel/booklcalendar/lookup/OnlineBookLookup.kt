package com.andreaszel.booklcalendar.lookup

import com.andreaszel.booklcalendar.settings.ApiKeys
import kotlinx.coroutines.CancellationException

class OnlineBookLookup(private val keys: ApiKeys) : BookLookup {
    override suspend fun byIsbn(isbn: String): List<BookMatch> = firstOf({ GoogleBooks.search("isbn:$isbn", keys.googleBooks.value, 1).map { it.copy(isbn = isbn) } }, { OpenLibrary.byIsbn(isbn) }).take(1).map(::withCovers)

    override suspend fun search(query: String): List<BookMatch> = firstOf({ GoogleBooks.search(query, keys.googleBooks.value) }, { OpenLibrary.search(query) }).distinctBy { "${it.title.lowercase()}|${it.author.lowercase()}" }.take(MAX_MATCHES).map(::withCovers)

    override suspend fun complete(match: BookMatch): BookMatch {
        if (match.description.isNotBlank()) return match
        val fromWork = match.workKey?.let { key -> runCatching { OpenLibrary.description(key) }.getOrNull() }.orEmpty()
        val text = fromWork.ifBlank { Wikipedia.summary(match.title, match.author).orEmpty() }
        return match.copy(description = text)
    }

    private fun withCovers(match: BookMatch) = if (match.isbn == null) match else match.copy(coverUrls = (listOf(OpenLibrary.coverByIsbn(match.isbn)) + match.coverUrls).distinct())

    // Tries each source in order, fails only when none could be reached
    private suspend fun firstOf(vararg sources: suspend () -> List<BookMatch>): List<BookMatch> {
        var failure: Exception? = null
        var reached = false
        for (source in sources) {
            val result = try {
                source()
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                failure = e
                continue
            }
            reached = true
            if (result.isNotEmpty()) return result
        }
        if (!reached && failure != null) throw failure
        return emptyList()
    }

    private companion object {
        const val MAX_MATCHES = 4
    }
}
