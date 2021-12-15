package com.nguyen.polyglot.ui.screens

import android.os.Build.VERSION.SDK_INT
import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import com.nguyen.polyglot.model.Feed
import com.nguyen.polyglot.model.Language
import com.nguyen.polyglot.ui.SharedViewModel
import com.nguyen.polyglot.ui.components.feed.*
import com.nguyen.polyglot.ui.navigation.PolyglotScreens
import com.nguyen.polyglot.ui.screens.feeds.FeedViewModel
import com.nguyen.polyglot.ui.theme.*
import com.nguyen.polyglot.util.DataStoreUtils
import com.nguyen.polyglot.util.UIState
import kotlinx.coroutines.launch
import java.util.*



@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FeedScreen(
    sharedViewModel: SharedViewModel,
    feedViewModel: FeedViewModel,
    navController: NavController
) {



    val enFeedUIState: UIState<List<Feed>> by feedViewModel.enFeedItems
    var enFeedItems by remember { mutableStateOf(enFeedUIState.value) }

    val esFeedUIState: UIState<List<Feed>> by feedViewModel.esFeedItems
    var esFeedItems by remember { mutableStateOf(esFeedUIState.value) }

    val frFeedUIState: UIState<List<Feed>> by feedViewModel.frFeedItems
    var frFeedItems by remember { mutableStateOf(frFeedUIState.value) }

    val deFeedUIState: UIState<List<Feed>> by feedViewModel.deFeedItems
    var deFeedItems by remember { mutableStateOf(deFeedUIState.value) }

    fun initialFeed(languageId: String?): List<Feed>?{
        var feed: List<Feed>? = null
        when(languageId){
            "en_US" -> {
                feed = enFeedUIState.value
            }
            "es" -> {
                feed = esFeedUIState.value
            }
            "fr" -> {
                feed = frFeedUIState.value
            }
            "de" -> {
                feed = deFeedUIState.value
            }
        }
        return feed

    }

    var feedItems by remember { mutableStateOf(initialFeed(sharedViewModel.currentPickedLanguage.value?.id)) }



    val topicsUIState: UIState<String> by feedViewModel.topics
    var topics by remember { mutableStateOf(topicsUIState.value ?: "") }

    val context = LocalContext.current
    val currentPickedLanguage: Language? by sharedViewModel.currentPickedLanguage
    val bottomSheetScaffoldState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden
    )
    val coroutineScope = rememberCoroutineScope()
    val feedScrollState = rememberLazyListState()

    var isLoading by remember { mutableStateOf(false) }

    var customFeedDialog by remember { mutableStateOf(false) }
    var customFeedUrl by remember { mutableStateOf("") }
    var customFeedType by remember { mutableStateOf("news") }


    val imageLoader = ImageLoader.invoke(context).newBuilder()
        .componentRegistry {
            if (SDK_INT >= 28) {
                add(ImageDecoderDecoder(context))
            } else {
                add(GifDecoder())
            }
        }.build()


    LaunchedEffect(true) {
        feedViewModel.getTopics(DataStoreUtils.getAccessTokenFromDataStore(context))
    }

    LaunchedEffect(topicsUIState) {
        when (topicsUIState) {
            is UIState.Initial -> {

            }
            is UIState.Loading -> {
                Log.d("FeedScreen", "topics loading")
            }
            is UIState.Error -> {
                Log.d("FeedScreen", "topics error: ${topicsUIState.errorMsg}")
            }
            is UIState.Loaded -> {
                Log.d("FeedScreen", "topics loaded: ${topicsUIState.value}")
                if (topicsUIState.value != null) {
                    topics = topicsUIState.value!!
                }
            }
        }
    }



    LaunchedEffect(currentPickedLanguage) {
        currentPickedLanguage?.id?.let {
            feedViewModel.getFeed(DataStoreUtils.getAccessTokenFromDataStore(context), it)
        }
    }



    LaunchedEffect(enFeedUIState) {
        when (enFeedUIState) {
            is UIState.Initial -> {
                Log.d("FeedScreen", "initial")
            }

            is UIState.Loading -> {
                Log.d("FeedScreen", "loading")
                isLoading = true
            }

            is UIState.Error -> {
                Log.d("FeedScreen", "error ${enFeedUIState.errorMsg}")
                isLoading = false
            }

            is UIState.Loaded -> {
                Log.d("FeedScreen", "loaded: ${enFeedUIState.value?.size}")
                if(currentPickedLanguage?.id == "en_US"){
                    feedItems = enFeedUIState.value
                    isLoading = false
                }
            }
        }
    }

    LaunchedEffect(esFeedUIState) {
        when (esFeedUIState) {
            is UIState.Initial -> {
                Log.d("FeedScreen", "initial")
            }

            is UIState.Loading -> {
                Log.d("FeedScreen", "loading")
                isLoading = true
            }

            is UIState.Error -> {
                Log.d("FeedScreen", "error ${esFeedUIState.errorMsg}")
                isLoading = false
            }

            is UIState.Loaded -> {
                Log.d("FeedScreen", "loaded: ${esFeedUIState.value?.size}")
                Log.d("FeedScreen", "current Picked language ${currentPickedLanguage?.id}")
                if(currentPickedLanguage?.id == "es"){
                    feedItems = esFeedUIState.value
                    isLoading = false
                }
            }
        }
    }

    LaunchedEffect(frFeedUIState) {
        when (frFeedUIState) {
            is UIState.Initial -> {
                Log.d("FeedScreen", "initial")
            }

            is UIState.Loading -> {
                Log.d("FeedScreen", "loading")
                isLoading = true
            }

            is UIState.Error -> {
                Log.d("FeedScreen", "error ${frFeedUIState.errorMsg}")
                isLoading = false
            }

            is UIState.Loaded -> {
                Log.d("FeedScreen", "loaded: ${frFeedUIState.value?.size}")
                if(currentPickedLanguage?.id == "fr"){
                    feedViewModel.currentFeedIndex = 0
                    feedViewModel.currentFeedOffset = 0
                    feedItems = frFeedUIState.value
                    isLoading = false
                }
            }
        }
    }

    LaunchedEffect(deFeedUIState) {
        when (deFeedUIState) {
            is UIState.Initial -> {
                Log.d("FeedScreen", "initial")
            }

            is UIState.Loading -> {
                Log.d("FeedScreen", "loading")
                isLoading = true
            }

            is UIState.Error -> {
                Log.d("FeedScreen", "error ${deFeedUIState.errorMsg}")
                isLoading = false
            }

            is UIState.Loaded -> {
                Log.d("FeedScreen", "loaded: ${deFeedUIState.value?.size}")
                if(currentPickedLanguage?.id == "de"){
                    feedViewModel.currentFeedIndex = 0
                    feedViewModel.currentFeedOffset = 0
                    feedItems = deFeedUIState.value
                    isLoading = false
                }
            }
        }
    }




    ModalBottomSheetLayout(
        sheetShape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
        sheetContent = {
            TopicMenu(
                topics = topics,
                onPickTopic = {
                    feedViewModel.pickTopic(it)
                },
                isPicked = {
                    feedViewModel.isTopicPicked(it)
                },
                onFinish = {
                    coroutineScope.launch {
                        feedViewModel.updateTopics(
                            DataStoreUtils.getAccessTokenFromDataStore(
                                context
                            )
                        )
                        bottomSheetScaffoldState.hide()
                    }
                },
                onDismiss = {
                    feedViewModel.dismissTopics()
                }
            )
        },
        sheetState = bottomSheetScaffoldState,
    ) {
        Scaffold(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            backgroundColor = Color.White,
        ) {

            LazyColumn(modifier = Modifier.padding(bottom = 50.dp), state = feedScrollState) {
                coroutineScope.launch {
                    feedScrollState.scrollToItem(
                        feedViewModel.currentFeedIndex,
                        feedViewModel.currentFeedOffset
                    )
                }
                item {
                    FeedTopBar(onClickTopicMenu = {
                        coroutineScope.launch {
                            if (bottomSheetScaffoldState.isVisible) bottomSheetScaffoldState.hide()
                            else bottomSheetScaffoldState.show()
                        }
                    })
                }

                item {
                    sharedViewModel.pickedLanguagesUIState.value.value?.let { languages ->
                        currentPickedLanguage?.let { language ->
                            FeedLanguageBar(
                                languages = languages,
                                currentPickedLanguage = language,
                                onPickLanguage = {
                                    feedViewModel.currentFeedIndex = 0
                                    feedViewModel.currentFeedOffset = 0
                                    sharedViewModel.changeCurrentPickedLanguage(
                                        it
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
                                    FeedEmptyPlaceholder()
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
                                    FeedItem(feed = it[index], onClick = { feed ->
                                        val url = feed.url.replace("/", "<")
                                        val thumbnail = feed.thumbnail?.replace("/", "<")
                                        if (feed.type == "news") {
                                            feedViewModel.saveFeedScrollingState(
                                                feedScrollState.firstVisibleItemIndex,
                                                feedScrollState.firstVisibleItemScrollOffset
                                            )
                                            navController.navigate("${PolyglotScreens.NewsDetail.route}/${feed.id}/${url}")
                                        } else {
                                            feedViewModel.saveFeedScrollingState(
                                                feedScrollState.firstVisibleItemIndex,
                                                feedScrollState.firstVisibleItemScrollOffset
                                            )
                                            navController.navigate("${PolyglotScreens.VideoDetail.route}/${feed.id}")
                                        }
                                    })
                                }
                            }
                        }
                    }
                }

            }

            if (customFeedDialog) {
                CustomFeedDialog(
                    customUrl = customFeedUrl,
                    currentType = customFeedType,
                    imageLoader = imageLoader,
                    onDismiss = { customFeedDialog = false },
                    onHandleUrlChange = { customFeedUrl = it },
                    onChangeType = { customFeedType = it },
                    onAddClick = {
                        feedViewModel.saveFeedScrollingState(
                            feedScrollState.firstVisibleItemIndex,
                            feedScrollState.firstVisibleItemScrollOffset
                        )
                        if (customFeedType == "news"){
                            val url = it.replace("/", "<")
                            val date = null
                            val thumbnail = null
                            val title = null
                            navController.navigate("${PolyglotScreens.NewsDetail.route}/${UUID.randomUUID()}/${url}")
                        } else {
                            val videoId = it.substring(it.length - 11, it.length)
                            Log.d("FeedScreen", "videoId: $videoId")
                            navController.navigate("${PolyglotScreens.VideoDetail.route}/${videoId}")
                        }
                    }
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
            ) {
                Button(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(bottom = 70.dp, end = 10.dp)
                        .size(56.dp),
                    shape = CircleShape,
                    onClick = {
                        customFeedDialog = true
                    }
                ) {
                    Icon(Icons.Filled.Add, "")
                }
            }
        }
    }
}