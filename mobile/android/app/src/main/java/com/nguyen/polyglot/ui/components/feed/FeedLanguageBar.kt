package com.nguyen.polyglot.ui.components.feed

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.nguyen.polyglot.model.Language
import com.nguyen.polyglot.ui.theme.DarkBlue
import com.nguyen.polyglot.util.UtilFunctions

@Composable
fun FeedLanguageBar(
    languages: List<Language>,
    currentPickedLanguage: Language,
    onPickLanguage: (language: Language) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(bottom = 40.dp, start = 20.dp, end = 20.dp)

    ) {
        languages.forEach { language ->
            Image(
                painter = painterResource(
                    id = UtilFunctions.generateFlagForLanguage(
                        language.id
                    )
                ),
                contentDescription = "language icon",
                modifier = Modifier
                    .padding(horizontal = 5.dp)
                    .size(if (language.id == currentPickedLanguage.id) 46.dp else 38.dp)
                    .clip(CircleShape)
                    .border(
                        if (language.id == currentPickedLanguage.id) 3.dp else 0.dp,
                        DarkBlue,
                        CircleShape
                    )
                    .clickable {
                        onPickLanguage(language)
                    }
            )
        }
    }
}