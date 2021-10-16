package com.nguyen.polyglot.ui.screens.newsDetail

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.nguyen.polyglot.ui.SharedViewModel
import com.nguyen.polyglot.ui.components.VideoPlayer
import com.nguyen.polyglot.ui.screens.videoDetail.VideoDetailViewModel
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.nguyen.polyglot.model.SubtitlePart
import com.nguyen.polyglot.ui.components.BackHandler
import com.nguyen.polyglot.ui.components.feedDetail.video.SubtitleBox
import com.nguyen.polyglot.ui.theme.Typography
import com.nguyen.polyglot.util.DataStoreUtils
import com.nguyen.polyglot.util.UIState
import com.nguyen.polyglot.util.UtilFunctions.reformatString
import io.ktor.http.*

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

    var listState = rememberLazyListState()

    val context = LocalContext.current

    fun getCurrentPart(): String {
        if (currentSecond >= videoSubtitle!![currentIndex].end) {
            var temp = currentSecond
            while (temp >= videoSubtitle!![currentIndex].end) {
                currentIndex++
            }
        }
        return reformatString(videoSubtitle!![currentIndex].text ?: "")
    }

    fun getPreviousPart(): String? {
        if (currentSecond >= videoSubtitle!![currentIndex].end) {
            var temp = currentSecond
            while (temp >= videoSubtitle!![currentIndex].end) {
                currentIndex++
            }
        }
        return reformatString(videoSubtitle!![currentIndex].text ?: "")
    }

    fun getNextPart(): String? {
        if (currentSecond >= videoSubtitle!![currentIndex].end) {
            var temp = currentSecond
            while (temp >= videoSubtitle!![currentIndex].end) {
                currentIndex++
            }
        }
        return reformatString(videoSubtitle!![currentIndex].text ?: "")
    }

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
                var temp = currentSecond
                var tempIndex = currentIndex
                while (temp >= videoSubtitle!![tempIndex].end) {
                    tempIndex++
                }
                currentIndex = tempIndex
            }
        }

    }

    LaunchedEffect(currentIndex) {
        if (videoSubtitle != null)
            listState.scrollToItem(currentIndex)
    }

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
                VideoPlayer(
                    videoId,
                    start = viewModel.getVideoStartSecond(),
                    onPlaying = { second ->
                        currentSecond = second
                    }
                )
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
                            subtitleText = subtitleParts[index].text
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