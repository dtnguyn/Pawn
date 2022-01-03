package com.nguyen.polyglot.ui.screens.home

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nguyen.polyglot.model.Word
import com.nguyen.polyglot.repo.WordRepository
import com.nguyen.polyglot.util.SupportedLanguage
import com.nguyen.polyglot.util.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


/**
This viewModel holds all the data for the home screen
 */

@HiltViewModel
class HomeViewModel
@Inject constructor(
    private val wordRepo: WordRepository,
) : ViewModel() {

    // This is a list of daily English random words
    private val _dailyEnWordsUIState: MutableState<UIState<List<Word>>> =
        mutableStateOf(UIState.Initial(null))
    val dailyEnWordsUIState: State<UIState<List<Word>>> = _dailyEnWordsUIState

    private val _dailyEsWordsUIState: MutableState<UIState<List<Word>>> =
        mutableStateOf(UIState.Initial(null))
    val dailyEsWordsUIState: State<UIState<List<Word>>> = _dailyEsWordsUIState

    private val _dailyFrWordsUIState: MutableState<UIState<List<Word>>> =
        mutableStateOf(UIState.Initial(null))
    val dailyFrWordsUIState: State<UIState<List<Word>>> = _dailyFrWordsUIState

    private val _dailyDeWordsUIState: MutableState<UIState<List<Word>>> =
        mutableStateOf(UIState.Initial(null))
    val dailyDeWordsUIState: State<UIState<List<Word>>> = _dailyDeWordsUIState

    private val _showAddLanguagesMenu: MutableState<Boolean?> = mutableStateOf(null)
    val showAddLanguagesMenu: State<Boolean?> = _showAddLanguagesMenu


    fun getDailyWords(dailyWordCount: Int, languageId: String, topic: String) {
        viewModelScope.launch {
            when (languageId) {
                SupportedLanguage.ENGLISH.id -> {
                    if (_dailyEnWordsUIState.value.value == null) {
                        wordRepo.getRandomDailyWord(dailyWordCount, languageId, topic).collectLatest {
                            _dailyEnWordsUIState.value = it
                        }
                    }
                }

                SupportedLanguage.SPANISH.id -> {
                    if (_dailyEsWordsUIState.value.value == null) {
                        wordRepo.getRandomDailyWord(dailyWordCount, languageId, topic).collectLatest {
                            _dailyEsWordsUIState.value = it
                        }
                    }
                }

                SupportedLanguage.FRENCH.id -> {
                    if (_dailyFrWordsUIState.value.value == null) {
                        wordRepo.getRandomDailyWord(dailyWordCount, languageId, topic).collectLatest {
                            _dailyFrWordsUIState.value = it
                        }
                    }
                }

                SupportedLanguage.GERMANY.id -> {
                    if (_dailyDeWordsUIState.value.value == null) {
                        wordRepo.getRandomDailyWord(dailyWordCount, languageId, topic).collectLatest {
                            _dailyDeWordsUIState.value = it
                        }
                    }
                }
            }
        }
    }

    fun removeDailyWords(word: Word, languageId: String?) {
        if(languageId == null) return
        val wordValue = word.value

        viewModelScope.launch {
            wordRepo.hideWord(word, languageId)
        }

        _dailyEnWordsUIState.value = UIState.Loaded(
            when (languageId) {
                "en_US" -> {
                    _dailyEnWordsUIState.value.value?.filter { it.value != wordValue }
                }
                "es" -> {
                    _dailyEsWordsUIState.value.value?.filter { it.value != wordValue }
                }
                "fr" -> {
                    _dailyFrWordsUIState.value.value?.filter { it.value != wordValue }
                }
                "de" -> {
                    _dailyDeWordsUIState.value.value?.filter { it.value != wordValue }
                }
                else -> {
                    null
                }
            }
        )

    }

    fun setAddLanguagesMenu(open: Boolean) {
        _showAddLanguagesMenu.value = open
    }



}