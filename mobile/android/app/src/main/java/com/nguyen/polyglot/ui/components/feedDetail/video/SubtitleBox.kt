package com.nguyen.polyglot.ui.components.feedDetail.video

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import com.nguyen.polyglot.model.SubtitlePart
import com.nguyen.polyglot.ui.theme.LightGreen
import com.nguyen.polyglot.ui.theme.Neon
import com.nguyen.polyglot.ui.theme.Typography
import com.nguyen.polyglot.util.UtilFunctions.reformatString

@Composable
fun SubtitleBox(
    selected: Boolean,
    isTranslated: Boolean,
    subtitlePart: SubtitlePart,
    onClick: () -> Unit
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
                .clickable {
                    onClick()
                }

            ,
            backgroundColor = if (selected) Neon else Color.LightGray,
        ) {
            Column(Modifier.padding(20.dp)) {
                Text(text = subtitlePart.lang, style = Typography.h6)
                Spacer(modifier = Modifier.padding(3.dp))
                Text(text = reformatString(subtitlePart.text ?: ""), style = Typography.body1, fontSize = 18.sp)
                if(isTranslated){
                    Spacer(modifier = Modifier.padding(10.dp))
                    Text(text = subtitlePart.translatedLang, style = Typography.h6)
                    Spacer(modifier = Modifier.padding(3.dp))
                    Text(text = reformatString(subtitlePart.translatedText ?: ""), style = Typography.body1, fontSize = 18.sp)
                }


            }
        }

    }
}