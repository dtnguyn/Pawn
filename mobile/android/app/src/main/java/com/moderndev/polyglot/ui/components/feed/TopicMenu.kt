package com.moderndev.polyglot.ui.components.feed

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.moderndev.polyglot.R
import com.moderndev.polyglot.ui.theme.Blue
import com.moderndev.polyglot.ui.theme.Typography

@Composable
fun TopicMenu(
    topics: String,
    isLocked: Boolean,
    onPickTopic: (topic: String) -> Unit,
    isPicked: (topic: String) -> Boolean,
    onFinish: () -> Unit,
    onDismiss: () -> Unit
) {


    var topicList by remember { mutableStateOf(topics.split(",").map { it.trim() }) }
    val currentOnBack by rememberUpdatedState { }



    Column(
        Modifier
            .fillMaxWidth()
            .padding(top = 20.dp, start = 20.dp, end = 20.dp, bottom = 70.dp)
    ) {

        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(2f)) {
                Row {
                    Text(text = stringResource(id = R.string.feed_topics), style = Typography.h6)
                }
                Text(text = stringResource(id = R.string.feed_topics_sub), style = Typography.body1)
            }
            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.CenterEnd) {

                if (isLocked) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_lock_red_32),
                        contentDescription = "Lock icon",
                        modifier = Modifier
                            .size(50.dp)
                            .padding(start = 5.dp)
                    )
                } else {
                    Button(
                        onClick = { onFinish() },
                        colors = ButtonDefaults.buttonColors(Blue),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Text(
                            text = stringResource(id = R.string.done),
                            style = Typography.body1,
                        )
                    }
                }

            }

        }


        Spacer(modifier = Modifier.padding(10.dp))

        TopicItem(
            text = stringResource(id = R.string.sport),
            icon = R.drawable.sports,
            iconPicked = R.drawable.sports_picked,
            isPicked = isPicked("sport")
        ) {
            onPickTopic("sport")
        }

        Spacer(modifier = Modifier.padding(10.dp))

        TopicItem(
            text = stringResource(id = R.string.tech),
            icon = R.drawable.technology,
            iconPicked = R.drawable.technology_picked,
            isPicked = isPicked("tech")
        ) {
            onPickTopic("tech")
        }

        Spacer(modifier = Modifier.padding(10.dp))

        TopicItem(
            text = stringResource(id = R.string.politics),
            icon = R.drawable.politics,
            iconPicked = R.drawable.politics_picked,
            isPicked = isPicked("politics")
        ) {
            onPickTopic("politics")
        }

        Spacer(modifier = Modifier.padding(10.dp))

        TopicItem(
            text = stringResource(id = R.string.gaming),
            icon = R.drawable.gaming,
            iconPicked = R.drawable.gaming_picked,
            isPicked = isPicked("gaming")
        ) {
            onPickTopic("gaming")
        }

        Spacer(modifier = Modifier.padding(10.dp))

        TopicItem(
            text = stringResource(id = R.string.beauty),
            icon = R.drawable.beauty,
            iconPicked = R.drawable.beauty_picked,
            isPicked = isPicked("beauty")
        ) {
            onPickTopic("beauty")
        }

        Spacer(modifier = Modifier.padding(10.dp))

        TopicItem(
            text = stringResource(id = R.string.business),
            icon = R.drawable.business,
            iconPicked = R.drawable.business_picked,
            isPicked = isPicked("business")
        ) {
            onPickTopic("business")
        }

        Spacer(modifier = Modifier.padding(10.dp))

        TopicItem(
            text = stringResource(id = R.string.entertainment),
            icon = R.drawable.entertainment,
            iconPicked = R.drawable.entertainment_picked,
            isPicked = isPicked("entertainment")
        ) {
            onPickTopic("entertainment")
        }

        Spacer(modifier = Modifier.padding(10.dp))
    }
}