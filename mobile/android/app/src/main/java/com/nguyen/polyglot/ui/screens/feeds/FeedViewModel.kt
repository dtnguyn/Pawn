package com.nguyen.polyglot.ui.screens.feeds

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nguyen.polyglot.model.Feed
import com.nguyen.polyglot.repo.FeedRepository
import com.nguyen.polyglot.util.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class FeedViewModel@Inject constructor (
    private val feedRepo: FeedRepository,
): ViewModel()  {

    companion object {
        private val allTopics = listOf("sports", "gaming", "business", "tech", "beauty", "movie", "politics")
    }

    private val _feedItems: MutableState<UIState<List<Feed>>> = mutableStateOf(UIState.Initial(listOf()))
    val feedItems: State<UIState<List<Feed>>> = _feedItems

    private val _topics: MutableState<UIState<String>> = mutableStateOf(UIState.Initial(""))
    val topics: State<UIState<String>> = _topics

    private val topicMap = HashMap<String, Boolean?>()

    var currentFeedIndex = 0
    var currentFeedOffset = 0

    init {
        allTopics.forEach {
            topicMap[it] = false
        }
    }


    fun getFeeds(accessToken: String?, language: String){

        if(accessToken == null){
            _feedItems.value = UIState.Error("Please login to get news feeds")
            return
        }
        viewModelScope.launch {
            feedRepo.getFeeds(accessToken, language).collect {
                _feedItems.value = it
            }
        }
    }

    fun updateTopics(accessToken: String?) {
        if(accessToken.isNullOrBlank()){
            _topics.value = UIState.Error("Please login to update topics!")
            return
        }

        var newTopicString = ""
        allTopics.forEach {
            if(topicMap[it] == true){
                newTopicString += "$it,"
            }
        }
        //Update topic on server
        viewModelScope.launch {
            feedRepo.updateTopics(accessToken, newTopicString).collectLatest {
                _topics.value = it
            }
        }
    }

    fun pickTopic(topic: String){
        if(topic.isNotEmpty()) {
            topicMap[topic] = !(topicMap[topic] ?: false)

            // This is just for the ui to recompose
            _topics.value = UIState.Loaded(topics.value.value)
        }
    }

    fun getTopics(accessToken: String?) {
        if(accessToken.isNullOrBlank()){
            _topics.value = UIState.Error("Please login to update topics!")
            return
        }
        viewModelScope.launch {
            feedRepo.getTopics(accessToken).collectLatest {state ->
                _topics.value = state

                state.value?.let {
                    it.split(",").map { it.trim() }.forEach { topic ->
                        if(topic.isNotEmpty()){
                            topicMap[topic] = true
                        }
                    }
                }

            }
        }
    }

    fun dismissTopics(){
        allTopics.forEach {
            topicMap[it] = false
        }
        topics.value.value?.let {
            it.split(",").map { it.trim() }.forEach { topic ->
                if(topic.isNotEmpty()){
                    topicMap[topic] = true
                }
            }
        }
    }

    fun isTopicPicked(topic: String): Boolean {
        return topicMap[topic] ?: false
    }

    fun saveFeedScrollingState(index: Int, offset: Int){
        currentFeedIndex = index
        currentFeedOffset = offset
    }


}