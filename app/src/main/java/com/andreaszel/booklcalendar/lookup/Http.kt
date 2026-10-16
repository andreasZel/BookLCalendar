package com.andreaszel.booklcalendar.lookup

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

object Http {
    private const val USER_AGENT = "BookLCalendar/0.1 (Android)"

    suspend fun get(url: String): String = withContext(Dispatchers.IO) {
        val connection = URL(url).openConnection() as HttpURLConnection
        connection.connectTimeout = 10_000
        connection.readTimeout = 15_000
        connection.setRequestProperty("User-Agent", USER_AGENT)
        connection.setRequestProperty("Accept", "application/json")
        try {
            val code = connection.responseCode
            if (code !in 200..299) throw HttpException(code)
            connection.inputStream.bufferedReader().use { it.readText() }
        } finally {
            connection.disconnect()
        }
    }

    fun encode(text: String): String = URLEncoder.encode(text, "UTF-8")
}

class HttpException(val code: Int) : IOException("HTTP $code")
