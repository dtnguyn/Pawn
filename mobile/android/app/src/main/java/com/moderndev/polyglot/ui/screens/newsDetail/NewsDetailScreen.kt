package com.moderndev.polyglot.ui.screens.newsDetail

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.NavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.jetpack.composeadmobads.addInterstitialCallbacks
import com.jetpack.composeadmobads.loadInterstitial
import com.jetpack.composeadmobads.showInterstitial
import com.moderndev.polyglot.R
import com.moderndev.polyglot.model.FeedDetail
import com.moderndev.polyglot.model.NewsDetail
import com.moderndev.polyglot.model.Word
import com.moderndev.polyglot.ui.SharedViewModel
import com.moderndev.polyglot.ui.components.BackHandler
import com.moderndev.polyglot.ui.components.SelectableText
import com.moderndev.polyglot.ui.components.feedDetail.news.LoadingNews
import com.moderndev.polyglot.ui.components.feedDetail.news.WordActionMenu
import com.moderndev.polyglot.ui.components.feedDetail.news.WordDefinition
import com.moderndev.polyglot.ui.navigation.PolyglotScreens
import com.moderndev.polyglot.ui.theme.Typography
import com.moderndev.polyglot.util.DataStoreUtils
import com.moderndev.polyglot.util.UIState
import com.skydoves.landscapist.CircularReveal
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.launch


