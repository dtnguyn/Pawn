package com.nguyen.polyglot.ui.screens.account

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.nguyen.polyglot.R
import com.nguyen.polyglot.ui.SharedViewModel
import com.nguyen.polyglot.ui.theme.Blue
import com.nguyen.polyglot.ui.theme.Typography

@Composable
fun AccountScreen(navController: NavController, viewModel: AccountViewModel, sharedViewModel: SharedViewModel) {

    Scaffold(backgroundColor = Color.White) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)) {
            Box(modifier = Modifier.fillMaxWidth()) {
                IconButton(
                    onClick = { /*TODO*/ },
                    content = {
                        Image(painter = painterResource(id = R.drawable.close), contentDescription = "Close button")
                    },
                    modifier = Modifier
                        .size(30.dp)
                        .align(Alignment.CenterStart)
                )
                Text(text = "Account", style = Typography.h4, modifier = Modifier.align(Alignment.TopCenter))
                Text(text = "Logout", style = Typography.body1, color = Color.Red, modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .clickable { })
            }

//            Image(painter = painterResource(id = R.drawable.=), contentDescription = )
        }
    }
}