package com.nguyen.polyglot.ui.screens.definition

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nguyen.polyglot.model.WordDetail
import com.nguyen.polyglot.repo.WordRepository
import com.nguyen.polyglot.util.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject


private const val TAG = "WordDetailViewModel"

@HiltViewModel
class WordDetailViewModel
@Inject constructor(private val wordRepo: WordRepository) : ViewModel() {

    private val _wordDetailUIState = mutableStateOf<UIState<WordDetail>>(UIState.Initial(null))

    val wordDetailUIState: State<UIState<WordDetail>> = _wordDetailUIState


    fun getWordDetail(wordValue: String?, language: String?) {
        viewModelScope.launch {
            if (wordValue == null || language == null) {
                _wordDetailUIState.value = UIState.Error("No word found!")
                return@launch
            }
            wordRepo.getWordDetail(wordValue, language).collect {
                Log.d(TAG, "getWordDetail ${it.value}" )
                _wordDetailUIState.value = it
            }
        }
    }

}