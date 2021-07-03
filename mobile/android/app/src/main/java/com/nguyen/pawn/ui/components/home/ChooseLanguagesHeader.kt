package com.nguyen.pawn.ui.components.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonColors
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.nguyen.pawn.R
import com.nguyen.pawn.model.Language
import com.nguyen.pawn.ui.theme.Blue
import com.nguyen.pawn.ui.theme.ReallyRed
import com.nguyen.pawn.ui.theme.Typography
import com.nguyen.pawn.util.SupportedLanguage

@Composable
fun ChooseLanguagesHeader(pickedLanguages: List<Language>, onFinish: () -> Unit) {


    fun generateFlagForLanguage(language: String): Int {
        return when (language) {
            SupportedLanguage.ENGLISH.value -> SupportedLanguage.ENGLISH.flag
            SupportedLanguage.GERMANY.value -> SupportedLanguage.GERMANY.flag
            SupportedLanguage.FRENCH.value -> SupportedLanguage.FRENCH.flag
            SupportedLanguage.SPANISH.value -> SupportedLanguage.SPANISH.flag
            else -> SupportedLanguage.ENGLISH.flag
        }
    }


    Column(Modifier.padding(30.dp)) {
        Text(
            text = "Choose languages that you want to learn",
            style = Typography.h3,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.padding(10.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Your picks:", style = Typography.body1)

            Button(onClick = { onFinish() }, colors = ButtonDefaults.buttonColors(Blue), shape = RoundedCornerShape(20.dp), enabled = pickedLanguages.isNotEmpty()) {
                Text(
                    text = "Done",
                    style = Typography.body1,
                )
            }
        }
        Spacer(modifier = Modifier.padding(5.dp))
        Row(Modifier.requiredHeight(32.dp)) {
            if(pickedLanguages.isEmpty()) {
                Text(text = "Please choose at least one language", style = Typography.body1, color = ReallyRed)
            } else {
                pickedLanguages.forEach { language ->
                    Image(
                        painter = painterResource(id = generateFlagForLanguage(language.id)),
                        contentDescription = "language icon",
                        modifier = Modifier
                            .padding(horizontal = 5.dp)
                            .size(32.dp)
                    )
                }
            }
        }
    }
}