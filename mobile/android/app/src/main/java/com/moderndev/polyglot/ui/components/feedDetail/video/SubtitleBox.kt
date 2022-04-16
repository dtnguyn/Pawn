package com.moderndev.polyglot.ui.components.feedDetail.video

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.moderndev.polyglot.model.SubtitlePart
import com.moderndev.polyglot.ui.theme.LightGreen
import com.moderndev.polyglot.ui.theme.Neon
import com.moderndev.polyglot.ui.theme.Typography
import com.moderndev.polyglot.util.UtilFunctions.reformatString

@ExperimentalFoundationApi
@Composable
fun SubtitleBox(
    selected: Boolean,
    isTranslated: Boolean,
    subtitlePart: SubtitlePart,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(if (selected) LightGreen else Color.White),
    ) {

        Card(
            shape = RoundedCornerShape(30.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp, vertical = 15.dp)
                .clip(RoundedCornerShape(30.dp))
                .combinedClickable(
                    onClick = {
                        onClick()
                    },
                    onLongClick = {
                        onLongClick()
                    }
                ),
            backgroundColor = if (selected) Neon else Color.LightGray,
        ) {
            Column(Modifier.padding(20.dp)) {
                Text(text = subtitlePart.lang, style = Typography.h6)
                Spacer(modifier = Modifier.padding(3.dp))
                Text(
                    text = reformatString(subtitlePart.text ?: ""),
                    style = Typography.body1,
                    fontSize = 18.sp
                )
                if (isTranslated) {
                    Spacer(modifier = Modifier.padding(10.dp))
                    Text(text = subtitlePart.translatedLang, style = Typography.h6)
                    Spacer(modifier = Modifier.padding(3.dp))
                    Text(
                        text = reformatString(subtitlePart.translatedText ?: ""),
                        style = Typography.body1,
                        fontSize = 18.sp
                    )
                }


            }
        }

    }
}