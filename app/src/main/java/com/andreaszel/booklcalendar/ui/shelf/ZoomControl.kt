package com.andreaszel.booklcalendar.ui.shelf

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.andreaszel.booklcalendar.ui.theme.Dimens
import com.andreaszel.booklcalendar.ui.theme.Palette
import com.andreaszel.booklcalendar.ui.theme.TextStyles
import com.andreaszel.booklcalendar.ui.theme.comic

@Composable
fun ZoomControl(level: ZoomLevel, onPick: (ZoomLevel) -> Unit) {
    Row(horizontalArrangement = Arrangement.spacedBy(4.dp), modifier = Modifier.comic(Palette.White, radius = Dimens.RadiusPill, shadow = 2.dp).padding(4.dp)) {
        ZoomLevel.entries.forEach { option ->
            val on = option == level
            Box(contentAlignment = Alignment.Center, modifier = Modifier.heightIn(min = 36.dp).clip(RoundedCornerShape(Dimens.Radius)).background(if (on) Palette.Coral else Color.Transparent).border(2.dp, if (on) Palette.Ink else Color.Transparent, RoundedCornerShape(Dimens.Radius)).clickable(role = Role.Tab) { onPick(option) }.semantics { selected = on }.padding(horizontal = 14.dp)) {
                Text(option.name, style = TextStyles.Label.copy(fontSize = 15.sp))
            }
        }
    }
}
