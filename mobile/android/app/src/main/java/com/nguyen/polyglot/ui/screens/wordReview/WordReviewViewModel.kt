package com.nguyen.polyglot.ui.screens.wordReview

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nguyen.polyglot.model.Language
import com.nguyen.polyglot.model.ReviewQuestion
import com.nguyen.polyglot.repo.WordReviewRepository
import com.nguyen.polyglot.util.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WordReviewViewModel
@Inject constructor(private val repo: WordReviewRepository) : ViewModel() {

    private val _quickQuestionsUIState: MutableState<UIState<List<ReviewQuestion>>> =
        mutableStateOf(UIState.Initial(null))
    val quickQuestionsUIState: State<UIState<List<ReviewQuestion>>> = _quickQuestionsUIState

    val currentQuestionIndex = mutableStateOf(0)

    fun getQuickQuestions(language: String) {
        viewModelScope.launch {
            repo.getQuickReviewQuestions(language).collectLatest {
                _quickQuestionsUIState.value = it
            }
        }
    }

    fun incrementQuestionIndex() {
        if (currentQuestionIndex.value < quickQuestionsUIState.value.value?.size?.minus(1) ?: 0) {
            currentQuestionIndex.value += 1
        }
    }

    fun checkAnswer(question: String, answer: String) {
        val questions = quickQuestionsUIState.value.value
        questions?.let { questionList ->
            val updatedList = questionList.map {
                if (it.question == question) {
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
            _quickQuestionsUIState.value = UIState.Loaded(updatedList)
        }
    }


}