package com.nguyen.pawn.ui.components.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.nguyen.pawn.R
import com.nguyen.pawn.ui.theme.Typography

@Composable
fun ChooseLanguagesHeader(onFinish: () -> Unit) {
    Column(Modifier.padding(30.dp)) {
        Text(
            text = "Choose languages that you want to learn",
            style = Typography.h3,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.padding(10.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Your picks:", style = Typography.body1)

            Text(
                text = "Done",
                style = Typography.body1,
                modifier = Modifier
                    .clickable {
                        onFinish()
                    }
            )


        }
        Spacer(modifier = Modifier.padding(5.dp))
        Row {
            Image(
                painter = painterResource(id = R.drawable.english),
                contentDescription = "language icon",
                modifier = Modifier
                    .padding(horizontal = 5.dp)
                    .size(32.dp)

            )
            Image(
                painter = painterResource(id = R.drawable.spain),
                contentDescription = "language icon",
                modifier = Modifier
                    .padding(horizontal = 5.dp)
                    .size(32.dp)
            )
        }
    }
}