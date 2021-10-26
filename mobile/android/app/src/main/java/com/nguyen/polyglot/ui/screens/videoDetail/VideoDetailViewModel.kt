package com.nguyen.polyglot.ui.screens.videoDetail

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nguyen.polyglot.model.SubtitlePart
import com.nguyen.polyglot.model.Word
import com.nguyen.polyglot.repo.FeedRepository
import com.nguyen.polyglot.util.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VideoDetailViewModel
@Inject constructor(
    private val feedRepo: FeedRepository,
) : ViewModel() {


    private val _videoSubtitleUIState: MutableState<UIState<List<SubtitlePart>>> =
        mutableStateOf(UIState.Initial(null))
    val videoSubtitleUIState: State<UIState<List<SubtitlePart>>> = _videoSubtitleUIState

    private val _wordDefinitionUIState: MutableState<UIState<Word>> =
        mutableStateOf(UIState.Initial(null))
    val wordDefinitionUIState: State<UIState<Word>> = _wordDefinitionUIState

    var startSecond = 0f
    var currentSubtitleIndex = 0
    var focusSubtitlePart: SubtitlePart? = null


    fun getVideoSubtitle(
        accessToken: String?,
        videoId: String?,
        language: String?,
        translatedLanguage: String?
    ) {
        if (accessToken == null) {
            _videoSubtitleUIState.value = UIState.Error("Please login first!")
            return
        }

        if (videoId == null) {
            _videoSubtitleUIState.value =
                UIState.Error("Couldn't find subtitle for this video!")
            return
        }

        if (language == null) {
            _videoSubtitleUIState.value =
                UIState.Error("Need language to get subtitles!")
            return
        }

        if (translatedLanguage == null) {
            _videoSubtitleUIState.value =
                UIState.Error("Need native language to get subtitles!")
            return
        }

        viewModelScope.launch {
            feedRepo.getVideoSubtitle(
                accessToken = accessToken,
                videoId = videoId,
                language = language,
                translatedLanguage = translatedLanguage
            ).collectLatest {
                _videoSubtitleUIState.value = it
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


    fun updateStartSecond(second: Float) {
        startSecond = second
    }

    fun getVideoStartSecond() = startSecond


    fun updateSubtitleIndex(index: Int) {
        currentSubtitleIndex = index
    }

    fun getSubtitleIndex() = currentSubtitleIndex

    fun resetState() {
        startSecond = 0f
        currentSubtitleIndex = 0
        _videoSubtitleUIState.value = UIState.Initial(null)
        _wordDefinitionUIState.value = UIState.Initial(null)
    }


}