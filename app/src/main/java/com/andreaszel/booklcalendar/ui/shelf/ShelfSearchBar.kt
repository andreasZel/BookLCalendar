package com.andreaszel.booklcalendar.ui.shelf

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.andreaszel.booklcalendar.ui.components.ComicIconButton
import com.andreaszel.booklcalendar.ui.components.ComicTextField
import com.andreaszel.booklcalendar.ui.icons.ComicIcons

@Composable
fun ShelfSearchBar(query: String, onChange: (String) -> Unit, onClose: () -> Unit) {
    val focus = remember { FocusRequester() }
    LaunchedEffect(Unit) { focus.requestFocus() }

    Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 8.dp)) {
        ComicTextField(value = query, onChange = onChange, placeholder = "Search title, author or description", fontSize = 16.sp, modifier = Modifier.weight(1f).focusRequester(focus))
        ComicIconButton(icon = ComicIcons.Close, label = "Close search", onClick = onClose)
    }
}
