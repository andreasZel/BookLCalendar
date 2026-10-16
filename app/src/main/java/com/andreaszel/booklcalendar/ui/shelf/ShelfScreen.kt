package com.andreaszel.booklcalendar.ui.shelf

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.andreaszel.booklcalendar.ui.components.ComicButton
import com.andreaszel.booklcalendar.ui.theme.Palette

@Composable
fun ShelfScreen(onAdd: () -> Unit, onOpen: (Long) -> Unit) {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
        ComicButton(text = "Add book", onClick = onAdd, fill = Palette.Coral)
    }
}
