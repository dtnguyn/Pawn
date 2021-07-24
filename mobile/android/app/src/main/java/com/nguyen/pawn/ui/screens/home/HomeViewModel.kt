package com.nguyen.pawn.ui.screens.home

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nguyen.pawn.model.Word
import com.nguyen.pawn.repo.WordRepository
import com.nguyen.pawn.util.LoadingType
import com.nguyen.pawn.util.SupportedLanguage
import com.nguyen.pawn.util.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
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

    // This is a list of daily English random words
    private val _dailyEnWords: MutableState<ArrayList<Word>> = mutableStateOf(arrayListOf())
    val dailyEnWords: State<ArrayList<Word>> = _dailyEnWords

    private val _dailyEsWords: MutableState<ArrayList<Word>> = mutableStateOf(arrayListOf())
    val dailyEsWords: State<ArrayList<Word>> = _dailyEnWords

    private val _dailyFrWords: MutableState<ArrayList<Word>> = mutableStateOf(arrayListOf())
    val dailyFrWords: State<ArrayList<Word>> = _dailyEnWords

    private val _dailyDeWords: MutableState<ArrayList<Word>> = mutableStateOf(arrayListOf())
    val dailyDeWords: State<ArrayList<Word>> = _dailyEnWords

    private val _uiState = mutableStateOf<UIState>(UIState.Idle())
    val uiState: State<UIState> = _uiState


    fun getDailyWords(dailyWordCount: Int, languageId: String) {
        viewModelScope.launch {
            val words = wordRepo.getRandomDailyWord(dailyWordCount, languageId) as ArrayList<Word>
            withContext(Main){
                when(languageId){
                    SupportedLanguage.ENGLISH.id -> {
                        _dailyEnWords.value = words

                    }
                    SupportedLanguage.SPANISH.id -> {
                        _dailyEnWords.value = words

                    }
                    SupportedLanguage.FRENCH.id -> {
                        _dailyEnWords.value = words

                    }
                    SupportedLanguage.GERMANY.id -> {
                        _dailyEnWords.value = words
                    }
                }
            }

        }
    }

    fun removeDailyWords(wordId: String) {
//        _dailyEnWords.value = _dailyEnWords.value.filter { word ->
//            word.id != wordId
//        } as ArrayList<Word>
    }


    suspend fun turnOnLoading(type: LoadingType) {
        withContext(Main) {
            if ((_uiState.value is UIState.Loading).not()) _uiState.value = UIState.Loading(type)
        }
    }

    suspend fun goToIdle(from: UIState) {
        withContext(Main) {
            _uiState.value = UIState.Idle(from)
        }
    }

    suspend fun emitError(errMsg: String) {
        withContext(Main) {
            _uiState.value = UIState.Error(errMsg)
        }
    }

}