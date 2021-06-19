package com.nguyen.pawn.ui.components.auth

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.nguyen.pawn.ui.theme.ReallyRed
import com.nguyen.pawn.ui.theme.TextFieldGrey
import com.nguyen.pawn.ui.theme.Typography

@ExperimentalMaterialApi
@Composable
fun Register(nativeLanguage: String, onClickNativeLanguage: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var passwordVerify by remember { mutableStateOf("") }


    Column() {
        TextField(
            value = username,
            onValueChange = { newValue -> username = newValue },
            label = {
                Text(text = "Enter your username", style = Typography.body2, color = Color.Gray)
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
                .padding(top = 30.dp, start = 30.dp, end = 30.dp, bottom = 10.dp)
                .aspectRatio(5f)
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
                .padding(horizontal = 30.dp, vertical = 10.dp)
                .aspectRatio(5f)

        )

        Card(
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp, vertical = 10.dp)
                .aspectRatio(5f)
                .clip(RoundedCornerShape(20.dp))
                .clickable { onClickNativeLanguage() },
            backgroundColor = TextFieldGrey
        ) {
            Box(
                Modifier
                    .fillMaxHeight()
                    .fillMaxWidth()
                    .padding(15.dp), contentAlignment = Alignment.CenterStart
            ) {
                if (nativeLanguage.isBlank()) Text(
                    text = "Choose your native language",
                    style = Typography.body2,
                    color = Color.Gray
                )
                else Text(
                    text = nativeLanguage,
                    style = Typography.body1,
                    color = Color.Black
                )
            }

        }

        TextField(
            value = password,
            onValueChange = { newValue -> password = newValue },
            visualTransformation = PasswordVisualTransformation(),
            label = {
                Text(text = "Enter your password", style = Typography.body2, color = Color.Gray)
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
                .padding(horizontal = 30.dp, vertical = 10.dp)
                .aspectRatio(5f)
        )
        TextField(
            value = passwordVerify,
            onValueChange = { newValue -> passwordVerify = newValue },
            visualTransformation = PasswordVisualTransformation(),
            label = {
                Text(
                    text = "Enter your password again",
                    style = Typography.body2,
                    color = Color.Gray
                )
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
                .padding(horizontal = 30.dp, vertical = 10.dp)
                .aspectRatio(5f)
        )
        Button(
            onClick = { /*TODO*/ },
            shape = RoundedCornerShape(20.dp),
            colors = ButtonDefaults.buttonColors(ReallyRed),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp, vertical = 20.dp)
                .aspectRatio(5f)
        ) {
            Text(text = "Register", style = Typography.h6, color = Color.White)
        }

    }


}