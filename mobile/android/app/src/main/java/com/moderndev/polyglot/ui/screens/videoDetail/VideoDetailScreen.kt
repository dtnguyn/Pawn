package com.moderndev.polyglot.ui.screens.newsDetail

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.moderndev.polyglot.ui.SharedViewModel
import com.moderndev.polyglot.ui.screens.videoDetail.VideoDetailViewModel
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.jetpack.composeadmobads.loadInterstitial
import com.jetpack.composeadmobads.showInterstitial
import com.moderndev.polyglot.R
import com.moderndev.polyglot.model.SubtitlePart
import com.moderndev.polyglot.ui.components.BackHandler
import com.moderndev.polyglot.ui.components.CustomDialog
import com.moderndev.polyglot.ui.components.feedDetail.video.FocusSubtitleMenu
import com.moderndev.polyglot.ui.components.feedDetail.video.SubtitleBox
import com.moderndev.polyglot.ui.navigation.PolyglotScreens
import com.moderndev.polyglot.ui.theme.Typography
import com.moderndev.polyglot.util.DataStoreUtils
import com.moderndev.polyglot.util.UIState
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import io.ktor.utils.io.concurrent.*
import kotlinx.coroutines.launch

@ExperimentalFoundationApi
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun VideoDetailScreen(
    navController: NavController,
    viewModel: VideoDetailViewModel,
    sharedViewModel: SharedViewModel,
    videoId: String
) {


    val videoSubtitleUIState by viewModel.videoSubtitleUIState
    var videoSubtitle by remember { mutableStateOf(videoSubtitleUIState.value) }

    var user by remember { mutableStateOf(sharedViewModel.authStatusUIState.value.value?.user) }
    var showPremiumDialog by remember { mutableStateOf(false) }

    var currentIndexState by remember { mutableStateOf(viewModel.currentSubtitleIndex) }
    var currentIndex = viewModel.currentSubtitleIndex
    var currentSecond = viewModel.startSecond


    var focusSubtitlePart: SubtitlePart? by remember { mutableStateOf(viewModel.focusSubtitlePart) }


    var isTranslated by remember { mutableStateOf(false) }

    val listState = rememberLazyListState()

    val context = LocalContext.current

    val modalBottomSheetState = rememberModalBottomSheetState(
        initialValue = if (viewModel.focusSubtitlePart != null) ModalBottomSheetValue.Expanded else ModalBottomSheetValue.Hidden
    )
    val coroutineScope = rememberCoroutineScope()


    val playerView = YouTubePlayerView(context)
    var player: YouTubePlayer? by remember { mutableStateOf(null) }

    loadInterstitial(context)
    showInterstitial(context)

    LaunchedEffect(true) {
        if (videoSubtitleUIState !is UIState.Loaded) {
            viewModel.getVideoSubtitle(
                DataStoreUtils.getAccessTokenFromDataStore(context),
                videoId
                ,
                sharedViewModel.currentPickedLanguage.value?.id,
                sharedViewModel.authStatusUIState.value.value?.user?.nativeLanguageId
            )
        }
    }



    LaunchedEffect(videoSubtitleUIState) {
        when (videoSubtitleUIState) {
            is UIState.Initial -> {

            }
            is UIState.Loading -> {

            }
            is UIState.Error -> {
                Toast.makeText(context, videoSubtitleUIState.errorMsg, Toast.LENGTH_SHORT).show()
            }
            is UIState.Loaded -> {
                Log.d("VideoDetailScreen", "videoSubtitle value ${videoSubtitleUIState.value}")
                videoSubtitle = videoSubtitleUIState.value
            }
        }
    }

    LaunchedEffect(currentIndexState) {
        if (videoSubtitle != null)
            listState.scrollToItem(currentIndexState)
    }




    ModalBottomSheetLayout(
        sheetShape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
        sheetContent = {
            focusSubtitlePart?.let {
                FocusSubtitleMenu(
                    isOpen = modalBottomSheetState.isVisible,
                    subtitlePart = focusSubtitlePart,
                    viewModel = viewModel,
                    sharedViewModel = sharedViewModel,
                    moveToWordDetail = {
                        viewModel.updateSubtitleIndex(currentIndex)
                        viewModel.updateStartSecond(currentSecond)
                        viewModel.focusSubtitlePart = focusSubtitlePart
                        navController.navigate("${PolyglotScreens.WordDetail.route}/${it}/${sharedViewModel.currentPickedLanguage.value?.id}")
                    }
                )
            }
            Text(text = "")
        },
        sheetState = modalBottomSheetState,
    ) {
        BackHandler(onBack = {
            viewModel.resetState()
            navController.popBackStack()
        })
        Scaffold(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            backgroundColor = Color.White
        ) {
            BackHandler(onBack = {
                viewModel.resetState()
                viewModel.updateStartSecond(currentSecond)
                viewModel.updateSubtitleIndex(currentIndex)
                navController.popBackStack()
            })

            Column {
                videoSubtitle?.let { subtitleParts ->
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .padding(5.dp)
                    ) {
                        IconButton(
                            modifier = Modifier.align(Alignment.CenterStart),
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



                        Row(modifier = Modifier.align(Alignment.CenterEnd)) {
                            Text(text = stringResource(id = R.string.translated), style = Typography.body1)

                            Switch(
                                checked = isTranslated,
                                onCheckedChange = {
                                    if(user?.isPremium == true){
                                        isTranslated = it
                                    } else {
                                        player?.pause()
                                        showPremiumDialog = true
                                    }
                                }
                            )
                        }
                    }


                    AndroidView(factory = {
                        playerView.addYouTubePlayerListener(object :
                            AbstractYouTubePlayerListener() {
                            override fun onReady(youTubePlayer: YouTubePlayer) {
                                player = youTubePlayer
                                youTubePlayer.loadVideo(
                                    videoId,
                                    viewModel.getVideoStartSecond()
                                )
                            }

                            override fun onCurrentSecond(
                                youTubePlayer: YouTubePlayer,
                                second: Float
                            ) {
                              currentSecond = second
                                if (second >= subtitleParts[currentIndex].end && currentIndex != subtitleParts.size - 1) {
                                    //This check if 2 subtitle parts have a time gap
                                    if (subtitleParts[currentIndex + 1].start > second) return
                                    val tempSec = second
                                    var tempIndex = currentIndex
                                    while (tempSec >= subtitleParts[tempIndex].end && tempIndex < subtitleParts.size - 1) {
                                        tempIndex++
                                    }
                                    if (tempIndex >= 0 && tempIndex < subtitleParts.size) {
                                        currentIndex = tempIndex
                                        currentIndexState = tempIndex
                                    }
                                } else if (second < subtitleParts[currentIndex].start && currentIndex != 0) {
                                    val tempSec = second
                                    var tempIndex = currentIndex
                                    while (tempSec < subtitleParts[tempIndex].start && tempIndex != 0) {
                                        tempIndex--
                                    }
                                    if (tempIndex >= 0 && tempIndex < subtitleParts.size) {
                                        currentIndex = tempIndex
                                        currentIndexState = tempIndex
                                    }
                                }
                            }
                        })
                        playerView
                    })
                    LazyColumn(
                        state = listState
                    ) {
                        item {
                            Spacer(modifier = Modifier.padding(20.dp))
                        }

                        items(subtitleParts.size) { index ->
                            SubtitleBox(
                                selected = currentIndexState == index,
                                subtitlePart = subtitleParts[index],
                                isTranslated = isTranslated && currentIndexState == index,
                                onClick = {
                                    player?.seekTo(subtitleParts[index].start)
                                },
                                onLongClick = {
                                    player?.pause()
                                    focusSubtitlePart = subtitleParts[index]
                                    coroutineScope.launch {
                                        modalBottomSheetState.show()
                                    }
                                }
                            )
                        }

                        item {
                            Spacer(modifier = Modifier.padding(20.dp))
                        }
                    }
                }
            }

        }

        if(showPremiumDialog)
            CustomDialog(
                title = "Premium Plan Required",
                content = "This feature requires Premium plan! You can go to the App Settings and buy the Premium plan to unlock this feature.",
                icon = R.drawable.ic_lock_red_32,
                onDismissText = "Go Premium",
                onDismiss = {
                    showPremiumDialog = false
                },
                onAction = {
                    player?.pause()
                    showPremiumDialog = false
                    navController.navigate("setting")
                }
            )
    }


}