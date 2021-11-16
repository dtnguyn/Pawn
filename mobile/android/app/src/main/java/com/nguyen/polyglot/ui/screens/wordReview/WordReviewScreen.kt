package com.nguyen.polyglot.ui.screens.wordReview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.nguyen.polyglot.ui.theme.*

@Composable
fun WordReviewScreen(navController: NavController) {

    Scaffold(backgroundColor = Color.White) {
        Column(Modifier.padding(20.dp)) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                shape = RoundedCornerShape(20.dp),
                backgroundColor = LightGrey,
                elevation = 4.dp,

                ) {
                Row(
                    Modifier
                        .padding(10.dp)
                        .align(Alignment.CenterHorizontally)) {
                    Box(
                        Modifier
                            .fillMaxWidth(0.8f)
                            .height(10.dp)
                            .background(Grey, RoundedCornerShape(10.dp))
                            .align(CenterVertically)
                    ) {
                        Box(
                            Modifier
                                .fillMaxWidth(0.95f)
                                .height(10.dp)
                                .background(LightRed, RoundedCornerShape(10.dp))
                        )
                        Box(
                            Modifier
                                .fillMaxWidth(0.8f)
                                .height(10.dp)
                                .background(LightGreen, RoundedCornerShape(10.dp))
                        )

                    }
                    Spacer(Modifier.padding(5.dp))
                    Text(text = "0/10", style = Typography.h6)

                }
            }
            Spacer(modifier = Modifier.padding(20.dp))
            Text(text = "This is just a dummy question to test out the UI of the app?", style = Typography.h3)
            Spacer(modifier = Modifier.padding(20.dp))
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(5f),
                colors = ButtonDefaults.buttonColors(LightGrey),
                shape = RoundedCornerShape(15.dp),
                onClick = {  }) {
                Text(
                    text = "Answer 1",
                    style = Typography.h6,
                )
            }
            Spacer(modifier = Modifier.padding(5.dp))
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(5f),
                colors = ButtonDefaults.buttonColors(LightGrey),
                shape = RoundedCornerShape(15.dp),
                onClick = {  }) {
                Text(
                    text = "Answer 2",
                    style = Typography.h6,
                )
            }
            Spacer(modifier = Modifier.padding(5.dp))
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(5f),
                colors = ButtonDefaults.buttonColors(LightGrey),
                shape = RoundedCornerShape(15.dp),
                onClick = {  }) {
                Text(
                    text = "Answer 3",
                    style = Typography.h6,
                )
            }
            Spacer(modifier = Modifier.padding(10.dp))
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(5f),
                colors = ButtonDefaults.buttonColors(LightGrey),
                shape = RoundedCornerShape(15.dp),
                onClick = {  }) {

                Text(
                    text = "Answer 4",
                    style = Typography.h6,
                )
            }
            Spacer(modifier = Modifier.padding(10.dp))
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(5f),
                colors = ButtonDefaults.buttonColors(LightOrange),
                shape = RoundedCornerShape(15.dp),
                onClick = {  }) {

                Text(
                    text = "Check Answer",
                    style = Typography.h5,
                )
            }
        }

    }


}