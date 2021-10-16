package com.nguyen.polyglot.ui.components

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.PlayerView
import androidx.compose.runtime.*
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

import androidx.annotation.NonNull

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener




@Composable
fun VideoPlayer(videoId: String, start: Float, player: YouTubePlayer?, onPlaying: (currentSecond: Float) -> Unit) {

//
//    val context = LocalContext.current
//    val playerView = YouTubePlayerView(context)
//
//    playerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
//        override fun onReady(youTubePlayer: YouTubePlayer) {
//            player = youTubePlayer
//            youTubePlayer.loadVideo(videoId, start)
//        }
//
//        override fun onCurrentSecond(youTubePlayer: YouTubePlayer, second: Float) {
//            Log.d("VideoPlayer", "Pause video $pause")
//
//            if(pause){
//              test?.pause()
//            }
//            onPlaying(second)
//        }
//
//    })
//
//    AndroidView(factory = {
//        playerView
//    })
}