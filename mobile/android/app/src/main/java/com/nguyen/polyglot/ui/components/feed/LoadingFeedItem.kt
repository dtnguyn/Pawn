package com.nguyen.polyglot.ui.components.feed

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.nguyen.polyglot.R
import com.nguyen.polyglot.ui.theme.Typography
import com.nguyen.polyglot.util.ShimmerAnimation

@Composable
fun LoadingFeedItem() {
    Card(
        shape = RoundedCornerShape(15.dp),
        modifier = Modifier.padding(vertical = 10.dp)
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.news_icon),
                    contentDescription = "news_icon",
                    modifier = Modifier
                        .size(32.dp)
                        .padding(end = 5.dp)
                )
                Text(text = "News", style = Typography.subtitle1)
            }
            Spacer(modifier = Modifier.padding(5.dp))
            ShimmerAnimation(modifier = Modifier
                .fillMaxWidth()
                .height(30.dp), shape = RoundedCornerShape(30.dp))
            Spacer(Modifier.padding(2.dp))
            ShimmerAnimation(modifier = Modifier
                .width(150.dp)
                .height(30.dp), shape = RoundedCornerShape(30.dp))

            Spacer(modifier = Modifier.padding(4.dp))
            ShimmerAnimation(modifier = Modifier
                .width(100.dp)
                .height(20.dp), shape = RoundedCornerShape(30.dp))


            Spacer(modifier = Modifier.padding(5.dp))

            ShimmerAnimation(modifier = Modifier
                .fillMaxWidth()
                .height(200.dp), shape = RoundedCornerShape(30.dp))

            Spacer(modifier = Modifier.padding(5.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = R.drawable.share),
                contentDescription = "share_icon",
                modifier = Modifier
                    .size(32.dp)
                    .padding(end = 5.dp)
            )
            Text(text = "Share", style = Typography.subtitle1)
        }
        }

    }
}