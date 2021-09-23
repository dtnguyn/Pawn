package com.nguyen.polygot.ui.screens

import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.nguyen.polygot.R
import com.nguyen.polygot.model.Feed
import com.nguyen.polygot.model.Language
import com.nguyen.polygot.ui.SharedViewModel
import com.nguyen.polygot.ui.components.RoundButton
import com.nguyen.polygot.ui.components.feed.FeedItem
import com.nguyen.polygot.ui.components.feed.LoadingFeedItem
import com.nguyen.polygot.ui.screens.feeds.FeedViewModel
import com.nguyen.polygot.ui.theme.DarkBlue
import com.nguyen.polygot.ui.theme.LightGrey
import com.nguyen.polygot.ui.theme.TextFieldGrey
import com.nguyen.polygot.ui.theme.Typography
import com.nguyen.polygot.util.DataStoreUtils
import com.nguyen.polygot.util.UIState
import com.nguyen.polygot.util.UtilFunctions

@Composable
fun FeedScreen(
    sharedViewModel: SharedViewModel,
    feedViewModel: FeedViewModel
) {

    val feedUIState: UIState<List<Feed>> by feedViewModel.feedItems
    var feedItems by remember { mutableStateOf(feedUIState.value) }
    val context = LocalContext.current
    val currentPickedLanguage: Language? by sharedViewModel.currentPickedLanguage

    var isLoading by remember { mutableStateOf(false) }


    LaunchedEffect(currentPickedLanguage) {
        currentPickedLanguage?.id?.let {
            feedViewModel.getFeeds(DataStoreUtils.getAccessTokenFromDataStore(context), it)
        }
    }

    LaunchedEffect(feedUIState) {
        when (feedUIState) {
            is UIState.Initial -> {
                Log.d("FeedScreen", "initial")
            }

            is UIState.Loading -> {
                Log.d("FeedScreen", "loading")
                isLoading = true
            }

            is UIState.Error -> {
                Log.d("FeedScreen", "error ${feedUIState.errorMsg}")
                isLoading = false
            }

            is UIState.Loaded -> {
                Log.d("FeedScreen", "loaded: ${feedUIState.value?.size}")
                feedItems = feedUIState.value
                isLoading = false
            }
        }
    }


    Scaffold(
        Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        backgroundColor = Color.White
    ) {
        LazyColumn(Modifier.padding(bottom = 50.dp)) {
            item {

                Row(
                    Modifier
                        .padding(vertical = 20.dp, horizontal = 20.dp)
                ) {
                    Column(Modifier.weight(3f)) {
                        Text(text = "Your feed", style = Typography.h3)
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


            if (isLoading) {
                items(5) { index ->
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
                        LoadingFeedItem()
                    }
                }
            } else {
                feedItems?.let {
                    if (it.isEmpty()) {
                        item {
                            Box(
                                Modifier
                                    .fillParentMaxWidth()
                                    .fillParentMaxHeight(0.8f)
                                    .background(
                                        LightGrey,
                                        RoundedCornerShape(
                                            topStart = 30.dp,
                                            topEnd = 30.dp
                                        )
                                    )
                                    .padding(horizontal = 20.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = "Your feed is empty right now",
                                        style = Typography.h5,
                                        color = Color.Gray,
                                        textAlign = TextAlign.Center
                                    )
                                    Spacer(modifier = Modifier.padding(10.dp))
                                    Spacer(modifier = Modifier.padding(10.dp))
                                    Image(
                                        painter = painterResource(id = R.drawable.list_icon),
                                        contentDescription = "list_icon",
                                        modifier = Modifier
                                            .size(128.dp)
                                            .padding(end = 5.dp)
                                    )
                                    Spacer(modifier = Modifier.padding(10.dp))

                                    Text(
                                        text = "Save more words to get more news and videos to your feed",
                                        style = Typography.body1,
                                        color = Color.Gray,
                                        textAlign = TextAlign.Center
                                    )

                                    Spacer(modifier = Modifier.padding(10.dp))
                                    Spacer(modifier = Modifier.padding(10.dp))
                                    Spacer(modifier = Modifier.padding(10.dp))
                                    Spacer(modifier = Modifier.padding(10.dp))


                                }

                            }
                        }
                    } else {
                        items(it.size) { index ->
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
                                FeedItem(it[index])
                            }
                        }
                    }
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