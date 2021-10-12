package com.nguyen.polyglot.ui.screens.newsDetail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.nguyen.polyglot.ui.SharedViewModel
import com.nguyen.polyglot.ui.components.VideoPlayer

@Composable
fun VideoDetailScreen(navController: NavController, sharedViewModel: SharedViewModel, videoId: String) {
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