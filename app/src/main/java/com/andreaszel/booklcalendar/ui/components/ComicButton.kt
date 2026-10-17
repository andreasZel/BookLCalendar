package com.andreaszel.booklcalendar.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.andreaszel.booklcalendar.ui.theme.Dimens
import com.andreaszel.booklcalendar.ui.theme.TextStyles

@Composable
fun ComicButton(text: String, onClick: () -> Unit, fill: Color, modifier: Modifier = Modifier, icon: ImageVector? = null, height: Dp = 52.dp, radius: Dp = Dimens.Radius, shadow: Dp = 3.dp, fontSize: TextUnit = 16.sp, padding: PaddingValues = PaddingValues(horizontal = 16.dp), enabled: Boolean = true, gap: Dp = 8.dp) {
    Row(horizontalArrangement = Arrangement.spacedBy(gap, Alignment.CenterHorizontally), verticalAlignment = Alignment.CenterVertically, modifier = modifier.heightIn(min = height).pressable(fill = fill, shadow = shadow, radius = radius, enabled = enabled, role = Role.Button, onClick = onClick).padding(padding)) {
        if (icon != null) ComicIcon(icon, size = 22.dp)
        Text(text, style = TextStyles.Label.copy(fontSize = fontSize), maxLines = 1, overflow = TextOverflow.Ellipsis, modifier = Modifier.weight(1f, fill = false))
    }
}