@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@OptIn(ExperimentalPagerApi::class)
@Composable
fun NewsDetailScreen(
    viewModel: NewsDetailViewModel,
    sharedViewModel: SharedViewModel,
    navController: NavController,
    newsId: String,
    newsUrl: String
) {


    val newsDetailUIState: UIState<FeedDetail<NewsDetail>> by viewModel.newsDetailUIState
    var newsDetail by remember { mutableStateOf(newsDetailUIState.value) }
    val wordDefinitionUIState: UIState<Word> by viewModel.wordDefinitionUIState
    var wordDefinition by remember { mutableStateOf(wordDefinitionUIState.value) }
    var currentFocusWord: String? by remember { mutableStateOf(wordDefinition?.value) }
    var loading by remember { mutableStateOf(false) }
    val focusMode by viewModel.focusMode
    val isFindingDefinition by viewModel.isFindingDefinition

    val coroutineScope = rememberCoroutineScope()
    val selectableTextRange = remember { mutableStateOf<TextRange?>(null) }
    val articleScrollState = rememberScrollState()
    val context = LocalContext.current
    val bottomSheetScaffoldState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden
    )

    loadInterstitial(context)
    showInterstitial(context)


    LaunchedEffect(true) {
        if (newsDetailUIState !is UIState.Loaded) {
            viewModel.getNewsDetail(
                DataStoreUtils.getAccessTokenFromDataStore(context),
                newsId,
                newsUrl.replace("<", "/")
            )
        }

    }

    LaunchedEffect(newsDetailUIState) {
        when (newsDetailUIState) {
            is UIState.Initial -> {

            }
            is UIState.Loading -> {
                loading = true

            }
            is UIState.Error -> {
                loading = false
                Toast.makeText(context, newsDetailUIState.errorMsg, Toast.LENGTH_SHORT).show()
            }
            is UIState.Loaded -> {
                loading = false
                newsDetail = newsDetailUIState.value
            }


        }
    }
    LaunchedEffect(bottomSheetScaffoldState.isVisible) {
        if (!bottomSheetScaffoldState.isVisible) {
            //When the bottom sheet is closed
            viewModel.setFocusMode(false)
            viewModel.setIsFindingDefinition(false)
        }
    }


    LaunchedEffect(wordDefinitionUIState) {
        when (wordDefinitionUIState) {
            is UIState.Initial -> {

            }
            is UIState.Loading -> {

            }
            is UIState.Error -> {
                Toast.makeText(context, wordDefinitionUIState.errorMsg, Toast.LENGTH_SHORT).show()
            }
            is UIState.Loaded -> {
                wordDefinition = wordDefinitionUIState.value
            }

        }
    }

    ModalBottomSheetLayout(

        sheetShape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
        sheetContent = {
            if (isFindingDefinition) {
                WordDefinition(
                    word = wordDefinition,
                    isLoading = wordDefinitionUIState is UIState.Loading,
                    onBackClick = {
                        viewModel.setIsFindingDefinition(false)
                    },
                    onDetailClick = {
                        navController.navigate("${PolyglotScreens.WordDetail.route}/${wordDefinition!!.value}/${sharedViewModel.currentPickedLanguage.value?.id}")
                    }
                )
            } else {
                WordActionMenu(
                    word = currentFocusWord ?: "",
                    onLookUpDefinition = { word ->
                        coroutineScope.launch {
                            viewModel.getWordDefinition(
                                DataStoreUtils.getAccessTokenFromDataStore(
                                    context
                                ),
                                word,
                                sharedViewModel.currentPickedLanguage.value?.id
                            )
                            viewModel.setIsFindingDefinition(true)

                        }
                    },
                    onLookUpImages = { word ->
                        val openURL = Intent(Intent.ACTION_VIEW)
                        openURL.data =
                            Uri.parse("http://images.google.com/images?um=1&hl=en&safe=active&nfpr=1&q=${word}")
                        startActivity(context, openURL, null)

                    }
                )
            }

        },
        sheetState = bottomSheetScaffoldState,
    ) {
        BackHandler(onBack = {
            viewModel.resetState()
            navController.popBackStack()
        })
        Scaffold(
            backgroundColor = Color.White, modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()

        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .verticalScroll(articleScrollState),

                ) {
                coroutineScope.launch {
                    articleScrollState.scrollTo(viewModel.articleScrollPosition)
                }

                Spacer(modifier = Modifier.padding(5.dp))

                IconButton(
                    onClick = {
                        viewModel.resetState()
                        navController.popBackStack()
                    }
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.back_32_black),
                        contentDescription = "Back icon"
                    )

                }



                if (loading) {
                    LoadingNews()
                } else {
                    Text(text = newsDetail?.title ?: "", style = Typography.h3)
                    Spacer(modifier = Modifier.padding(2.dp))
                    Box(Modifier.fillMaxWidth()) {
                        Column {
                            Text(
                                text = newsDetail?.content?.source ?: "",
                                style = Typography.subtitle2
                            )
                            Text(
                                text = newsDetail?.content?.author ?: "",
                                style = Typography.subtitle2
                            )
                        }
                        Text(
                            text = newsDetail?.content?.publishedDate ?: "",
                            style = Typography.subtitle2,
                            modifier = Modifier.align(
                                Alignment.TopEnd
                            )
                        )
                    }

                    Log.d("NewsDetailScreen", "url: ${newsUrl}")

                    Spacer(modifier = Modifier.padding(5.dp))

                    GlideImage(
                        imageModel = newsDetail?.thumbnail ?: "",
                        contentScale = ContentScale.FillWidth,
                        circularReveal = CircularReveal(duration = 250),
//                        placeHolder = ImageBitmap.imageResource(id = R.drawable.cat_loading_icon),
                        error = ImageBitmap.imageResource(R.drawable.image_loading_error),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(15.dp))
                    )

                    Spacer(modifier = Modifier.padding(10.dp))
                    newsDetail?.content?.value?.let {
                        SelectableText(
                            text = it,
//                            textRange = selectableTextRange,
                            isFocusing = focusMode,
                            onLongClick = { word ->
                                if (word != "") {
                                    Log.d(
                                        "NewsDetailScreen",
                                        "scroll position: ${articleScrollState.value}"
                                    )
                                    viewModel.updateArticleScrollPosition(articleScrollState.value)
                                    viewModel.setFocusMode(true)
                                    currentFocusWord = word
                                    coroutineScope.launch {
                                        bottomSheetScaffoldState.show()
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    }

}


