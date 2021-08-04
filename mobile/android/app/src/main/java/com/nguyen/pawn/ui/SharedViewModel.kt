package com.nguyen.pawn.ui

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nguyen.pawn.model.Language
import com.nguyen.pawn.model.User
import com.nguyen.pawn.model.Word
import com.nguyen.pawn.repo.AuthRepository
import com.nguyen.pawn.repo.LanguageRepository
import com.nguyen.pawn.repo.WordRepository
import com.nguyen.pawn.util.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/** This viewModel contains states that
 * shared between different screens */

@HiltViewModel
class SharedViewModel
@Inject constructor(
    private val authRepo: AuthRepository,
    private val wordRepo: WordRepository,
    private val languageRepo: LanguageRepository
) : ViewModel() {

    /** ---STATES--- */

    /** This is the current user (null when no user is logged in) */
    private val _userUIState: MutableState<UIState<User>> = mutableStateOf(UIState.Initial(null))
    val userUIState: State<UIState<User>> = _userUIState

    /** The current language that the app is on */
    private val _currentPickedLanguage: MutableState<Language?> = mutableStateOf(null)
    val currentPickedLanguage: State<Language?> = _currentPickedLanguage

    /** The list of languages that user wants to learn */
    private val _pickedLanguagesUIState: MutableState<UIState<List<Language>>> = mutableStateOf(UIState.Initial(null))
    val pickedLanguagesUIState: State<UIState<List<Language>>> = _pickedLanguagesUIState

    /** The list of languages that user wants to learn (this list is used when user is choosing languages) */
//    private val _displayPickedLanguages: MutableState<ArrayList<Language>> = mutableStateOf(arrayListOf())
//    val displayPickedLanguages: State<ArrayList<Language>> = _displayPickedLanguages

    /** This is a list of words that user saved */
    private val _savedWords: MutableState<ArrayList<Word>> = mutableStateOf(arrayListOf())
    val savedWords: State<ArrayList<Word>> = _savedWords

    /** A hash map that helps update saved words quicker */
    private val savedWordMap = HashMap<String, Boolean>()

    /** A hash map that helps update picked languages quicker */
    private val pickedLanguageMap = HashMap<String, Boolean>()


    /** ---INTENTS--- */

    /** Auth intents*/

    /** Get the current user using token, set
     *  the current user null if not logged in */
    fun getUser(accessToken: String?, refreshToken: String?) {
        if (accessToken.isNullOrBlank() || refreshToken.isNullOrBlank()) {
            _userUIState.value = UIState.Loaded(null)
            return
        }
        viewModelScope.launch {
            authRepo.checkAuthStatus(accessToken).collectLatest {
                _userUIState.value = it
            }
        }
    }

    /** Call api to get rid of the refresh token,
     * then set the current user to null */
    fun logout(refreshToken: String?) {
        viewModelScope.launch {
            if (refreshToken != null) {
                authRepo.logout(refreshToken).collectLatest {
                    if (it is UIState.Error) {
                        _userUIState.value = UIState.Error(it.errorMsg ?: "")
                    } else if (it is UIState.Loaded) {
                        _userUIState.value = UIState.Loaded(null)
                    }
                }
            } else _userUIState.value = UIState.Loaded(null)

        }
    }


    /** Language intents */

    /** Check in pickedLanguageMap if the chosen language is picked or not,
     * if yes then remove it from map and display list,
     * if no then add to the map and display list */
    fun togglePickedLanguage(language: Language) {
        pickedLanguageMap[language.id] = pickedLanguageMap[language.id] != true
    }

    /** Get the current picked languages either
     *  from network or room database */
    fun getPickedLanguages(accessToken: String?) {
        viewModelScope.launch {
            languageRepo.getLearningLanguages(accessToken).collectLatest {
                _pickedLanguagesUIState.value = it
                if(it is UIState.Loaded && it.loadedValue != null){
                    val languages = it.loadedValue
                    val currentLanguageId = _currentPickedLanguage.value?.id
                    var currentInList = false
                    for (language in languages) {
                        pickedLanguageMap[language.id] = true
                        if (!currentInList && currentLanguageId != null) {
                            currentInList = language.id == currentLanguageId
                        }
                    }
                    if (languages.isNotEmpty() && !currentInList) {
                        _currentPickedLanguage.value = languages.first()
                    }
                }
            }
        }
    }

    /** Save the picked languages to network and/or room database
     *  Update the picked list and current pick language*/
    fun savePickedLanguages(languages: List<Language>, accessToken: String?) {
        viewModelScope.launch {
            languageRepo.pickLearningLanguages(languages, accessToken).collectLatest { it ->
                _pickedLanguagesUIState.value = it
                if(it is UIState.Loaded && it.loadedValue != null){
                    if (_currentPickedLanguage.value == null) {
                        if (languages.isNotEmpty()) {
                            _currentPickedLanguage.value = languages.first()
                        }
                    } else {
                        if (languages.isNotEmpty() && languages.none { language -> language.id == _currentPickedLanguage.value!!.id }) {
                            _currentPickedLanguage.value = languages.first()
                        }
                    }
                }
            }
        }
    }

    /** This will update the current language picked
     * by the user */
    fun changeCurrentPickedLanguage(language: Language) {
        if (_currentPickedLanguage.value?.id != language.id) _currentPickedLanguage.value = language
    }


    /**  Word intents */

    /** If the word is saved, then remove it from map and list
     * else add it to the map and list */
    fun toggleSavedWord(word: Word) {
        if (savedWordMap[word.value] == true) {
            _savedWords.value = _savedWords.value.filter { savedWord ->
                savedWord.value != word.value
            } as ArrayList<Word>
            savedWordMap[word.value] = false
        } else {
            _savedWords.value = (arrayListOf(word) + _savedWords.value) as ArrayList<Word>
            savedWordMap[word.value] = true
        }
    }


    /** A helper function to check if the word is in the map */
    fun checkIsSaved(wordId: String): Boolean {
        return savedWordMap[wordId] == true
    }

}