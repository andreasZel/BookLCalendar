package com.andreaszel.booklcalendar.ui.shelf

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.andreaszel.booklcalendar.ui.theme.Palette
import com.andreaszel.booklcalendar.ui.theme.TextStyles

@Composable
fun GroupHeader(label: String, count: Int, fontSize: TextUnit) {
    Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 16.dp, bottom = 4.dp)) {
        Text(label, maxLines = 1, overflow = TextOverflow.Ellipsis, style = TextStyles.Label.copy(fontSize = fontSize), modifier = Modifier.semantics { heading() })
        Box(modifier = Modifier.weight(1f).height(3.dp).background(Palette.Ink, RoundedCornerShape(3.dp)))
        Text(countLabel(count), maxLines = 1, style = TextStyles.Strong.copy(fontSize = 12.sp, color = Palette.Muted))
    }
}
