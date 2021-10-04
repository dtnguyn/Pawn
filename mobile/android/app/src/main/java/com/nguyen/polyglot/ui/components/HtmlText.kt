package com.nguyen.polyglot.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.nguyen.polyglot.R
import dev.jeziellago.compose.markdowntext.MarkdownText

@Composable
fun HtmlText(html: String, modifier: Modifier = Modifier) {
    MarkdownText(
        markdown = html,
        textAlign = TextAlign.Justify,
        fontSize = 20.sp,
        fontResource = R.font.mmedium
    )

}