package com.andreaszel.booklcalendar.data

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.net.HttpURLConnection
import java.net.URL
import java.util.UUID

class CoverStore(private val context: Context) {
    private val coversDir get() = File(context.filesDir, "covers").apply { mkdirs() }
    private val cameraDir get() = File(context.cacheDir, "camera").apply { mkdirs() }

    fun newCameraUri(): Uri {
        val file = File(cameraDir, "${UUID.randomUUID()}.jpg")
        return FileProvider.getUriForFile(context, "${context.packageName}.covers", file)
    }

    suspend fun import(uri: Uri): String = withContext(Dispatchers.IO) {
        val target = File(coversDir, "${UUID.randomUUID()}.jpg")
        context.contentResolver.openInputStream(uri).use { input ->
            requireNotNull(input) { "Cannot open $uri" }
            target.outputStream().use { input.copyTo(it) }
        }
        cameraDir.listFiles()?.forEach { it.delete() }
        target.absolutePath
    }

    suspend fun download(urls: List<String>): String? = withContext(Dispatchers.IO) {
        for (url in urls) {
            val target = File(coversDir, "${UUID.randomUUID()}.jpg")
            val ok = runCatching {
                val connection = URL(url).openConnection() as HttpURLConnection
                connection.connectTimeout = 10_000
                connection.readTimeout = 15_000
                try {
                    if (connection.responseCode != HttpURLConnection.HTTP_OK) return@runCatching false
                    connection.inputStream.use { input -> target.outputStream().use { input.copyTo(it) } }
                    target.length() > MIN_COVER_BYTES
                } finally {
                    connection.disconnect()
                }
            }.getOrDefault(false)
            if (ok) return@withContext target.absolutePath
            target.delete()
        }
        null
    }

    suspend fun delete(path: String?) = withContext(Dispatchers.IO) {
        if (path != null) File(path).delete()
    }

    private companion object {
        // Open Library and Google return tiny placeholders for missing covers
        const val MIN_COVER_BYTES = 1_000L
    }
}
