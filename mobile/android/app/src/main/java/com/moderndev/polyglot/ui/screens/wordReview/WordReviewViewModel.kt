package com.moderndev.polyglot.ui.screens.wordReview

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moderndev.polyglot.model.ReviewQuestion
import com.moderndev.polyglot.repo.WordReviewRepository
import com.moderndev.polyglot.util.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WordReviewViewModel
@Inject constructor(private val repo: WordReviewRepository) : ViewModel() {

    private val _questionsUIState: MutableState<UIState<List<ReviewQuestion>>> =
        mutableStateOf(UIState.Initial(null))
    val questionsUIState: State<UIState<List<ReviewQuestion>>> = _questionsUIState

    val currentQuestionIndex = mutableStateOf(0)
    val wrongAnswerCount = mutableStateOf(0)
    val correctAnswerCount = mutableStateOf(0)

    fun getQuickQuestions(language: String) {
        viewModelScope.launch {
            repo.getReviewQuestions(language, 10).collectLatest {
                _questionsUIState.value = it
            }
        }
    }

    fun getFullQuestions(language: String) {
        viewModelScope.launch {
            repo.getReviewQuestions(language,).collectLatest {
                _questionsUIState.value = it
            }
        }
    }

    fun incrementQuestionIndex() {
        if (currentQuestionIndex.value < questionsUIState.value.value?.size?.minus(1) ?: 0) {
            currentQuestionIndex.value += 1
        }
    }
    fun decrementQuestionIndex() {
        if (currentQuestionIndex.value > 0) {
            currentQuestionIndex.value -= 1
        }
    }
    fun checkAnswer(wordOfQuestion: String, answer: String) {
        val questions = questionsUIState.value.value
        questions?.let { questionList ->
            val updatedList = questionList.map {
                if (it.word.value == wordOfQuestion) {
                    if(it.correctAnswer != answer) wrongAnswerCount.value += 1
                    else correctAnswerCount.value += 1
                     ReviewQuestion(
                        word = it.word,
                        question = it.question,
                        answerOptions = it.answerOptions,
                        correctAnswer = it.correctAnswer,
                        userAnswer = answer,
                        type = it.type
                    )
                } else it
            }
            _questionsUIState.value = UIState.Loaded(updatedList)
        }
    }


    fun resetState(){
        _questionsUIState.value = UIState.Initial(null)
        currentQuestionIndex.value = 0
        wrongAnswerCount.value = 0
        correctAnswerCount.value = 0
    }


}