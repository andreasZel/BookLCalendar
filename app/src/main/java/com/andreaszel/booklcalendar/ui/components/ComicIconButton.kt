package com.andreaszel.booklcalendar.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.andreaszel.booklcalendar.ui.theme.Dimens
import com.andreaszel.booklcalendar.ui.theme.Palette

@Composable
fun ComicIconButton(icon: ImageVector, label: String, onClick: () -> Unit, fill: Color = Palette.White, modifier: Modifier = Modifier) {
    Box(contentAlignment = Alignment.Center, modifier = modifier.size(Dimens.Touch).pressable(fill = fill, shadow = 2.dp, role = Role.Button, onClick = onClick).semantics { contentDescription = label }) {
        ComicIcon(icon, size = 22.dp)
    }
}
