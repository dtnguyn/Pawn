package com.nguyen.polyglot.ui.screens.wordReview

import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.ImageLoader
import coil.compose.rememberImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import com.nguyen.polyglot.R
import com.nguyen.polyglot.ui.components.wordReview.WordReviewResultFailure
import com.nguyen.polyglot.ui.components.wordReview.WordReviewResultSuccess

@Composable
fun WordReviewResultScreen(navController: NavController, viewModel: WordReviewViewModel) {

    val numberOfQuestions by remember {
        mutableStateOf<Int>(
            viewModel.questionsUIState.value.value?.size ?: 1
        )
    }

    val numberOfCorrectAnswer by viewModel.correctAnswerCount


    val imageLoader = ImageLoader.invoke(LocalContext.current).newBuilder()
        .componentRegistry {
            if (Build.VERSION.SDK_INT >= 28) {
                add(ImageDecoderDecoder(LocalContext.current))
            } else {
                add(GifDecoder())
            }
        }.build()
    Scaffold(backgroundColor = Color.White) {
        Box(
            contentAlignment = Alignment.Center, modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {

            if (numberOfCorrectAnswer.toFloat() / numberOfQuestions >= 0.8) {
                Image(
                    painter = rememberImagePainter(
                        data = R.drawable.confetti_gif,
                        imageLoader = imageLoader
                    ),
                    contentScale = ContentScale.FillBounds,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                )
            }



            IconButton(
                onClick = {
                    navController.popBackStack()
                    viewModel.resetState()
                }, modifier = Modifier
                    .align(
                        Alignment.TopStart
                    )
                    .padding(10.dp)
            ) {
                Icon(Icons.Filled.Close, "")
            }

            if (numberOfCorrectAnswer.toFloat() / numberOfQuestions >= 0.8) {
                WordReviewResultSuccess(
                    score = "$numberOfCorrectAnswer/$numberOfQuestions",
                    scoreInPercentage = (numberOfCorrectAnswer.toFloat() / numberOfQuestions) * 100
                ) {
                    navController.navigate("wordReviewMenu") {
                        popUpTo("home")
                    }
                    viewModel.resetState()
                }
            } else {
                WordReviewResultFailure(score = "$numberOfCorrectAnswer/$numberOfQuestions") {
                    navController.navigate("wordReviewMenu") {
                        popUpTo("home")
                    }
                    viewModel.resetState()
                }
            }

        }
    }


}