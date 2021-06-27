package com.nguyen.pawn.ui.components.home

import androidx.compose.foundation.Image
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
import com.nguyen.pawn.ui.theme.*
import com.nguyen.pawn.util.SupportedLanguage

@Composable
fun LanguageItem(
    language: SupportedLanguage,
    isPicked: Boolean,
    onToggleLanguage: (language: SupportedLanguage) -> Unit
) {

    fun generateBackgroundColorForLanguage(language: SupportedLanguage): Color {
        return when (language) {
            SupportedLanguage.ENGLISH -> Blue
            SupportedLanguage.GERMANY -> LightOrange
            SupportedLanguage.FRENCH -> DarkBlue
            SupportedLanguage.SPANISH -> LightRed
        }
    }

    fun generateFlagForLanguage(language: SupportedLanguage): Int {
        return when (language) {
            SupportedLanguage.ENGLISH -> R.drawable.england
            SupportedLanguage.GERMANY -> R.drawable.germany
            SupportedLanguage.FRENCH -> R.drawable.france
            SupportedLanguage.SPANISH -> R.drawable.spain
        }
    }

    fun generateIconForLanguage(language: SupportedLanguage): Int {
        return when (language) {
            SupportedLanguage.ENGLISH -> R.drawable.big_ben
            SupportedLanguage.GERMANY -> R.drawable.beer
            SupportedLanguage.FRENCH -> R.drawable.eiffel_tower
            SupportedLanguage.SPANISH -> R.drawable.bull
        }
    }


    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .padding(start = 30.dp, end = 30.dp, bottom = 20.dp)
            .clip(RoundedCornerShape(20.dp))
            .clickable {
                onToggleLanguage(language)
            },
        shape = RoundedCornerShape(20.dp),
        backgroundColor = generateBackgroundColorForLanguage(
            language
        )
    ) {
        Column(
            Modifier.padding(
                top = 15.dp,
                start = 15.dp,
                end = 15.dp
            )
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = language.value,
                    style = Typography.h5,
                    color = Color.White
                )
                Card(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape),
                    backgroundColor = if (isPicked) LightGreen else Grey
                ) {
                    if (isPicked)
                        Image(
                            painter = painterResource(id = R.drawable.check_32_green),
                            contentDescription = "Check icon"
                        )
                }
            }
            Row(
                Modifier
                    .fillMaxHeight()
                    .fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(
                        id = generateFlagForLanguage(language)
                    ),
                    contentDescription = "language flag",
                    modifier = Modifier
                        .size(64.dp)
                        .align(Alignment.CenterVertically)
                )
                Column(
                    Modifier
                        .fillMaxWidth()
                        .fillMaxWidth()
                        .align(Alignment.Bottom)
                ) {
                    Image(
                        painter = painterResource(id = generateIconForLanguage(language)),
                        contentDescription = "language icon",
                        modifier = Modifier
                            .size(120.dp)
                            .align(Alignment.CenterHorizontally)
                    )
                }

            }
        }
    }
}