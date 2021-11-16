package com.nguyen.polyglot.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.nguyen.polyglot.R
import com.nguyen.polyglot.ui.navigation.PolyglotScreens
import com.nguyen.polyglot.ui.theme.Blue
import com.nguyen.polyglot.ui.theme.ReallyRed
import com.nguyen.polyglot.ui.theme.Typography

@Composable
fun WordReviewMenuScreen(navController: NavController) {

    Scaffold(backgroundColor = Color.White) {
        Column(Modifier.padding(20.dp)) {

            Image(
                painter = painterResource(id = R.drawable.quiz_icon),
                contentDescription = null,
                modifier = Modifier.fillMaxWidth(70f).align(Alignment.CenterHorizontally)
            )
            Spacer(Modifier.padding(20.dp))
            Text(text = "Review your vocabulary", style = Typography.h3)
            Spacer(Modifier.padding(5.dp))
            Text(
                text = "Feel confident about your vocabulary? Take a quick or full review of all your saved words!",
                style = Typography.body1
            )
            Spacer(Modifier.padding(20.dp))
            Button(
                modifier = Modifier
                    .fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(ReallyRed),
                shape = RoundedCornerShape(15.dp),
                onClick = { navController.navigate("wordReview") }) {
                Column {
                    Text(
                        text = "Quick Review",
                        color = Color.White,
                        style = Typography.h5,
                    )
                    Text(
                        text = "Take a short quiz (10 questions or less) to review your vocabulary",
                        color = Color.White,
                        style = Typography.body1,
                    )
                }

            }
            Spacer(Modifier.padding(10.dp))
            Button(
                modifier = Modifier
                    .fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(Blue),
                shape = RoundedCornerShape(15.dp),
                onClick = { navController.navigate("wordReview") }) {
                Column {
                    Text(
                        text = "Full Review",
                        color = Color.White,
                        style = Typography.h5,
                    )
                    Text(
                        text = "Take a full review of all your saved words. You will feel more confident in your vocabulary",
                        color = Color.White,
                        style = Typography.body1,
                    )
                }

            }


        }
    }


}