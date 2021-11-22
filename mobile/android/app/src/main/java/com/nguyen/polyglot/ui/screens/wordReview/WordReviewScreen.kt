package com.nguyen.polyglot.ui.screens.wordReview

import android.media.MediaPlayer
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Alignment.Companion.End
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.nguyen.polyglot.R
import com.nguyen.polyglot.model.ReviewQuestion
import com.nguyen.polyglot.ui.SharedViewModel
import com.nguyen.polyglot.ui.components.RoundButton
import com.nguyen.polyglot.ui.theme.*
import com.nguyen.polyglot.util.UIState

@Composable
fun WordReviewScreen(
    viewModel: WordReviewViewModel,
    sharedViewModel: SharedViewModel,
    navController: NavController
) {

    val questionsUIState by viewModel.questionsUIState
    var questions by remember { mutableStateOf(questionsUIState.value) }

    val currentLanguage by sharedViewModel.currentPickedLanguage

    val currentQuestionIndex by viewModel.currentQuestionIndex
    val wrongAnswerCount by viewModel.wrongAnswerCount
    val correctAnswerCount by viewModel.correctAnswerCount
    var currentChoice by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current


    fun answerColor(question: ReviewQuestion, answer: String): Color {
        if (question.userAnswer == null) {
            //Answer is not checked yet
            if (currentChoice != null && currentChoice == answer)
                return Blue

        } else {
            // Answer is checked
            if (answer == question.correctAnswer)
                return LightGreen
            else if (question.userAnswer == answer) return LightRed
        }
        return LightGrey

    }

    LaunchedEffect(true) {
        if (currentLanguage != null) {
            viewModel.getQuickQuestions("en_US")
        }
    }

    LaunchedEffect(questionsUIState) {
        when (questionsUIState) {

            is UIState.Initial -> {

            }
            is UIState.Loading -> {

            }
            is UIState.Error -> {

            }
            is UIState.Loaded -> {
                questions = questionsUIState.value
                Log.d("WordReviewScreen", "questions: ${questionsUIState.value?.size}")
            }
        }
    }



    Scaffold(backgroundColor = Color.White) {
        if (questions == null) {

        } else {
            Column(
                Modifier
                    .padding(start = 20.dp, end = 20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(modifier = Modifier.padding(10.dp))
                IconButton(onClick = {
                    navController.popBackStack()
                    viewModel.resetState()
                }) {
                    Icon(Icons.Filled.Close, "")
                }
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    shape = RoundedCornerShape(20.dp),
                    backgroundColor = LightGrey,
                    elevation = 4.dp,

                    ) {
                    Row(
                        Modifier
                            .padding(10.dp)
                            .align(Alignment.CenterHorizontally)
                    ) {
                        Box(
                            Modifier
                                .fillMaxWidth(0.8f)
                                .height(10.dp)
                                .background(Grey, RoundedCornerShape(10.dp))
                                .align(CenterVertically)
                        ) {
                            Box(
                                Modifier
                                    .fillMaxWidth((((wrongAnswerCount + correctAnswerCount).toFloat() / questions!!.size)))
                                    .height(10.dp)
                                    .background(LightRed, RoundedCornerShape(10.dp))
                            )
                            Box(
                                Modifier
                                    .fillMaxWidth(((correctAnswerCount.toFloat() / questions!!.size)))
                                    .height(10.dp)
                                    .background(LightGreen, RoundedCornerShape(10.dp))
                            )

                        }
                        Spacer(Modifier.padding(5.dp))
                        Text(
                            text = "${currentQuestionIndex + 1}/${questions?.size}",
                            style = Typography.h6
                        )

                    }
                }
                Spacer(modifier = Modifier.padding(10.dp))
                if (questions!![currentQuestionIndex].question == "What is the definition of the word in the following audio?") {
                    Text(
                        text = questions!![currentQuestionIndex].question,
                        style = Typography.h3
                    )
                    Spacer(modifier = Modifier.padding(5.dp))
                    Box(modifier = Modifier.align(CenterHorizontally)) {
                        RoundButton(
                            backgroundColor = LightGrey,
                            size = 60.dp,
                            icon = R.drawable.speaker,
                            onClick = {
                                questions!![currentQuestionIndex].word.pronunciationAudio?.let {
                                    Log.d("WordReviewScreen", "audio: $it")
                                    val audioUrl = if ("http" in it) it else "https:$it"
                                    MediaPlayer.create(
                                        context,
                                        Uri.parse(audioUrl)
                                    ).start()
                                }

                            })
                    }

                } else {
                    Text(
                        text = questions!![currentQuestionIndex].question,
                        style = Typography.h3
                    )

                }
                Spacer(modifier = Modifier.padding(5.dp))

                if(currentQuestionIndex != 0){
                    Button(
                        onClick = { viewModel.decrementQuestionIndex() },
                        colors = ButtonDefaults.buttonColors(
                            LightGrey
                        ),
                        shape = RoundedCornerShape(30.dp),
                    ) {
                        Row(verticalAlignment = CenterVertically) {
                            Icon(Icons.Filled.ArrowBack, "")
                            Spacer(modifier = Modifier.padding(3.dp))
                            Text(text = "Back", style = Typography.subtitle1, fontSize = 16.sp)
                        }

                    }
                }



                Spacer(modifier = Modifier.padding(10.dp))

                questions!![currentQuestionIndex].answerOptions.forEach {
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(5f),
                        colors = ButtonDefaults.buttonColors(
                            answerColor(
                                questions!![currentQuestionIndex],
                                it
                            )
                        ),
                        shape = RoundedCornerShape(15.dp),
                        onClick = {
                            if (questions!![currentQuestionIndex].userAnswer == null)
                                currentChoice = it
                        }) {
                        Text(
                            text = it,
                            style = Typography.h6,
                        )
                    }
                    Spacer(modifier = Modifier.padding(5.dp))
                }

                Spacer(modifier = Modifier.padding(10.dp))

                if (questions!![currentQuestionIndex].userAnswer == null) {

                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(5f),
                        colors = ButtonDefaults.buttonColors(LightOrange),
                        shape = RoundedCornerShape(15.dp),
                        onClick = {
                            if (currentChoice != null) {
                                viewModel.checkAnswer(
                                    questions!![currentQuestionIndex].word.value,
                                    currentChoice!!
                                )
                            }
                        }) {
                        Text(
                            text = "Check Answer",
                            style = Typography.h5,
                        )
                    }
                } else {
                    Box(modifier = Modifier.align(End)) {
                        RoundButton(
                            backgroundColor = Blue,
                            size = 64.dp,
                            icon = R.drawable.arrow_white_128,
                            onClick = {
                                if (currentQuestionIndex < questions!!.size - 1) {
                                    currentChoice = null
                                } else {
                                    // Got to result screen
                                    navController.navigate("wordReviewResult") {
                                        popUpTo("home")
                                    }

                                }
                                viewModel.incrementQuestionIndex()
                            })
                    }

                }
                Spacer(modifier = Modifier.padding(10.dp))
            }
        }

    }
}


