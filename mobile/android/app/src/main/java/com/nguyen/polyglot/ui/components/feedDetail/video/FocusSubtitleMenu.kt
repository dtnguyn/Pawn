package com.nguyen.polyglot.ui.components.feedDetail.video

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.nguyen.polyglot.model.SubtitlePart
import com.nguyen.polyglot.ui.components.SelectableText
import com.nguyen.polyglot.ui.theme.Neon
import com.nguyen.polyglot.ui.theme.Typography
import com.nguyen.polyglot.util.UtilFunctions.reformatString
import kotlinx.coroutines.launch
import androidx.compose.runtime.*
import androidx.compose.ui.window.DialogProperties
import com.nguyen.polyglot.ui.theme.LightGreen
import com.nguyen.polyglot.ui.theme.LightRed

@Composable
fun FocusSubtitleMenu(
    subtitlePart: SubtitlePart,
    currentFocusWord: String?,
    mainLanguage: String,
    onDismiss: () -> Unit,
    onLongClick: (word: String) -> Unit
) {

    Column(Modifier.padding(20.dp)) {
        Text(text = "Subtitle Action", style = Typography.h6)
        Card(
            shape = RoundedCornerShape(30.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp, vertical = 15.dp),

            backgroundColor = Neon,
        ) {
            Column(Modifier.padding(30.dp)) {
                Text(text = mainLanguage, style = Typography.h6)
                Spacer(modifier = Modifier.padding(5.dp))

                SelectableText(
                    text = reformatString(subtitlePart.text ?: ""),
                    isFocusing = currentFocusWord != null,
                    onLongClick = { word ->
                        if (word != "") {
                            onLongClick(word)
                        }
                    }
                )
            }
        }
        Spacer(modifier = Modifier.padding(10.dp))


        Button(
            onClick = {
            },
            content = {
                Text(text = "Translate text", style = Typography.h6)
            },
            shape = RoundedCornerShape(30.dp),
            colors = ButtonDefaults.buttonColors(LightRed),
        )

        currentFocusWord?.let{
            Spacer(modifier = Modifier.padding(5.dp))
            Button(
                onClick = {
                },
                content = {
                    Text(text = "Find definition for \"${it}\" ", style = Typography.h6)
                },
                shape = RoundedCornerShape(30.dp),
                colors = ButtonDefaults.buttonColors(LightGreen),
            )
        }

    }


}