package com.moderndev.polyglot.ui.components.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.moderndev.polyglot.model.Language
import com.moderndev.polyglot.ui.theme.Blue
import com.moderndev.polyglot.ui.theme.ReallyRed
import com.moderndev.polyglot.ui.theme.Typography
import com.moderndev.polyglot.util.SupportedLanguage
import com.moderndev.polyglot.R

@Composable
fun ChooseLanguagesHeader(pickedLanguages: List<Language>, onFinish: () -> Unit) {


    fun generateFlagForLanguage(language: String): Int {
        return when (language) {
            SupportedLanguage.ENGLISH.id -> SupportedLanguage.ENGLISH.flag
            SupportedLanguage.GERMANY.id -> SupportedLanguage.GERMANY.flag
            SupportedLanguage.FRENCH.id -> SupportedLanguage.FRENCH.flag
            SupportedLanguage.SPANISH.id -> SupportedLanguage.SPANISH.flag
            else -> SupportedLanguage.ENGLISH.flag
        }
    }


    Column(Modifier.padding(30.dp)) {
        Text(
            text = stringResource(id = R.string.language_menu_title),
            style = Typography.h3,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.padding(10.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = stringResource(id = R.string.your_picks), style = Typography.body1)

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
                Text(text = stringResource(id = R.string.language_menu_warning), style = Typography.body1, color = ReallyRed)
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