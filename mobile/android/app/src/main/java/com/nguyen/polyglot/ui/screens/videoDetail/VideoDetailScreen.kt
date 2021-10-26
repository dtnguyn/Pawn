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
import com.nguyen.polyglot.model.Word
import com.nguyen.polyglot.ui.components.BackHandler
import com.nguyen.polyglot.ui.components.feedDetail.news.WordActionMenu
import com.nguyen.polyglot.ui.components.feedDetail.video.FocusSubtitleMenu
import com.nguyen.polyglot.ui.components.feedDetail.video.SubtitleBox
import com.nguyen.polyglot.ui.navigation.PolyglotScreens
import com.nguyen.polyglot.util.DataStoreUtils
import com.nguyen.polyglot.util.UIState
import com.nguyen.polyglot.util.UtilFunctions.reformatString
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import io.ktor.utils.io.concurrent.*
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
    var currentSecond by remember { mutableStateOf(viewModel.startSecond) }


    var focusSubtitlePart: SubtitlePart? by remember { mutableStateOf(viewModel.focusSubtitlePart) }

    val currentPickedLanguage by sharedViewModel.currentPickedLanguage


    val listState = rememberLazyListState()

    val currentLanguage by sharedViewModel.currentPickedLanguage

    val context = LocalContext.current

    val modalBottomSheetState = rememberModalBottomSheetState(
        initialValue = if (viewModel.focusSubtitlePart != null) ModalBottomSheetValue.Expanded else ModalBottomSheetValue.Hidden
    )
    val coroutineScope = rememberCoroutineScope()


    val playerView = YouTubePlayerView(context)
    var player: YouTubePlayer? by remember { mutableStateOf(null) }



    LaunchedEffect(true) {
        if (videoSubtitleUIState !is UIState.Loaded) {
            viewModel.getVideoSubtitle(
                DataStoreUtils.getAccessTokenFromDataStore(context),
                videoId,
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
                                subtitlePart = subtitleParts[index],
                                isTranslated = true,
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