package com.nguyen.polyglot.ui.components.feedDetail.video

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nguyen.polyglot.model.SubtitlePart
import com.nguyen.polyglot.ui.theme.LightGreen
import com.nguyen.polyglot.ui.theme.Neon
import com.nguyen.polyglot.ui.theme.Typography

@Composable
fun SubtitleBox(selected: Boolean, mainLanguage: String, subtitleText: String?){
    if(subtitleText == null) return

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(if (selected) LightGreen else Color.White),
    ) {

        Card(
            shape = RoundedCornerShape(30.dp),
            modifier = Modifier.fillMaxWidth().padding(horizontal = 30.dp, vertical = 15.dp),
            backgroundColor = if(selected) Neon else Color.LightGray,
        ) {
            Column(Modifier.padding(30.dp)) {
                Text(text = mainLanguage, style = Typography.h6)
                Spacer(modifier = Modifier.padding(5.dp))
                Text(text = subtitleText, style = Typography.body1, fontSize = 18.sp)
            }
        }

    }
}