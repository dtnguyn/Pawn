package com.nguyen.polygot.ui.screens.feedDetail

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nguyen.polygot.model.FeedDetail
import com.nguyen.polygot.model.NewsDetail
import com.nguyen.polygot.repo.FeedRepository
import com.nguyen.polygot.util.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedDetailViewModel
@Inject constructor (
    private val feedRepo: FeedRepository,
): ViewModel()  {

    private val _newsDetailUIState: MutableState<UIState<FeedDetail<NewsDetail>>> = mutableStateOf(UIState.Initial(null))
    val newsDetailUIState: State<UIState<FeedDetail<NewsDetail>>> = _newsDetailUIState


    fun getNewsDetail(accessToken: String?, newsUrl: String, newsId: String) {

        if(accessToken == null){
            _newsDetailUIState.value = UIState.Error("Please login first!")
            return
        }

        viewModelScope.launch {
            feedRepo.getNewsDetail(accessToken, newsUrl, newsId).collectLatest {
                _newsDetailUIState.value = it
            }
        }


    }


}