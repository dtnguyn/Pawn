package com.nguyen.pawn.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nguyen.pawn.ui.components.DailyWordCard
import com.nguyen.pawn.ui.components.HomeAppBar
import com.nguyen.pawn.ui.theme.AlmostBlack
import com.nguyen.pawn.ui.theme.Typography

@Preview
@Composable
fun HomeScreen() {
    Surface(
        color = AlmostBlack,
    ) {
        Scaffold(
            Modifier
                .fillMaxHeight()
                .fillMaxWidth()
        ) {
            Column {
                HomeAppBar()
                Card(
                    shape = RoundedCornerShape(topStart = 60.dp, topEnd = 60.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    elevation = 10.dp
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(text = "Daily words", modifier = Modifier.padding(10.dp), style = Typography.h6)
                        DailyWordCard()
                    }
                }
            }
        }
    }
}