package com.nguyen.polygot.ui.components.feed

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.nguyen.polygot.R
import com.nguyen.polygot.ui.theme.Typography
import com.skydoves.landscapist.CircularReveal
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun FeedItem() {
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
            Text(text = "This is just a test title for the news, nothing is special about this text. Please ignore please!", style = Typography.h5)
            Text(text = "This is the upload time", style = Typography.subtitle2)
            
            Spacer(modifier = Modifier.padding(5.dp))
            
            GlideImage(
                imageModel = "https://img.huffingtonpost.com/asset/614105e226000087ed540ced.jpeg?cache=brjsyv3qis&ops=1778_1000%22",
                // Crop, Fit, Inside, FillHeight, FillWidth, None
                contentScale = ContentScale.Fit,
                // shows an image with a circular revealed animation.
                circularReveal = CircularReveal(duration = 250),
                // shows a placeholder ImageBitmap when loading.
                placeHolder = ImageBitmap.imageResource(R.drawable.fire),
                // shows an error ImageBitmap when the request failed.
                error = ImageBitmap.imageResource(R.drawable.error),
                modifier = Modifier.clip(RoundedCornerShape(15.dp))
            )

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