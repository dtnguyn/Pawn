package com.nguyen.polygot.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.nguyen.polygot.R
import com.nguyen.polygot.model.Language
import com.nguyen.polygot.ui.SharedViewModel
import com.nguyen.polygot.ui.components.RoundButton
import com.nguyen.polygot.ui.components.feed.FeedItem
import com.nguyen.polygot.ui.theme.DarkBlue
import com.nguyen.polygot.ui.theme.LightGrey
import com.nguyen.polygot.ui.theme.TextFieldGrey
import com.nguyen.polygot.ui.theme.Typography
import com.nguyen.polygot.util.UtilFunctions

@Composable
fun FeedScreen(
    sharedViewModel: SharedViewModel
) {

    val currentPickedLanguage: Language? by sharedViewModel.currentPickedLanguage




    Scaffold(
        Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        backgroundColor = Color.White
    ) {
        LazyColumn() {
            item {

                Row(
                    Modifier
                        .padding(vertical = 20.dp, horizontal = 20.dp)
                ) {
                    Column(Modifier.weight(3f)) {
                        Text(text = "Your feeds", style = Typography.h3)
                        Text(
                            text = "News and videos generated from your saved words",
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
                            }
                        }
                    }
                }
            }

            item {

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(bottom = 40.dp, start = 20.dp, end = 20.dp)

                ) {
                    sharedViewModel.pickedLanguagesUIState.value.value?.forEach { language ->
                        Image(
                            painter = painterResource(
                                id = UtilFunctions.generateFlagForLanguage(
                                    language.id
                                )
                            ),
                            contentDescription = "language icon",
                            modifier = Modifier
                                .padding(horizontal = 5.dp)
                                .size(if (language.id == currentPickedLanguage?.id) 46.dp else 38.dp)
                                .clip(CircleShape)
                                .border(
                                    if (language.id == currentPickedLanguage?.id) 3.dp else 0.dp,
                                    DarkBlue,
                                    CircleShape
                                )
                                .clickable {
                                    sharedViewModel.changeCurrentPickedLanguage(
                                        language
                                    )
                                }
                        )
                    }
                }
            }

            items(19) { index ->
                Box(
                    Modifier
                        .background(
                            LightGrey,
                            RoundedCornerShape(
                                topStart = if (index == 0) 30.dp else 0.dp,
                                topEnd = if (index == 0) 30.dp else 0.dp
                            )
                        )
                        .padding(horizontal = 20.dp)
                ) {
                    FeedItem()

                }
            }
//            Card(
//                shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .fillMaxHeight()
//                    .verticalScroll(rememberScrollState()),
//                backgroundColor = LightGrey,
//                elevation = 4.dp
//            ) {
//                LazyColumn(modifier = Modifier.padding(horizontal = 20.dp,)) {
//
//                }
//            }

        }
    }
}