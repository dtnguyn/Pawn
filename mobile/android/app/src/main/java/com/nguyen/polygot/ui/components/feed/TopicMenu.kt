package com.nguyen.polygot.ui.components.feed

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nguyen.polygot.R
import com.nguyen.polygot.ui.components.RoundButton
import com.nguyen.polygot.ui.theme.Blue
import com.nguyen.polygot.ui.theme.LightGreen
import com.nguyen.polygot.ui.theme.LightGrey
import com.nguyen.polygot.ui.theme.Typography

@Composable
fun TopicMenu(topics: String, onPickTopic: (topic: String) -> Unit, isPicked: (topic: String) -> Boolean, onFinish: () -> Unit, onDismiss: () -> Unit) {


    var topicList by remember { mutableStateOf(topics.split(",").map { it.trim() }) }
    val currentOnBack by rememberUpdatedState { }



    Column(
        Modifier
            .fillMaxWidth()
            .padding(top = 20.dp, start = 20.dp, end = 20.dp, bottom = 70.dp)
    ) {

        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(2f)) {
                Text(text = "Feed Topics", style = Typography.h6)
                Text(text = "Filter your feed to specific topics!", style = Typography.body1)
            }
            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.CenterEnd) {
                Button(onClick = { onFinish() }, colors = ButtonDefaults.buttonColors(Blue), shape = RoundedCornerShape(20.dp)) {
                    Text(
                        text = "Done",
                        style = Typography.body1,
                    )
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
                Text(text = "Sports", style = Typography.body1)

                Spacer(modifier = Modifier.padding(10.dp))


                RoundButton(
                    backgroundColor = if (isPicked("tech")) LightGreen else LightGrey,
                    size = 64.dp,
                    icon = if (isPicked("tech")) R.drawable.technology_picked else R.drawable.technology
                ) {
                    onPickTopic("tech")
                }
                Text(text = "Tech", style = Typography.body1)

                Spacer(modifier = Modifier.padding(10.dp))


                RoundButton(
                    backgroundColor = if (isPicked("politics")) LightGreen else LightGrey,
                    size = 64.dp,
                    icon = if (isPicked("politics")) R.drawable.politics_picked else R.drawable.politics
                ) {
                    onPickTopic("politics")
                }
                Text(text = "Politics", style = Typography.body1)
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
                Text(text = "Gaming", style = Typography.body1)

                Spacer(modifier = Modifier.padding(10.dp))


                RoundButton(
                    backgroundColor = if (isPicked("beauty")) LightGreen else LightGrey,
                    size = 64.dp,
                    icon = if (isPicked("beauty")) R.drawable.beauty_picked else R.drawable.beauty
                ) {
                    onPickTopic("beauty")
                }
                Text(text = "Beauty", style = Typography.body1)


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
                Text(text = "Business", style = Typography.body1)

                Spacer(modifier = Modifier.padding(10.dp))


                RoundButton(
                    backgroundColor = if (isPicked("movie")) LightGreen else LightGrey,
                    size = 64.dp,
                    icon = if (isPicked("movie")) R.drawable.entertainment_picked else R.drawable.entertainment
                ) {
                    onPickTopic("movie")
                }
                Text(text = "Movie", style = Typography.body1)

            }
        }
    }
}