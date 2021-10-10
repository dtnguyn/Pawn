package com.nguyen.polyglot.ui.components

import android.util.Log
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.*
import androidx.compose.ui.unit.sp
import com.nguyen.polyglot.ui.theme.Grey
import com.nguyen.polyglot.ui.theme.Typography

@Composable
fun SelectableText(
    text: String,
    textRange: MutableState<TextRange?>,
    isFocusing: Boolean,
    onLongClick: (word: String) -> Unit
){
    val layoutResult = remember { mutableStateOf<TextLayoutResult?>(null) }
    val longPressIndicator = Modifier.pointerInput(onLongClick) {
        detectTapGestures(
            onLongPress = { pos ->
                layoutResult.value?.let { layoutResult ->
                    val range = layoutResult.getWordBoundary(layoutResult.getOffsetForPosition(pos))

                    val word = text.substring(range.start, range.end)
                    if(word != ""){
                        textRange.value = range
                        onLongClick(word)
                    }

                }
            }
        )
    }

    Text(
        text = buildAnnotatedString {
            val range = textRange.value
            if(range != null && isFocusing){
                append(text.substring(0, range.start))
                withStyle(style = SpanStyle(background = Grey)) {
                    append(text.substring(textRange.value!!.start, range.end))
                }
                if(range.end < text.length){
                    append(text.substring(range.end, text.length))
                }

            } else {
                append(text)
            }

        },
        style = Typography.body1,
        fontSize = 18.sp,
        modifier = Modifier.then(longPressIndicator),
        onTextLayout = {
            layoutResult.value = it
        }
    )


}