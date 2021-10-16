package com.nguyen.polyglot.ui.screens.newsDetail

import android.content.Intent
import android.net.Uri
import android.util.Log
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
import com.nguyen.polyglot.ui.SharedViewModel
import com.nguyen.polyglot.ui.screens.videoDetail.VideoDetailViewModel
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.nguyen.polyglot.model.SubtitlePart
import com.nguyen.polyglot.ui.components.BackHandler
import com.nguyen.polyglot.ui.components.feedDetail.news.WordActionMenu
import com.nguyen.polyglot.ui.components.feedDetail.video.FocusSubtitleMenu
import com.nguyen.polyglot.ui.components.feedDetail.video.SubtitleBox
import com.nguyen.polyglot.util.DataStoreUtils
import com.nguyen.polyglot.util.UIState
import com.nguyen.polyglot.util.UtilFunctions.reformatString
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import kotlinx.coroutines.launch

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

    var currentIndex by remember { mutableStateOf(viewModel.currentSubtitleIndex) }
    var currentSecond by remember { mutableStateOf(0f) }

    var pauseVideo by remember { mutableStateOf(false) }

    var focusSubtitlePart: SubtitlePart? by remember { mutableStateOf(null) }
    var isFindingDefinition by remember { mutableStateOf(false) }
    var currentFocusWord: String? by remember { mutableStateOf(null) }


    var listState = rememberLazyListState()

    val currentLanguage by sharedViewModel.currentPickedLanguage

    val context = LocalContext.current

    val modalBottomSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden
    )
    val coroutineScope = rememberCoroutineScope()


    val playerView = YouTubePlayerView(context)
    var player: YouTubePlayer? by remember { mutableStateOf(null) }



    LaunchedEffect(true) {
        if (videoSubtitleUIState !is UIState.Loaded) {
            viewModel.getVideoSubtitle(
                DataStoreUtils.getAccessTokenFromDataStore(context),
                videoId,
                sharedViewModel.currentPickedLanguage.value?.value
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

            }
            is UIState.Loaded -> {
                videoSubtitle = videoSubtitleUIState.value
            }
        }
    }

    LaunchedEffect(currentSecond) {
        if (videoSubtitle != null) {
            if (currentSecond >= videoSubtitle!![currentIndex].end) {
                var tempSec = currentSecond
                var tempIndex = currentIndex
                while (tempSec >= videoSubtitle!![tempIndex].end && tempIndex < videoSubtitle!!.size - 1) {
                    tempIndex++
                }
                if (tempIndex >= 0 && tempIndex < videoSubtitle!!.size) currentIndex = tempIndex
            } else if (currentSecond < videoSubtitle!![currentIndex].start) {
                var tempSec = currentSecond
                var tempIndex = currentIndex
                while (tempSec < videoSubtitle!![tempIndex].start) {
                    Log.d("VideoDetailScreen", "start: ${videoSubtitle!![tempIndex].start}")
                    tempIndex--
                }
                if (tempIndex >= 0 && tempIndex < videoSubtitle!!.size) currentIndex = tempIndex

            }
        }

    }

    LaunchedEffect(currentIndex) {
        if (videoSubtitle != null)
            listState.scrollToItem(currentIndex)
    }

    ModalBottomSheetLayout(
        sheetShape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
        sheetContent = {
            focusSubtitlePart?.let {
                FocusSubtitleMenu(
                    subtitlePart = it,
                    mainLanguage = currentLanguage?.id ?: "",
                    onDismiss = {
                        focusSubtitlePart = null
                    },
                    onLongClick = {
                        currentFocusWord = it
//                                isFindingDefinition = true
                    }
                )
            }
            Text(text = "")
        },
        sheetState = modalBottomSheetState,
    ) {
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




//                    VideoPlayer(
//                        videoId,
//                        start = viewModel.getVideoStartSecond(),
//                        pause = pauseVideo,
//                        onPlaying = { second ->
//                            currentSecond = second
//                        }
//                    )
                    AndroidView(factory = {
                        playerView.addYouTubePlayerListener(object :
                            AbstractYouTubePlayerListener() {
                            override fun onReady(youTubePlayer: YouTubePlayer) {
                                player = youTubePlayer
                                youTubePlayer.loadVideo(videoId, viewModel.getVideoStartSecond())
                            }

                            override fun onCurrentSecond(
                                youTubePlayer: YouTubePlayer,
                                second: Float
                            ) {
                                currentSecond = second
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
                                selected = currentIndex == index,
                                mainLanguage = sharedViewModel.currentPickedLanguage.value?.value?.substring(
                                    0,
                                    2
                                ) ?: "",
                                subtitleText = reformatString(subtitleParts[index].text ?: ""),
                                onClick = {
                                    Log.d("VideoDetailScreen", "player: $player")
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
    }


}