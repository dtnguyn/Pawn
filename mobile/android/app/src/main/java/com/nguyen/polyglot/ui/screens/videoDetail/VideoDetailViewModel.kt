package com.nguyen.polyglot.ui.screens.videoDetail

import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nguyen.polyglot.model.SubtitlePart
import com.nguyen.polyglot.repo.FeedRepository
import com.nguyen.polyglot.util.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VideoDetailViewModel
@Inject constructor(
    private val feedRepo: FeedRepository,
) : ViewModel() {


    private val _videoSubtitleMutableState: MutableState<UIState<List<SubtitlePart>>> = mutableStateOf(UIState.Initial(null))
    val videoSubtitleUIState: State<UIState<List<SubtitlePart>>> = _videoSubtitleMutableState

    var startSecond = 0f
    var currentSubtitleIndex = 0


    fun getVideoSubtitle(accessToken: String?, videoId: String?, language: String?) {
        if (accessToken == null) {
            _videoSubtitleMutableState.value = UIState.Error("Please login first!")
            return
        }

        if (videoId == null) {
            _videoSubtitleMutableState.value =
                UIState.Error("Couldn't find subtitle for this video!")
            return
        }

        if (language == null) {
            _videoSubtitleMutableState.value =
                UIState.Error("Need language to get subtitles!")
            return
        }

        viewModelScope.launch {
            feedRepo.getVideoSubtitle(accessToken = accessToken, videoId = videoId, language = language).collectLatest {
                _videoSubtitleMutableState.value = it
            }
        }
    }


    fun updateStartSecond(second: Float){
        startSecond = second
    }

    fun getVideoStartSecond() = startSecond


    fun updateSubtitleIndex(index: Int){
        currentSubtitleIndex = index
    }

    fun getSubtitleIndex() = currentSubtitleIndex

    fun resetState() {

    }


}