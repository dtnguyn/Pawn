package com.nguyen.pawn.ui.screens.home

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nguyen.pawn.model.Language
import com.nguyen.pawn.model.Word
import com.nguyen.pawn.repo.WordRepository
import com.nguyen.pawn.util.LoadingType
import com.nguyen.pawn.util.SupportedLanguage
import com.nguyen.pawn.util.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.collectLatest
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
    private val _dailyEnWordsUIState: MutableState<UIState<List<Word>>> = mutableStateOf(UIState.Initial(null))
    val dailyEnWordsUIState: State<UIState<List<Word>>> = _dailyEnWordsUIState

    private val _dailyEsWordsUIState: MutableState<UIState<List<Word>>> = mutableStateOf(UIState.Initial(null))
    val dailyEsWordsUIState: State<UIState<List<Word>>> = _dailyEsWordsUIState

    private val _dailyFrWordsUIState: MutableState<UIState<List<Word>>> = mutableStateOf(UIState.Initial(null))
    val dailyFrWordsUIState: State<UIState<List<Word>>> = _dailyFrWordsUIState

    private val _dailyDeWordsUIState: MutableState<UIState<List<Word>>> = mutableStateOf(UIState.Initial(null))
    val dailyDeWordsUIState: State<UIState<List<Word>>> = _dailyDeWordsUIState




    fun getDailyWords(dailyWordCount: Int, languageId: String) {
        viewModelScope.launch {
                when(languageId){
                    SupportedLanguage.ENGLISH.id -> {
                        if(_dailyEnWordsUIState.value.loadedValue == null) {
                            wordRepo.getRandomDailyWord(dailyWordCount, languageId).collectLatest {
                                _dailyEnWordsUIState.value = it
                            }
                        }
                    }

                    SupportedLanguage.SPANISH.id -> {
                        if(_dailyEsWordsUIState.value.loadedValue == null) {
                            wordRepo.getRandomDailyWord(dailyWordCount, languageId).collectLatest {
                                _dailyEsWordsUIState.value = it
                            }
                        }
                    }

                    SupportedLanguage.FRENCH.id -> {
                        if(_dailyFrWordsUIState.value.loadedValue == null) {
                            wordRepo.getRandomDailyWord(dailyWordCount, languageId).collectLatest {
                                _dailyFrWordsUIState.value = it
                            }
                        }

                    }
                    SupportedLanguage.GERMANY.id -> {
                        if(_dailyDeWordsUIState.value.loadedValue == null) {
                            wordRepo.getRandomDailyWord(dailyWordCount, languageId).collectLatest {
                                _dailyDeWordsUIState.value = it
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





}