package com.nguyen.polyglot.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
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
        Column(Modifier.padding(horizontal = 20.dp)) {
            Spacer(modifier = Modifier.padding(10.dp))
            IconButton(onClick = {
                navController.popBackStack()
            }) {
                Icon(Icons.Filled.ArrowBack, "")
            }
            Spacer(modifier = Modifier.padding(10.dp))
            Image(
                painter = painterResource(id = R.drawable.quiz_icon),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth(70f)
                    .align(Alignment.CenterHorizontally)
            )
            Spacer(Modifier.padding(20.dp))
            Text(text = stringResource(id = R.string.review_title), style = Typography.h3)
            Spacer(Modifier.padding(5.dp))
            Text(
                text = stringResource(id = R.string.review_subtitle),
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
                        text = stringResource(id = R.string.quick_review),
                        color = Color.White,
                        style = Typography.h5,
                    )
                    Text(
                        text = stringResource(id = R.string.quick_review_subtitle),
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
                        text = stringResource(id = R.string.full_review),
                        color = Color.White,
                        style = Typography.h5,
                    )
                    Text(
                        text = stringResource(id = R.string.login_title),
                        color = Color.White,
                        style = Typography.body1,
                    )
                }

            }


        }
    }


}