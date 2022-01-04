package com.nguyen.polyglot.ui.components.feed

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.nguyen.polyglot.R
import com.nguyen.polyglot.ui.components.RoundButton
import com.nguyen.polyglot.ui.theme.TextFieldGrey
import com.nguyen.polyglot.ui.theme.Typography
import kotlinx.coroutines.launch

@Composable
fun FeedTopBar(onClickTopicMenu: () -> Unit){

    Row(
        Modifier
            .padding(vertical = 20.dp, horizontal = 20.dp)
    ) {
        Column(Modifier.weight(3f)) {
            Text(text = stringResource(id = R.string.your_feed), style = Typography.h3)
            Text(
                text = stringResource(id = R.string.feed_subtitle),
                style = Typography.body2
            )
        }

        Column(
            Modifier
                .weight(1f)
                .align(Alignment.CenterVertically)
        ) {
            Box(
                Modifier
                    .align(Alignment.End)
            ) {
                RoundButton(
                    backgroundColor = TextFieldGrey,
                    size = 56.dp,
                    icon = R.drawable.paint,
                    padding = 11.dp
                ) {
                    onClickTopicMenu()
                }
            }
        }
    }
}