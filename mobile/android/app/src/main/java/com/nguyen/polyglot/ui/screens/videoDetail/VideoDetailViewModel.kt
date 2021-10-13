package com.nguyen.polyglot.ui.screens.videoDetail

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
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


    fun getVideoSubtitle(accessToken: String?, videoId: String?) {
        if(accessToken == null){
            _videoSubtitleMutableState.value = UIState.Error("Please login first!")
            return
        }

        if(videoId == null){
            _videoSubtitleMutableState.value = UIState.Error("Couldn't find subtitle for this video!")
            return
        }

        viewModelScope.launch {
            feedRepo.getVideoSubtitle(accessToken = accessToken, videoId = videoId).collectLatest {
                _videoSubtitleMutableState.value = it
            }
        }
    }


}