package com.andreaszel.booklcalendar.ui.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.andreaszel.booklcalendar.ui.theme.Palette
import com.andreaszel.booklcalendar.ui.theme.TextStyles
import com.andreaszel.booklcalendar.ui.theme.comic
import com.andreaszel.booklcalendar.ui.theme.focusRing

@Composable
fun ComicTextField(value: String, onChange: (String) -> Unit, placeholder: String, fontSize: TextUnit, modifier: Modifier = Modifier, isError: Boolean = false, singleLine: Boolean = true, minLines: Int = 1, capitalization: KeyboardCapitalization = KeyboardCapitalization.Sentences, keyboardType: KeyboardType = KeyboardType.Text, label: String? = null) {
    val source = remember { MutableInteractionSource() }
    val focused by source.collectIsFocusedAsState()
    val style = (if (singleLine) TextStyles.Body else TextStyles.Body.copy(lineHeight = fontSize * 1.4f)).copy(fontSize = fontSize)
    BasicTextField(value = value, onValueChange = onChange, singleLine = singleLine, minLines = minLines, textStyle = style, cursorBrush = SolidColor(Palette.Ink), interactionSource = source, keyboardOptions = KeyboardOptions(capitalization = capitalization, autoCorrectEnabled = capitalization != KeyboardCapitalization.None && keyboardType == KeyboardType.Text, keyboardType = keyboardType), modifier = modifier.fillMaxWidth().then(if (label == null) Modifier else Modifier.semantics { contentDescription = label }).focusRing(focused).comic(Palette.White, edge = if (isError) Palette.Error else Palette.Ink), decorationBox = { inner ->
        Box(contentAlignment = if (singleLine) Alignment.CenterStart else Alignment.TopStart, modifier = Modifier.heightIn(min = 52.dp).padding(horizontal = 14.dp, vertical = 12.dp)) {
            if (value.isEmpty()) Text(placeholder, style = style.copy(color = Palette.Muted.copy(alpha = 0.7f)))
            inner()
        }
    })
}
