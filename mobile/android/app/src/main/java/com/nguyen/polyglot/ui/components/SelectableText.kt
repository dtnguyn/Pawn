package com.nguyen.polyglot.ui.components

import android.util.Log
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.material.Text
import androidx.compose.runtime.*
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
//    textRange: MutableState<TextRange?>,
    isFocusing: Boolean,
    onLongClick: (word: String) -> Unit
) {
    val layoutResult = remember { mutableStateOf<TextLayoutResult?>(null) }
    var layoutResultNonState: TextLayoutResult? = null
    val textRange = remember { mutableStateOf<TextRange?>(null) }
//    val longPressIndicator = Modifier

    LaunchedEffect(text) {
        textRange.value = null
//        layoutResult.value = null
    }

    Text(
        text = buildAnnotatedString {
            val range = textRange.value
            if (range != null && isFocusing) {
                append(text.substring(0, range.start))
                withStyle(style = SpanStyle(background = Grey)) {
                    append(text.substring(textRange.value!!.start, range.end))
                }
                if (range.end < text.length) {
                    append(text.substring(range.end, text.length))
                }

            } else {
                append(text)
            }

        },
        style = Typography.body1,
        fontSize = 18.sp,
        modifier = Modifier.pointerInput(onLongClick) {
            detectTapGestures(
                onLongPress = { pos ->
                    layoutResult.value?.let { layoutResult ->

                        val range = layoutResult.getWordBoundary(layoutResult.getOffsetForPosition(pos))

                        val word = layoutResult.layoutInput.text.substring(range.start, range.end)

                        if (word != "") {
                            textRange.value = range
                            onLongClick(word)
                        }

                    }
                }
            )
        },
        onTextLayout = {
            layoutResult.value = it
            layoutResultNonState = it
        }

    )


}