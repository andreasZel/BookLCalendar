package com.andreaszel.booklcalendar.ui.theme

import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import com.andreaszel.booklcalendar.R

@OptIn(ExperimentalTextApi::class)
object Fonts {
    val Fredoka = FontFamily(variable(R.font.fredoka, 500), variable(R.font.fredoka, 600), variable(R.font.fredoka, 700))
    val Nunito = FontFamily(variable(R.font.nunito, 600), variable(R.font.nunito, 700), variable(R.font.nunito, 800))

    private fun variable(res: Int, weight: Int) = Font(res, FontWeight(weight), variationSettings = FontVariation.Settings(FontVariation.weight(weight)))
}
