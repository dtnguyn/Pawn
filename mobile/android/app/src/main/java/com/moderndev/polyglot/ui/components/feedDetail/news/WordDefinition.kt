package com.moderndev.polyglot.ui.components.feedDetail.news

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.moderndev.polyglot.R
import com.moderndev.polyglot.model.Word
import com.moderndev.polyglot.ui.components.home.DailyWordCard
import com.moderndev.polyglot.ui.theme.Typography

@Composable
fun WordDefinition(word: Word?, isLoading: Boolean, onBackClick: () -> Unit, onDetailClick: () -> Unit) {
    Column() {
        Row(
            Modifier.padding(start = 20.dp, end = 20.dp, top = 20.dp, bottom = 5.dp),
            verticalAlignment = CenterVertically,
        ) {
            IconButton(
                onClick = onBackClick
            ) {
                Image(
                    painter = painterResource(id = R.drawable.back_32_black),
                    contentDescription = "Back icon"
                )

            }
            Spacer(modifier = Modifier.padding(5.dp))
            Text(
                text = stringResource(id = R.string.definition),
                style = Typography.h4,
            )
        }
        Text(
            text = stringResource(id = R.string.click_to_get_more_info),
            style = Typography.body1,
            fontSize = 18.sp,
            modifier = Modifier
                .padding(horizontal = 20.dp)
        )
        Spacer(modifier = Modifier.padding(10.dp))

        if(isLoading){
            DailyWordCard(
                isLoading = true,
                word = "",
                definition = "",
                onClick = {
                    onDetailClick()
                })
        } else {
            DailyWordCard(
                isLoading = false,
                word = word?.value ?: "",
                definition = word?.mainDefinition ?: "",
                onClick = {
                    onDetailClick()
                })
        }

        Spacer(Modifier.padding(10.dp))
    }
}