package com.nguyen.polyglot.ui.components

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
import com.nguyen.polyglot.R
import com.nguyen.polyglot.ui.theme.*
import com.nguyen.polyglot.util.UtilFunctions.generateRandomPastelColor

@ExperimentalAnimationApi
@Composable
fun SavedWordItem(word: String, pronunciationSymbol: String? = null, pronunciationAudio: String? = null, index: Int, onClick: () -> Unit) {

    Card(
        shape = RoundedCornerShape(15.dp),
        modifier = Modifier
            .padding(vertical = 15.dp)
            .fillMaxWidth()
            .height(70.dp)
            .clip(RoundedCornerShape(15.dp))
            .clickable { onClick() },
        backgroundColor = generateRandomPastelColor(word)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            if(pronunciationAudio.isNullOrBlank()){
                Text(text = word, style = Typography.h4)
            } else {
                Column {
                    Text(text = word, style = Typography.body1)
                    Text(text = pronunciationSymbol ?: "", style = Typography.body2)
                }

                RoundButton(backgroundColor = Color.White, size = 45.dp, icon = R.drawable.speaker) {

                }
            }
        }
    }
}

