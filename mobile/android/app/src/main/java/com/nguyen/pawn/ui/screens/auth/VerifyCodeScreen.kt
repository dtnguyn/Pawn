package com.nguyen.pawn.ui.screens.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.nguyen.pawn.R
import com.nguyen.pawn.ui.components.RoundedSquareButton
import com.nguyen.pawn.ui.theme.Blue
import com.nguyen.pawn.ui.theme.LightGrey
import com.nguyen.pawn.ui.theme.ReallyRed
import com.nguyen.pawn.ui.theme.Typography

@Composable
fun VerifyCodeScreen(navController: NavController) {


    var code by remember { mutableStateOf("") }


    Scaffold(backgroundColor = Color.White) {
        IconButton(onClick = { navController.popBackStack()  }, modifier = Modifier.padding(20.dp)) {
            Image(
                painter = painterResource(id = R.drawable.back_32_black),
                contentDescription = "Back icon"
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            RoundedSquareButton(backgroundColor = LightGrey, icon = R.drawable.email, size = 100.dp) {

            }

            Text(
                text = "Check your email",
                style = Typography.h2,
                color = Color.Black,
                modifier = Modifier.padding(vertical = 10.dp)
            )

            Text(
                text = "We have sent you a verification code. Open it and enter the code below.",
                style = Typography.body1,
                color = Color.Black,
                modifier = Modifier.padding(top = 5.dp, bottom = 20.dp)
            )


            TextField(
                value = code,
                onValueChange = { newValue -> code = newValue },
                label = {
                    Text(text = "Enter the verification code", style = Typography.body2, color = Color.Gray)
                },
                singleLine = true,
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 10.dp)
                    .aspectRatio(5f)
            )


            Button(
                onClick = { navController.navigate("verify") },
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(ReallyRed),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 20.dp)
                    .aspectRatio(5f)
            ) {
                Text(text = "Submit", style = Typography.h6, color = Color.White)
            }

        }

    }
}