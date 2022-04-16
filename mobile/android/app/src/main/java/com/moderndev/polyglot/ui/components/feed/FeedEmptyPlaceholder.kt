package com.moderndev.polyglot.ui.components.feed

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.moderndev.polyglot.ui.theme.Typography
import com.moderndev.polyglot.R

@Composable
fun FeedEmptyPlaceholder() {

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "Your feed is empty right now",
            style = Typography.h5,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.padding(10.dp))
        Spacer(modifier = Modifier.padding(10.dp))
        Image(
            painter = painterResource(id = R.drawable.list_icon),
            contentDescription = "list_icon",
            modifier = Modifier
                .size(128.dp)
                .padding(end = 5.dp)
        )
        Spacer(modifier = Modifier.padding(10.dp))

        Text(
            text = "Save more words to get more news and videos to your feed",
            style = Typography.body1,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.padding(10.dp))
        Spacer(modifier = Modifier.padding(10.dp))
        Spacer(modifier = Modifier.padding(10.dp))
        Spacer(modifier = Modifier.padding(10.dp))


    }


}