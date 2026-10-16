package com.andreaszel.booklcalendar.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.andreaszel.booklcalendar.ui.theme.Dimens
import com.andreaszel.booklcalendar.ui.theme.Palette
import com.andreaszel.booklcalendar.ui.theme.TextStyles

@Composable
fun ComicChip(label: String, selected: Boolean, onClick: () -> Unit) {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.heightIn(min = Dimens.Touch).clip(CircleShape).background(if (selected) Palette.Sky else Palette.White).border(Dimens.Outline, Palette.Ink, CircleShape).clickable(role = Role.RadioButton, onClick = onClick).semantics { this.selected = selected }.padding(horizontal = 14.dp)) {
        Text(label, style = TextStyles.Strong.copy(fontSize = 14.sp))
    }
}
