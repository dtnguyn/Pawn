package com.moderndev.polyglot.ui.screens.newsDetail

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moderndev.polyglot.model.FeedDetail
import com.moderndev.polyglot.model.NewsDetail
import com.moderndev.polyglot.model.Word
import com.moderndev.polyglot.repo.FeedRepository
import com.moderndev.polyglot.util.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsDetailViewModel
@Inject constructor(
    private val feedRepo: FeedRepository,
) : ViewModel() {

    private val _newsDetailUIState: MutableState<UIState<FeedDetail<NewsDetail>>> =
        mutableStateOf(UIState.Initial(null))
    val newsDetailUIState: State<UIState<FeedDetail<NewsDetail>>> = _newsDetailUIState

    private val _wordDefinitionUIState: MutableState<UIState<Word>> =
        mutableStateOf(UIState.Initial(null))
    val wordDefinitionUIState: State<UIState<Word>> = _wordDefinitionUIState

    var articleScrollPosition: Int = 0

    private val _isFindingDefinition: MutableState<Boolean> = mutableStateOf(false)
    val isFindingDefinition: State<Boolean> = _isFindingDefinition

    private val _focusMode: MutableState<Boolean> = mutableStateOf(false)
    val focusMode: State<Boolean> = _focusMode

    fun getNewsDetail(accessToken: String?, newsUrl: String, newsId: String) {

        if (accessToken == null) {
            _newsDetailUIState.value = UIState.Error("Please login first!")
            return
        }

        viewModelScope.launch {
            feedRepo.getNewsDetail(accessToken, newsUrl, newsId).collectLatest {
                _newsDetailUIState.value = it
            }
        }
    }

    fun getWordDefinition(accessToken: String?, wordValue: String?, language: String?) {
        viewModelScope.launch {

            if (accessToken == null) {
                _wordDefinitionUIState.value = UIState.Error("Please login first!")
                return@launch
            }

            if (wordValue == null || language == null) {
                _wordDefinitionUIState.value = UIState.Error("No definition found!")
                return@launch
            }



            feedRepo.getWordDefinition(accessToken, wordValue, language).collect {
                _wordDefinitionUIState.value = it
            }
        }
    }


    fun updateArticleScrollPosition(position: Int) {
        articleScrollPosition = position
    }


    fun resetState() {
        _wordDefinitionUIState.value = UIState.Initial(null)
        _newsDetailUIState.value = UIState.Initial(null)
        articleScrollPosition = 0
        _focusMode.value = false
        _isFindingDefinition.value = false
    }

    fun setFocusMode(status: Boolean) {
        _focusMode.value = status
    }

    fun setIsFindingDefinition(status: Boolean) {
        _isFindingDefinition.value = status
    }
}