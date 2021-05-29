package com.nguyen.pawn.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.nguyen.pawn.R
import com.nguyen.pawn.ui.theme.ReallyRed
import com.nguyen.pawn.ui.theme.Typography

@Composable
fun DailyWordCard(word: String, definition: String, pronunciation: String) {
    Card(
        shape = RoundedCornerShape(60.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
            .padding(10.dp),
        backgroundColor = ReallyRed
    ){
        Column(Modifier.padding(25.dp)) {
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Column {
                    Text(text = word, style = Typography.h1, color = Color.White)
                    Text(text = pronunciation, style = Typography.body2, color = Color.White)
                }
                Card(
                    Modifier
                        .clip(CircleShape)
                        .align(Alignment.CenterVertically)
                        .clickable(onClick = {}),
                ) {
                    Box(
                        modifier = Modifier
                            .size(45.dp)
                            .background(Color.White)

                    ) {
                        Image(
                            painter = painterResource(R.drawable.speaker),
                            contentDescription = "avatar",
                            modifier = Modifier
                                .width(36.dp)
                                .height(36.dp)
                                .background(Color.White)
                                .align(Alignment.Center)
                        )
                    }
                }
            }
            Spacer(Modifier.padding(5.dp))
            Text(text = definition, style = Typography.body1, color = Color.White)
        }
    }
}