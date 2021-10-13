package com.nguyen.polyglot.ui.screens.newsDetail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Scaffold
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
import com.nguyen.polyglot.util.DataStoreUtils
import com.nguyen.polyglot.util.UIState

@Composable
fun VideoDetailScreen(navController: NavController, viewModel: VideoDetailViewModel, sharedViewModel: SharedViewModel, videoId: String) {

    val videoSubtitleUIState by viewModel.videoSubtitleUIState
    var videoSubtitle by remember { mutableStateOf(videoSubtitleUIState.value) }

    val context = LocalContext.current

    LaunchedEffect(true){
        viewModel.getVideoSubtitle( DataStoreUtils.getAccessTokenFromDataStore(context), videoId)
    }

    LaunchedEffect(videoSubtitleUIState){
        when(videoSubtitleUIState){
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

    Scaffold(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight(),
        backgroundColor = Color.White
    ) {
        Column {
            VideoPlayer(videoId)
        }
    }
}