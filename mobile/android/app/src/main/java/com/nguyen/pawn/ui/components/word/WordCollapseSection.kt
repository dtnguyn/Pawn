package com.nguyen.pawn.ui.components.word

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import com.nguyen.pawn.model.WordDetail
import com.nguyen.pawn.ui.theme.ReallyRed
import com.nguyen.pawn.ui.theme.Typography
import com.nguyen.pawn.util.ShimmerAnimation
import com.nguyen.pawn.util.UtilFunctions

@Composable
fun WordCollapseSection(wordDetail: WordDetail?, loading: Boolean){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp),
        backgroundColor = ReallyRed,
        elevation = 0.dp,
        shape = RectangleShape
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(bottom = 50.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (wordDetail == null || loading) {
                ShimmerAnimation(
                    modifier = Modifier
                        .width(250.dp)
                        .height(50.dp),
                    shape = RoundedCornerShape(30.dp)
                )
                Spacer(modifier = Modifier.padding(5.dp))
                ShimmerAnimation(
                    modifier = Modifier
                        .width(100.dp)
                        .height(50.dp),
                    shape = RoundedCornerShape(30.dp)
                )
            } else {
                Text(text = wordDetail.value, style = Typography.h1, color = Color.White)
                Spacer(modifier = Modifier.padding(5.dp))
                wordDetail.pronunciations.forEach {
                    if (it.symbol != null) {
                        Text(
                            text = it.symbol,
                            style = Typography.body2,
                            color = Color.White
                        )
                    }
                }
                Spacer(modifier = Modifier.padding(10.dp))
            }
        }
    }
}