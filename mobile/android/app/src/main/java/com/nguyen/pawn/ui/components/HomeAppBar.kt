package com.nguyen.pawn.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.nguyen.pawn.R
import com.nguyen.pawn.ui.theme.Typography

@Composable
fun HomeAppBar(){
    Row(
        Modifier
            .fillMaxWidth()
            .padding(20.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row {
            Image(
                painter = painterResource(R.drawable.me),
                contentDescription = "avatar",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .clickable(onClick = {})
            )
            Column( modifier =  Modifier.align(Alignment.CenterVertically).padding(10.dp)) {
                Text(text = "Adron", color = Color.White, style = Typography.h6)
                Text(text = "Welcome back!", color = Color.White, style = Typography.body1)
            }
        }
        RoundButton(backgroundColor = Color.White, size = 55.dp, icon = R.drawable.settings, onClick = {})

    }

}