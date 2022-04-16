package com.moderndev.polyglot.ui.components.account

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.moderndev.polyglot.R
import com.moderndev.polyglot.ui.theme.ReallyRed
import com.moderndev.polyglot.ui.theme.Typography
import androidx.compose.runtime.*

@Composable
fun UpdateDialog(title: String, placeholder: String, content: String, currentValue: String, actionButtonText: String, onAction: (value: String) -> Unit, onDismiss: () -> Unit) {

    var value by remember { mutableStateOf(currentValue) }


    Dialog(
        onDismissRequest = { onDismiss() },
    ) {
        Surface(
            shape = RoundedCornerShape(15.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Box(Modifier.fillMaxWidth()) {
                    IconButton(
                        onClick = { onDismiss() },
                        content = {
                            Image(painter = painterResource(id = R.drawable.close), contentDescription = "Close button")
                        },
                        modifier = Modifier
                            .size(30.dp)
                            .align(Alignment.CenterStart)
                    )
                    Text(
                        text = title,
                        style = Typography.h5,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                
                Spacer(modifier = Modifier.padding(10.dp))

                Text(
                    text = content,
                    style = Typography.body1,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(start = 5.dp)
                )
                Spacer(modifier = Modifier.padding(5.dp))

                TextField(
                    value = value,
                    onValueChange = { newValue -> value = newValue },
                    placeholder = {
                        Text(text = placeholder, style = Typography.body2, color = Color.Gray)
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
                )

                Spacer(modifier = Modifier.padding(10.dp))

                Button(
                    onClick = {
                        onAction(value)
                    },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(ReallyRed),
                    enabled = value.isNotEmpty(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(5f)

                ) {
                    Text(text = actionButtonText, style = Typography.h6, color = Color.White)
                }

            }
        }

    }
}