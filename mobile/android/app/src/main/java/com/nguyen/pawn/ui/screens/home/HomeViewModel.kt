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
    private val _dailyEnWords: MutableState<ArrayList<Word>?> = mutableStateOf(null)
    val dailyEnWords: State<ArrayList<Word>?> = _dailyEnWords

    private val _dailyEsWords: MutableState<ArrayList<Word>?> = mutableStateOf(null)
    val dailyEsWords: State<ArrayList<Word>?> = _dailyEsWords

    private val _dailyFrWords: MutableState<ArrayList<Word>?> = mutableStateOf(null)
    val dailyFrWords: State<ArrayList<Word>?> = _dailyFrWords

    private val _dailyDeWords: MutableState<ArrayList<Word>?> = mutableStateOf(null)
    val dailyDeWords: State<ArrayList<Word>?> = _dailyDeWords

    // UI states
    private val _isLoadingDailyWords = mutableStateOf(false)
    val isLoadingDailyWords: State<Boolean> = _isLoadingDailyWords

    private val _isLoadingCurrentUser = mutableStateOf(false)
    val isLoadingCurrentUser: State<Boolean> = _isLoadingDailyWords


    fun getDailyWords(dailyWordCount: Int, languageId: String) {
        viewModelScope.launch {
                when(languageId){
                    SupportedLanguage.ENGLISH.id -> {
                        if(_dailyEnWords.value == null) {
                            withContext(Main){
                                _isLoadingDailyWords.value = true
                            }
                            val words = wordRepo.getRandomDailyWord(dailyWordCount, languageId)
                            _dailyEnWords.value = words

                            withContext(Main){
                                _isLoadingDailyWords.value = false
                            }
                        }
                    }

                    SupportedLanguage.SPANISH.id -> {
                        if(_dailyEsWords.value == null) {
                            withContext(Main){
                                _isLoadingDailyWords.value = true
                            }
                            val words = wordRepo.getRandomDailyWord(dailyWordCount, languageId)
                            _dailyEsWords.value = words

                            withContext(Main){
                                _isLoadingDailyWords.value = false
                            }
                        }
                    }

                    SupportedLanguage.FRENCH.id -> {
                        if(_dailyFrWords.value == null) {
                            withContext(Main){
                                _isLoadingDailyWords.value = true
                            }
                            val words = wordRepo.getRandomDailyWord(dailyWordCount, languageId)
                            _dailyFrWords.value = words

                            withContext(Main){
                                _isLoadingDailyWords.value = false
                            }
                        }

                    }
                    SupportedLanguage.GERMANY.id -> {
                        if(_dailyDeWords.value == null) {
                            withContext(Main){
                                _isLoadingDailyWords.value = true
                            }
                            val words = wordRepo.getRandomDailyWord(dailyWordCount, languageId)
                            _dailyDeWords.value = words

                            withContext(Main){
                                _isLoadingDailyWords.value = false
                            }
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

    fun displayLoadingUser(display: Boolean) {
        _isLoadingCurrentUser.value = display
    }


//    suspend fun turnOnLoading(type: LoadingType) {
//        withContext(Main) {
//            if ((_uiState.value is UIState.Loading).not()) _uiState.value = UIState.Loading(type)
//        }
//    }
//
//    suspend fun goToIdle(from: UIState) {
//        withContext(Main) {
//            _uiState.value = UIState.Idle(from)
//        }
//    }
//
//    suspend fun emitError(errMsg: String) {
//        withContext(Main) {
//            _uiState.value = UIState.Error(errMsg)
//        }
//    }



}