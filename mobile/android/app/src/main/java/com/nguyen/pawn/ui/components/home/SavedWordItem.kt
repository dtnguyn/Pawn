package com.nguyen.pawn.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.nguyen.pawn.R
import com.nguyen.pawn.ui.theme.*

@Composable
fun SavedWordItem(word: String, pronunciation: String, index: Int) {
    Card(
        shape = RoundedCornerShape(15.dp),
        modifier = Modifier
            .padding(horizontal = 30.dp, vertical = 15.dp)
            .fillMaxWidth()
            .height(70.dp),
        backgroundColor = generateColor(index + 1)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(text = word, style = Typography.body1)
                Text(text = pronunciation, style = Typography.body2)
            }

            RoundButton(backgroundColor = Color.White, size = 45.dp, icon = R.drawable.speaker) {

            }
        }
    }
}

fun generateColor(index: Int): Color {
    return when {
        index % 4 == 0 -> LightGreen
        index % 3 == 0 -> LightOrange
        index % 2 == 0 -> Neon
        else -> LightRed
    }
}