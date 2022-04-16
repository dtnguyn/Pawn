package com.moderndev.polyglot.ui.components

import androidx.compose.runtime.Composable
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer


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