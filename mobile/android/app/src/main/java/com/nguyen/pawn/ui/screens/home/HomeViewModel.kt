package com.nguyen.pawn.ui.screens.home

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.nguyen.pawn.model.Word
import com.nguyen.pawn.repo.WordRepository
import com.nguyen.pawn.util.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject


/**
    This viewModel holds all the data for the home screen
*/

@HiltViewModel
class HomeViewModel
@Inject constructor (
    private val wordRepo: WordRepository,
): ViewModel() {

    // This is a list of daily random words
    private val _dailyWords: MutableState<ArrayList<Word>> = mutableStateOf(arrayListOf())
    val dailyWords: State<ArrayList<Word>> = _dailyWords

    private val _uiState = mutableStateOf<UIState>(UIState.Loading)
    val uiState: State<UIState> = _uiState


    fun getDailyWords(dailyWordCount: Int) {

    }

    fun removeDailyWords(wordId: String) {
        _dailyWords.value = _dailyWords.value.filter { word ->
            word.id != wordId
        } as ArrayList<Word>
    }


    suspend fun turnOnLoading() {
        withContext(Dispatchers.Main) {
            if (_uiState.value != UIState.Loading) _uiState.value = UIState.Loading
        }
    }

    suspend fun turnOffLoading() {
        withContext(Dispatchers.Main) {
            _uiState.value = UIState.Idle
        }
    }

    suspend fun emitError(errMsg: String) {
        withContext(Dispatchers.Main) {
            _uiState.value = UIState.Error(errMsg)
        }
    }

}