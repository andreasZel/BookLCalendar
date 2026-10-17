package com.andreaszel.booklcalendar.ui.log

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import androidx.activity.result.ActivityResult

object SpeechInput {
    fun available(context: Context): Boolean = SpeechRecognizer.isRecognitionAvailable(context)

    fun intent(tag: String): Intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM).putExtra(RecognizerIntent.EXTRA_LANGUAGE, tag).putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, tag).putExtra(RecognizerIntent.EXTRA_PROMPT, "Say your note")

    fun text(result: ActivityResult): String? = if (result.resultCode == Activity.RESULT_OK) result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)?.firstOrNull() else null
}
