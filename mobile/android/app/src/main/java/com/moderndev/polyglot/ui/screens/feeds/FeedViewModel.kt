package com.moderndev.polyglot.ui.screens.feeds

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moderndev.polyglot.model.Feed
import com.moderndev.polyglot.repo.FeedRepository
import com.moderndev.polyglot.util.Constants.allFeedTopics
import com.moderndev.polyglot.util.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class FeedViewModel@Inject constructor (
    private val feedRepo: FeedRepository,
): ViewModel()  {


    private val _enFeedItems: MutableState<UIState<List<Feed>>> = mutableStateOf(UIState.Initial(listOf()))
    val enFeedItems: State<UIState<List<Feed>>> = _enFeedItems

    private val _esFeedItems: MutableState<UIState<List<Feed>>> = mutableStateOf(UIState.Initial(listOf()))
    val esFeedItems: State<UIState<List<Feed>>> = _esFeedItems

    private val _frFeedItems: MutableState<UIState<List<Feed>>> = mutableStateOf(UIState.Initial(listOf()))
    val frFeedItems: State<UIState<List<Feed>>> = _frFeedItems

    private val _deFeedItems: MutableState<UIState<List<Feed>>> = mutableStateOf(UIState.Initial(listOf()))
    val deFeedItems: State<UIState<List<Feed>>> = _deFeedItems

//    private val _topics: MutableState<UIState<String>> = mutableStateOf(UIState.Initial(""))
//    val topics: State<UIState<String>> = _topics

    private val topicMap = HashMap<String, Boolean?>()

    var currentFeedIndex = 0
    var currentFeedOffset = 0

    init {
        allFeedTopics.forEach {
            topicMap[it] = false
        }
    }


    fun getFeed(accessToken: String?, language: String, topics: String = ""){

        if(accessToken == null){
            _enFeedItems.value = UIState.Error("Please login to get news feeds")
            return
        }
        viewModelScope.launch {
            feedRepo.getFeeds(accessToken, language, topics).collect {
                when(language){
                    "en_US" -> {
                        _enFeedItems.value = it
                    }
                    "es" -> {
                        _esFeedItems.value = it
                    }
                    "fr" -> {
                        _frFeedItems.value = it
                    }
                    "de" -> {
                        _deFeedItems.value = it
                    }
                }

            }
        }
    }

//    fun updateTopics(accessToken: String?) {
//        if(accessToken.isNullOrBlank()){
//            _topics.value = UIState.Error("Please login to update topics!")
//            return
//        }
//
//        var newTopicString = ""
//        allFeedTopics.forEach {
//            if(topicMap[it] == true){
//                newTopicString += "$it,"
//            }
//        }
//        //Update topic on server
//        viewModelScope.launch {
//            feedRepo.updateTopics(accessToken, newTopicString).collectLatest {
//                _topics.value = it
//            }
//        }
//    }

    fun pickTopic(topic: String){
        if(topic.isNotBlank()) {
            topicMap[topic] = !(topicMap[topic] ?: false)

            // This is just for the ui to recompose
//            _topics.value = UIState.Loaded(topics.value.value)
        }
    }

//    fun getTopics(accessToken: String?) {
//        if(accessToken.isNullOrBlank()){
//            _topics.value = UIState.Error("Please login to update topics!")
//            return
//        }
//        viewModelScope.launch {
//            feedRepo.getTopics(accessToken).collectLatest {state ->
//                _topics.value = state
//
//                state.value?.let {
//                    it.split(",").map { it.trim() }.forEach { topic ->
//                        if(topic.isNotEmpty()){
//                            topicMap[topic] = true
//                        }
//                    }
//                }
//
//            }
//        }
//    }

    fun dismissTopics(){
        allFeedTopics.forEach {
            topicMap[it] = false
        }


//        topics.value.value?.let {
//            it.split(",").map { it.trim() }.forEach { topic ->
//                if(topic.isNotEmpty()){
//                    topicMap[topic] = true
//                }
//            }
//        }
    }

    fun isTopicPicked(topic: String): Boolean {
        return topicMap[topic] ?: false
    }

    fun saveFeedScrollingState(index: Int, offset: Int){
        currentFeedIndex = index
        currentFeedOffset = offset
    }


}