package com.andreaszel.booklcalendar.ui.add

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.andreaszel.booklcalendar.lookup.BookMatch
import com.andreaszel.booklcalendar.ui.components.BookCover
import com.andreaszel.booklcalendar.ui.components.ComicIcon
import com.andreaszel.booklcalendar.ui.components.pressable
import com.andreaszel.booklcalendar.ui.icons.ComicIcons
import com.andreaszel.booklcalendar.ui.theme.Dimens
import com.andreaszel.booklcalendar.ui.theme.Palette
import com.andreaszel.booklcalendar.ui.theme.TextStyles

@Composable
fun MatchRow(match: BookMatch, picked: Boolean, onClick: () -> Unit) {
    Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth().heightIn(min = Dimens.Touch).pressable(fill = if (picked) Palette.Sky else Palette.White, shadow = 2.dp, role = Role.RadioButton, onClick = onClick).semantics { selected = picked }.padding(8.dp)) {
        Box(modifier = Modifier.size(width = 30.dp, height = 40.dp).clip(RoundedCornerShape(4.dp))) {
            BookCover(title = match.title, coverPath = null, initialSize = 16.sp)
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(match.title, maxLines = 2, overflow = TextOverflow.Ellipsis, style = TextStyles.Strong.copy(fontSize = 15.sp))
            if (match.author.isNotBlank()) {
                Text(match.author, maxLines = 1, overflow = TextOverflow.Ellipsis, style = TextStyles.Body.copy(fontSize = 13.sp, color = Palette.Muted))
            }
        }
        if (picked) ComicIcon(ComicIcons.Check, size = 20.dp)
    }
}
