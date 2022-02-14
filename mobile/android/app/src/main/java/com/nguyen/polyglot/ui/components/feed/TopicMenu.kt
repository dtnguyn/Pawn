package com.nguyen.polyglot.ui.components.feed

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
import com.nguyen.polyglot.R
import com.nguyen.polyglot.ui.components.RoundButton
import com.nguyen.polyglot.ui.theme.Blue
import com.nguyen.polyglot.ui.theme.LightGreen
import com.nguyen.polyglot.ui.theme.LightGrey
import com.nguyen.polyglot.ui.theme.Typography

@Composable
fun TopicMenu(topics: String, isLocked: Boolean, onPickTopic: (topic: String) -> Unit, isPicked: (topic: String) -> Boolean, onFinish: () -> Unit, onDismiss: () -> Unit) {


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

                if(isLocked){
                    Image(
                        painter = painterResource(id = R.drawable.ic_lock_red_32),
                        contentDescription = "Lock icon",
                        modifier = Modifier
                            .size(50.dp)
                            .padding(start = 5.dp)
                    )
                } else {
                    Button(onClick = { onFinish() }, colors = ButtonDefaults.buttonColors(Blue), shape = RoundedCornerShape(20.dp)) {
                        Text(
                            text = stringResource(id = R.string.done),
                            style = Typography.body1,
                        )
                    }
                }

            }

        }


        Spacer(modifier = Modifier.padding(10.dp))

        Row(Modifier.fillMaxWidth()) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {
                RoundButton(
                    backgroundColor = if (isPicked("sports")) LightGreen else LightGrey,
                    size = 64.dp,
                    icon = if (isPicked("sports")) R.drawable.sports_picked else R.drawable.sports
                ) {
                    onPickTopic("sports")
                }
                Text(text = stringResource(id = R.string.sport), style = Typography.body1)

                Spacer(modifier = Modifier.padding(10.dp))


                RoundButton(
                    backgroundColor = if (isPicked("tech")) LightGreen else LightGrey,
                    size = 64.dp,
                    icon = if (isPicked("tech")) R.drawable.technology_picked else R.drawable.technology
                ) {
                    onPickTopic("tech")
                }
                Text(text = stringResource(id = R.string.tech), style = Typography.body1)

                Spacer(modifier = Modifier.padding(10.dp))


                RoundButton(
                    backgroundColor = if (isPicked("politics")) LightGreen else LightGrey,
                    size = 64.dp,
                    icon = if (isPicked("politics")) R.drawable.politics_picked else R.drawable.politics
                ) {
                    onPickTopic("politics")
                }
                Text(text = stringResource(id = R.string.politics), style = Typography.body1)
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {
                RoundButton(
                    backgroundColor = if (isPicked("gaming")) LightGreen else LightGrey,
                    size = 64.dp,
                    icon = if (isPicked("gaming")) R.drawable.gaming_picked else R.drawable.gaming
                ) {
                    onPickTopic("gaming")
                }
                Text(text = stringResource(id = R.string.gaming), style = Typography.body1)

                Spacer(modifier = Modifier.padding(10.dp))


                RoundButton(
                    backgroundColor = if (isPicked("beauty")) LightGreen else LightGrey,
                    size = 64.dp,
                    icon = if (isPicked("beauty")) R.drawable.beauty_picked else R.drawable.beauty
                ) {
                    onPickTopic("beauty")
                }
                Text(text = stringResource(id = R.string.beauty), style = Typography.body1)


            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {
                RoundButton(
                    backgroundColor = if (isPicked("business")) LightGreen else LightGrey,
                    size = 64.dp,
                    icon = if (isPicked("business")) R.drawable.business_picked else R.drawable.business
                ) {
                    onPickTopic("business")
                }
                Text(text = stringResource(id = R.string.business), style = Typography.body1)

                Spacer(modifier = Modifier.padding(10.dp))


                RoundButton(
                    backgroundColor = if (isPicked("movie")) LightGreen else LightGrey,
                    size = 64.dp,
                    icon = if (isPicked("movie")) R.drawable.entertainment_picked else R.drawable.entertainment
                ) {
                    onPickTopic("movie")
                }
                Text(text = stringResource(id = R.string.business), style = Typography.body1)

            }
        }
    }
}