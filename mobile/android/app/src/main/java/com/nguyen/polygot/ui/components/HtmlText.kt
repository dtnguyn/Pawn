package com.nguyen.polygot.ui.components

import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.TextView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.text.HtmlCompat
import com.nguyen.polygot.R
import com.nguyen.polygot.ui.theme.Typography
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