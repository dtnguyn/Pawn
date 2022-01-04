package com.nguyen.polyglot.ui.components.stats

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.nguyen.polyglot.R
import com.nguyen.polyglot.model.Language
import com.nguyen.polyglot.ui.theme.Grey
import com.nguyen.polyglot.ui.theme.LightGrey
import com.nguyen.polyglot.ui.theme.Typography
import com.nguyen.polyglot.util.UtilFunctions.generateBackgroundColorForLanguage
import com.nguyen.polyglot.util.UtilFunctions.generateFlagForLanguage

@Composable
fun LanguageBox(language: Language?, wordCount: Int, isActive: Boolean = false, onClick: (id: String) -> Unit) {

    if(language == null) return

    Column(
        Modifier
            .width(120.dp)
            .padding(10.dp)
            .clip(RoundedCornerShape(10.dp))
            .clickable {
                onClick(language.id)
            }
            .background(if (isActive) LightGrey else Color.White, RoundedCornerShape(10.dp))
    ) {
        Box(modifier = Modifier
            .fillMaxWidth(0.7f)
            .height(4.dp)
            .background(generateBackgroundColorForLanguage(language.id), RoundedCornerShape(topStart = 10.dp, topEnd = 40.dp, bottomEnd = 40.dp)))
        Spacer(modifier = Modifier.padding(5.dp))
        Column(Modifier.padding(5.dp)) {
            Image(
                painter = painterResource(id = generateFlagForLanguage(language.id)),
                contentDescription = "language icon",
                modifier = Modifier
                    .size(32.dp)
            )
            Spacer(modifier = Modifier.padding(5.dp))
            Text(text = language.value, style = Typography.body1)
            Text(text = "$wordCount ${stringResource(id = R.string.words)}", style = Typography.subtitle1)
        }

    }
}