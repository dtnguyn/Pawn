package com.moderndev.polyglot.ui.components.feed

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.moderndev.polyglot.ui.theme.LightGreen
import com.moderndev.polyglot.ui.theme.LightGrey
import com.moderndev.polyglot.ui.theme.Typography

@Composable
fun TopicItem(text: String, icon: Int, iconPicked: Int, isPicked: Boolean, onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(15.dp),
        backgroundColor = if (isPicked) LightGreen else LightGrey,
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .clip(RoundedCornerShape(15.dp))
            .clickable {
                onClick()
            }
    ) {
        Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                Card(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape),
                    backgroundColor = if (isPicked) LightGreen else LightGrey,
                ) {
                    Image(
                        painter = painterResource(if (isPicked) iconPicked else icon),
                        contentDescription = "icon",
                        modifier = Modifier.padding(7.dp)
                    )

                }
                Spacer(modifier = Modifier.padding(5.dp))
                Text(text = text, style = Typography.body1, modifier = Modifier.align(
                    Alignment.CenterVertically
                ))
            }
        }
    }
}