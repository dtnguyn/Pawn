package com.nguyen.pawn.ui.components

import androidx.compose.animation.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.nguyen.pawn.R
import com.nguyen.pawn.ui.theme.*
import com.nguyen.pawn.util.UtilFunctions.generateColor

@ExperimentalAnimationApi
@Composable
fun SavedWordItem(word: String, pronunciation: String?, index: Int, onClick: () -> Unit) {

    AnimatedVisibility(
        visible = true,
        enter = slideInVertically(initialOffsetY = { 400 }) + expandVertically(expandFrom = Alignment.Top) + fadeIn(initialAlpha = 0.3f),
        exit = slideOutVertically(targetOffsetY = { 400 }) + shrinkVertically() + fadeOut(),

    ) {
        Card(
            shape = RoundedCornerShape(15.dp),
            modifier = Modifier
                .padding(horizontal = 30.dp, vertical = 15.dp)
                .fillMaxWidth()
                .height(70.dp)
                .clip(RoundedCornerShape(15.dp))
                .clickable { onClick() },
            backgroundColor = generateColor(index + 1)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(10.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(text = word, style = Typography.body1)
                    Text(text = pronunciation ?: "", style = Typography.body2)
                }

                RoundButton(backgroundColor = Color.White, size = 45.dp, icon = R.drawable.speaker) {

                }
            }
        }
    }

}

