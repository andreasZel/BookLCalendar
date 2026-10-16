package com.andreaszel.booklcalendar.data

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
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

    suspend fun delete(path: String?) = withContext(Dispatchers.IO) {
        if (path != null) File(path).delete()
    }
}
