package com.nguyen.polygot.ui.components.auth

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
import com.nguyen.polygot.ui.theme.LightGrey
import com.nguyen.polygot.ui.theme.Typography

@Composable
fun LanguageBottomSheetContent(languages: List<String>, onLanguageClick: (language: String) -> Unit) {

    Box(
        Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(20.dp),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Pick a language",
                style = Typography.h5,
                color = Color.Black,
                modifier = Modifier.padding(10.dp)
            )
            languages.forEach { language ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .padding(vertical = 10.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .clickable { onLanguageClick(language) },
                    shape = RoundedCornerShape(20.dp),
                    backgroundColor = LightGrey,
                ) {
                    Box(Modifier.fillMaxWidth().fillMaxHeight(), contentAlignment = Alignment.CenterStart) {
                        Text(text = language, style = Typography.body1, color = Color.Gray, modifier = Modifier.padding(20.dp))
                    }
                }
            }
        }
    }
}