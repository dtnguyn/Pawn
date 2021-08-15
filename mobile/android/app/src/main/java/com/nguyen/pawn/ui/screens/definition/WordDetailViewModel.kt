package com.nguyen.pawn.ui.screens.definition

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nguyen.pawn.model.WordDetail
import com.nguyen.pawn.repo.WordRepository
import com.nguyen.pawn.util.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

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
            wordRepo.getWordDetail(wordValue, language).collectLatest {
                _wordDetailUIState.value = it
            }
        }
    }

}