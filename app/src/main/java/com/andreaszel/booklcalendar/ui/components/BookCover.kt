package com.andreaszel.booklcalendar.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.TextUnit
import coil3.compose.AsyncImage
import com.andreaszel.booklcalendar.ui.theme.Palette
import com.andreaszel.booklcalendar.ui.theme.TextStyles
import com.andreaszel.booklcalendar.ui.theme.dotted
import java.io.File

@Composable
fun BookCover(title: String, coverPath: String?, initialSize: TextUnit, modifier: Modifier = Modifier) {
    if (coverPath != null) {
        AsyncImage(model = File(coverPath), contentDescription = "Cover of $title", contentScale = ContentScale.Crop, modifier = modifier.fillMaxSize().background(Palette.Paper))
        return
    }
    val initial = title.trim().removePrefix("The ").trim().firstOrNull()?.uppercase() ?: "?"
    val pastel = Palette.Pastels[Math.floorMod(title.hashCode(), Palette.Pastels.size)]
    Box(contentAlignment = Alignment.Center, modifier = modifier.fillMaxSize().background(pastel).dotted()) {
        Text(initial, style = TextStyles.Heading.copy(fontSize = initialSize))
    }
}
