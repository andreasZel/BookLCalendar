package com.andreaszel.booklcalendar.lookup

import android.content.Context
import android.net.Uri
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import java.io.File

class CoverReader(private val context: Context) {
    suspend fun read(path: String): List<CoverLine> {
        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
        try {
            val image = InputImage.fromFilePath(context, Uri.fromFile(File(path)))
            val text = recognizer.process(image).awaitOrNull() ?: return emptyList()
            return text.textBlocks.flatMap { it.lines }.map { CoverLine(it.text, it.boundingBox?.height() ?: 0) }
        } finally {
            recognizer.close()
        }
    }
}
