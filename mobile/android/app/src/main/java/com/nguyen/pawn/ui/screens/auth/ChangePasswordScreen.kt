package com.nguyen.pawn.ui.screens.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.nguyen.pawn.R
import com.nguyen.pawn.ui.theme.ReallyRed
import com.nguyen.pawn.ui.theme.Typography


@Composable
fun ChangePasswordScreen(navController: NavController) {

    var email by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var newPasswordVerify by remember { mutableStateOf("") }

    Scaffold(backgroundColor = Color.White) {
        Column(Modifier.padding(20.dp)) {
            IconButton(onClick = { navController.popBackStack()  }, modifier = Modifier.padding(bottom = 20.dp)) {
                Image(
                    painter = painterResource(id = R.drawable.back_32_black),
                    contentDescription = "Back icon"
                )
            }

            Text(
                text = "Reset password",
                style = Typography.h2,
                color = Color.Black,
                modifier = Modifier.padding(vertical = 10.dp)
            )
            Text(
                text = "Enter your current email and a new password below, then we will send you an email to verify this change.",
                style = Typography.body1,
                color = Color.Black,
                modifier = Modifier.padding(top = 5.dp, bottom = 20.dp)
            )

            TextField(
                value = email,
                onValueChange = { newValue -> email = newValue },
                label = {
                    Text(text = "Enter your email", style = Typography.body2, color = Color.Gray)
                },
                singleLine = true,
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                ),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 10.dp)
                    .aspectRatio(5f)
            )
            TextField(
                value = newPassword,
                onValueChange = { newValue -> newPassword = newValue },
                visualTransformation = PasswordVisualTransformation(),
                label = {
                    Text(text = "Enter your new password", style = Typography.body2, color = Color.Gray)
                },
                singleLine = true,
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                ),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 10.dp)
                    .aspectRatio(5f)
            )
            TextField(
                value = newPasswordVerify,
                onValueChange = { newValue -> newPasswordVerify = newValue },
                visualTransformation = PasswordVisualTransformation(),
                label = {
                    Text(text = "Enter your new password again", style = Typography.body2, color = Color.Gray)
                },
                singleLine = true,
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                ),
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