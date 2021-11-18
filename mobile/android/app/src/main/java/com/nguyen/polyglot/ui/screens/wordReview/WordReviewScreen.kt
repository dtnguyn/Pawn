package com.nguyen.polyglot.ui.screens.wordReview

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Alignment.Companion.End
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
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

    val quickQuestionsUIState by viewModel.quickQuestionsUIState
    var quickQuestions by remember { mutableStateOf(quickQuestionsUIState.value) }

    val currentLanguage by sharedViewModel.currentPickedLanguage

    val currentQuestionIndex by viewModel.currentQuestionIndex
    var currentChoice by remember { mutableStateOf<String?>(null) }


    fun answerColor(question: ReviewQuestion, answer: String): Color {
        if (question.userAnswer == null) {
            //Answer is not checked yet
            if (currentChoice != null && currentChoice == answer)
                return Blue

        } else {
            // Answer is checked
            if (answer == question.correctAnswer)
                return LightGreen
            else if (currentChoice == answer) return LightRed
        }
        return LightGrey

    }

    LaunchedEffect(true) {
        if (currentLanguage != null) {
            viewModel.getQuickQuestions("en_US")
        }
    }

    LaunchedEffect(quickQuestionsUIState) {
        when (quickQuestionsUIState) {

            is UIState.Initial -> {

            }
            is UIState.Loading -> {

            }
            is UIState.Error -> {

            }
            is UIState.Loaded -> {
                quickQuestions = quickQuestionsUIState.value
                Log.d("WordReviewScreen", "questions: ${quickQuestionsUIState.value?.size}")
            }
        }
    }

    LaunchedEffect(currentQuestionIndex) {
        Log.d("WordReviewScreen", "value: ${currentQuestionIndex}")
    }

    Scaffold(backgroundColor = Color.White) {
        if (quickQuestions == null) {

        } else {
            Column(Modifier.padding(20.dp)) {
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
                                    .fillMaxWidth(0.95f)
                                    .height(10.dp)
                                    .background(LightRed, RoundedCornerShape(10.dp))
                            )
                            Box(
                                Modifier
                                    .fillMaxWidth(0.8f)
                                    .height(10.dp)
                                    .background(LightGreen, RoundedCornerShape(10.dp))
                            )

                        }
                        Spacer(Modifier.padding(5.dp))
                        Text(
                            text = "${currentQuestionIndex + 1}/${quickQuestions?.size}",
                            style = Typography.h6
                        )

                    }
                }
                Spacer(modifier = Modifier.padding(20.dp))
                Text(
                    text = quickQuestions!![currentQuestionIndex].question,
                    style = Typography.h3
                )
                Spacer(modifier = Modifier.padding(20.dp))

                quickQuestions!![currentQuestionIndex].answerOptions.forEach {


                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(5f),
                        colors = ButtonDefaults.buttonColors(
                            answerColor(
                                quickQuestions!![currentQuestionIndex],
                                it
                            )
                        ),
                        shape = RoundedCornerShape(15.dp),
                        onClick = { currentChoice = it }) {
                        Text(
                            text = it,
                            style = Typography.h6,
                        )
                    }
                    Spacer(modifier = Modifier.padding(5.dp))
                }

                Spacer(modifier = Modifier.padding(10.dp))

                if (quickQuestions!![currentQuestionIndex].userAnswer == null) {

                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(5f),
                        colors = ButtonDefaults.buttonColors(LightOrange),
                        shape = RoundedCornerShape(15.dp),
                        onClick = {
                            if (currentChoice != null) {
                                viewModel.checkAnswer(
                                    quickQuestions!![currentQuestionIndex].question,
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
                                currentChoice = null
                                viewModel.incrementQuestionIndex()
                            })
                    }

                }
            }
        }

    }
}


