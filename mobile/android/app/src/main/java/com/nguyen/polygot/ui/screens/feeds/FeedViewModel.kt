package com.nguyen.polygot.ui.screens.feeds

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nguyen.polygot.model.Feed
import com.nguyen.polygot.repo.FeedRepository
import com.nguyen.polygot.repo.WordRepository
import com.nguyen.polygot.util.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class FeedViewModel@Inject constructor (
    private val feedRepo: FeedRepository,
): ViewModel()  {

    private val _feeds: MutableState<UIState<List<Feed>>> = mutableStateOf(UIState.Initial(listOf()))
    val feed: State<UIState<List<Feed>>> = _feeds


    fun getFeeds(accessToken: String?, language: String){

        if(accessToken == null){
            _feeds.value = UIState.Error("Please login to get news feeds")
            return
        }
        viewModelScope.launch {
            feedRepo.getFeeds(accessToken, language).collect {
                _feeds.value = it
            }
        }

    }


}