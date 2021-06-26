package com.nguyen.pawn.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.nguyen.pawn.R
import com.nguyen.pawn.ui.theme.ReallyRed
import com.nguyen.pawn.ui.theme.Typography

@Composable
fun CustomDialog(title: String, content: String, icon: Int, onDismiss: () -> Unit) {
    Dialog(
        onDismissRequest = { onDismiss() },
    ) {
        Surface(
            shape = RoundedCornerShape(15.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.padding(20.dp)
            ) {
                Image(
                    painter = painterResource(id = icon),
                    contentDescription = "dialog icon",
                    modifier = Modifier
                        .size(100.dp)
                        .padding(vertical = 10.dp)
                )
                Text(
                    text = title,
                    style = Typography.h2,
                    modifier = Modifier.padding(vertical = 10.dp)
                )
                Text(
                    text = content,
                    style = Typography.h6,
                    modifier = Modifier.padding(vertical = 10.dp),
                    textAlign = TextAlign.Center
                )

                Button(
                    onClick = {
                        onDismiss()
                    },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(ReallyRed),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(70.dp)
                        .padding(vertical = 10.dp, horizontal = 30.dp)
                ) {
                    Text(text = "Try Again", style = Typography.h6, color = Color.White)
                }

            }
        }

    }
}