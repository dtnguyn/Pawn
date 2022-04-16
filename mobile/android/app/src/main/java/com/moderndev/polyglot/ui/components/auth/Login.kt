package com.moderndev.polyglot.ui.components.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.moderndev.polyglot.R
import com.moderndev.polyglot.ui.theme.LightGrey
import com.moderndev.polyglot.ui.theme.ReallyRed
import com.moderndev.polyglot.ui.theme.Typography

@Composable
fun Login(navController: NavController, onLogin: (emailOrUsername: String, password: String) -> Unit) {

    var emailOrUsername by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }


    Column {
        TextField(
            value = emailOrUsername,
            onValueChange = { newValue -> emailOrUsername = newValue },
            label = {
                Text(text = stringResource(id = R.string.email_or_username_placeholder), style = Typography.body2, color = Color.Gray)
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
            value = password,
            onValueChange = { newValue -> password = newValue },
            visualTransformation = PasswordVisualTransformation(),
            label = {
                Text(text = stringResource(id = R.string.password_placeholder), style = Typography.body2, color = Color.Gray)
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
        Text(
            text = stringResource(id = R.string.forgot_password),
            style = Typography.body2,
            color = Color.Black,
            modifier = Modifier
                .padding(top = 5.dp, end = 40.dp, start = 40.dp, bottom = 10.dp)
                .clip(
                    RoundedCornerShape(5.dp)
                )
                .clickable {
                    navController.navigate("password")
                }
        )
        Button(
            onClick = {
                onLogin(emailOrUsername, password)
            },
            shape = RoundedCornerShape(20.dp),
            colors = ButtonDefaults.buttonColors(ReallyRed),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp, vertical = 10.dp)
                .aspectRatio(5f)
        ) {
            Text(text = stringResource(id = R.string.login), style = Typography.h6, color = Color.White)
        }
        Text(
            text = stringResource(id = R.string.or),
            style = Typography.body2,
            color = Color.Black,
            modifier = Modifier.padding(top = 10.dp, end = 40.dp, start = 40.dp, bottom = 5.dp)
        )
        Button(
            onClick = { /*TODO*/ },
            shape = RoundedCornerShape(20.dp),
            colors = ButtonDefaults.buttonColors(LightGrey),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp, vertical = 5.dp)
                .aspectRatio(5f)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Image(
                    painter = painterResource(id = R.drawable.google),
                    contentDescription = "google icon",
                    modifier = Modifier.size(64.dp)
                )
                Text(
                    text = stringResource(id = R.string.log_in_with_Google),
                    style = Typography.body1,
                    color = Color.Black,
                    modifier = Modifier.padding(start = 20.dp)
                )
            }
        }
    }

}