package com.nguyen.pawn.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.nguyen.pawn.R
import com.nguyen.pawn.model.User
import com.nguyen.pawn.ui.theme.*

@Composable
fun HomeAppBar(navController: NavController, user: User?){
    Row(
        Modifier
            .fillMaxWidth()
            .height(120.dp)
            .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if(user == null) {
            Column( modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(10.dp)) {
                Text(text = "Welcome to Pawn!", color = Color.White, style = Typography.h6)
                Button(onClick = { navController.navigate("auth") }, colors = ButtonDefaults.buttonColors(ReallyRed), shape = RoundedCornerShape(20.dp)) {
                    Text(text = "Login", color = Color.White, style = Typography.h6)
                }
            }
        } else {
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
                Column( modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(10.dp)) {
                    Text(text = user.username, color = Color.White, style = Typography.h6)
                    Text(text = "Welcome back!", color = Color.White, style = Typography.body1)
                }
        }

        }
        RoundButton(backgroundColor = Color.White, size = 55.dp, icon = R.drawable.settings, padding = 12.dp, onClick = {})

    }

}