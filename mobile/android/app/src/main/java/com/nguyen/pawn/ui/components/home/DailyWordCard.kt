package com.nguyen.pawn.ui.components

import android.util.Log
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
import com.nguyen.pawn.util.ShimmerAnimation

private const val TAG = "DailyWordCard"

@Composable
fun DailyWordCard(
    isLoading: Boolean,
    word: String,
    definition: String,
    pronunciation: String?,
    onClick: () -> Unit
) {

    Box(Modifier.padding(horizontal = 30.dp)) {
        Card(
            shape = RoundedCornerShape(60.dp),
            modifier = Modifier
                .clip(RoundedCornerShape(60.dp))
                .height(250.dp)
                .fillMaxWidth()
                .clickable(onClick = onClick),
            backgroundColor = ReallyRed
        ) {
            if (isLoading) {
                Column(Modifier.padding(25.dp)) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        ShimmerAnimation(width = 200.dp, height = 40.dp, borderRadius = 30.dp)
                        RoundButton(
                            backgroundColor = Color.White,
                            size = 45.dp,
                            icon = R.drawable.speaker,
                            onClick = {})
                    }
                    Spacer(Modifier.padding(15.dp))
                    ShimmerAnimation(width = 250.dp, height = 20.dp, borderRadius = 30.dp)
                    Spacer(Modifier.padding(5.dp))
                    ShimmerAnimation(width = 250.dp, height = 20.dp, borderRadius = 30.dp)
                    Spacer(Modifier.padding(5.dp))
                    ShimmerAnimation(width = 150.dp, height = 20.dp, borderRadius = 30.dp)
                }
            } else {
                Column(Modifier.padding(25.dp)) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column {
                            Text(text = word, style = Typography.h1, color = Color.White)
                            Text(
                                text = pronunciation ?: "",
                                style = Typography.body2,
                                color = Color.White
                            )
                        }
                        RoundButton(
                            backgroundColor = Color.White,
                            size = 45.dp,
                            icon = R.drawable.speaker,
                            onClick = {})
                    }
                    Spacer(Modifier.padding(5.dp))
                    Text(text = definition, style = Typography.body1, color = Color.White)
                }
            }

        }
    }

}