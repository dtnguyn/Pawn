package com.nguyen.polyglot.ui.screens.stats

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.nguyen.polyglot.R
import com.nguyen.polyglot.ui.SharedViewModel
import com.nguyen.polyglot.ui.components.stats.CustomPieChart
import com.nguyen.polyglot.ui.theme.*

@Composable
fun StatsScreen(navController: NavController, sharedViewModel: SharedViewModel) {

    Scaffold(backgroundColor = Color.White) {
        Column(Modifier.padding(20.dp)) {
            Row() {
                Image(
                    painter = painterResource(R.drawable.me),
                    contentDescription = "avatar",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .clickable(onClick = {})
                )
                Spacer(modifier = Modifier.padding(3.dp))
                Column(Modifier.align(CenterVertically)) {
                    Text(text = "Adron", style = Typography.h3)
                    Text(text = "Joined since 2020", style = Typography.h6)
                }

            }
            Spacer(modifier = Modifier.padding(10.dp))
            CustomPieChart(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(40f),
                progress = listOf(30f, 20f, 10f, 40f),
                colors = listOf(
                    LightRed, LightGreen, LightOrange, LightGrey
                ),
                isDonut = true,
            )
        }

    }

}