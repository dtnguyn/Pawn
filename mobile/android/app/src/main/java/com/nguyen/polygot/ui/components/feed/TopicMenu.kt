package com.nguyen.polygot.ui.components.feed

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nguyen.polygot.R
import com.nguyen.polygot.ui.components.RoundButton
import com.nguyen.polygot.ui.theme.LightGreen
import com.nguyen.polygot.ui.theme.Typography

@Composable
fun TopicMenu() {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(top = 20.dp, start = 20.dp, end = 20.dp, bottom = 70.dp)) {
        Text(text = "Feed Topics", style = Typography.h4)
        Text(text = "You have picked 7 topics", style = Typography.h4)

        Spacer(modifier = Modifier.padding(10.dp))

        Row(Modifier.fillMaxWidth()) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
                RoundButton(backgroundColor = LightGreen, size = 64.dp, icon = R.drawable.sports_picked) {

                }
                Text(text = "Sports", style = Typography.body1)

                Spacer(modifier = Modifier.padding(10.dp))


                RoundButton(backgroundColor = LightGreen, size = 64.dp, icon = R.drawable.technology_picked) {

                }
                Text(text = "Tech", style = Typography.body1)

                Spacer(modifier = Modifier.padding(10.dp))


                RoundButton(backgroundColor = LightGreen, size = 64.dp, icon = R.drawable.politics_picked) {

                }
                Text(text = "Politics", style = Typography.body1)
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
                RoundButton(backgroundColor = LightGreen, size = 64.dp, icon = R.drawable.gaming_picked) {

                }
                Text(text = "Gaming", style = Typography.body1)

                Spacer(modifier = Modifier.padding(10.dp))


                RoundButton(backgroundColor = LightGreen, size = 64.dp, icon = R.drawable.beauty_picked) {

                }
                Text(text = "Beauty", style = Typography.body1)


            }

            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
                RoundButton(backgroundColor = LightGreen, size = 64.dp, icon = R.drawable.business_picked) {

                }
                Text(text = "Business", style = Typography.body1)

                Spacer(modifier = Modifier.padding(10.dp))


                RoundButton(backgroundColor = LightGreen, size = 64.dp, icon = R.drawable.entertainment_picked) {

                }
                Text(text = "Fun", style = Typography.body1)

            }
        }
    }
}