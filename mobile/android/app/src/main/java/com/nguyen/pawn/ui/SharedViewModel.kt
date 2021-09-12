package com.nguyen.pawn.ui

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nguyen.pawn.model.*
import com.nguyen.pawn.repo.AuthRepository
import com.nguyen.pawn.repo.LanguageRepository
import com.nguyen.pawn.repo.WordRepository
import com.nguyen.pawn.util.SupportedLanguage
import com.nguyen.pawn.util.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

/** This viewModel contains states that
 * shared between different screens */

private const val TAG = "SharedViewModel"

@HiltViewModel
class SharedViewModel
@Inject constructor(
    private val authRepo: AuthRepository,
    private val wordRepo: WordRepository,
    private val languageRepo: LanguageRepository,
) : ViewModel() {

    /** ---STATES--- */

    /** This is the current user (null when no user is logged in) */
    private val _authStatusUIState: MutableState<UIState<AuthStatus>> = mutableStateOf(UIState.Initial(AuthStatus(Token(null, null), null)))
    val authStatusUIState: State<UIState<AuthStatus>> = _authStatusUIState

    /** The current language that the app is on */
    private val _currentPickedLanguage: MutableState<Language?> = mutableStateOf(null)
    val currentPickedLanguage: State<Language?> = _currentPickedLanguage

    /** The list of languages that user wants to learn */
    private val _pickedLanguagesUIState: MutableState<UIState<List<Language>>> = mutableStateOf(UIState.Initial(null))
    val pickedLanguagesUIState: State<UIState<List<Language>>> = _pickedLanguagesUIState


    /** This is a list of words that user saved */
    private val _savedEnWordsUIState: MutableState<UIState<List<Word>>> = mutableStateOf(UIState.Initial(listOf()))
    val savedEnWordsUIState: State<UIState<List<Word>>> = _savedEnWordsUIState
    private val savedEnWordMap = HashMap<String, Boolean>()

    /** This is a list of words that user saved */
    private val _savedEsWordsUIState: MutableState<UIState<List<Word>>> = mutableStateOf(UIState.Initial(listOf()))
    val savedEsWordsUIState: State<UIState<List<Word>>> = _savedEsWordsUIState
    private val savedEsWordMap = HashMap<String, Boolean>()

    /** This is a list of words that user saved */
    private val _savedFrWordsUIState: MutableState<UIState<List<Word>>> = mutableStateOf(UIState.Initial(listOf()))
    val savedFrWordsUIState: State<UIState<List<Word>>> = _savedFrWordsUIState
    private val savedFrWordMap = HashMap<String, Boolean>()

    /** This is a list of words that user saved */
    private val _savedDeWordsUIState: MutableState<UIState<List<Word>>> = mutableStateOf(UIState.Initial(listOf()))
    val savedDeWordsUIState: State<UIState<List<Word>>> = _savedDeWordsUIState
    private val savedDeWordMap = HashMap<String, Boolean>()


    /** A hash map that helps update picked languages quicker */
    private val pickedLanguageMap = HashMap<String, Boolean>()


    /** ---INTENTS--- */

    /** Auth intents*/

    fun initializeAuthStatus(initialAuthStatus: AuthStatus){
        _authStatusUIState.value = UIState.Loaded(initialAuthStatus)
        Log.d("TAG", "test this1 ${initialAuthStatus}")
    }

    /** Get the current user using token, set
     *  the current user null if not logged in */
    fun checkAuthStatus(accessToken: String?, refreshToken: String?) {
//        if (accessToken.isNullOrBlank() || refreshToken.isNullOrBlank()) {
//            _authStatusUIState.value = UIState.Loaded(AuthStatus(Token(accessToken, refreshToken), null))
//            return
//        }
        viewModelScope.launch {
            authRepo.checkAuthStatus(accessToken, refreshToken).collectLatest {userState ->
                _authStatusUIState.value = userState
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
                        _authStatusUIState.value = UIState.Error(it.errorMsg ?: "")
                    } else if (it is UIState.Loaded) {
                        _authStatusUIState.value = UIState.Loaded(null)
                    }
                }
            } else _authStatusUIState.value = UIState.Loaded(null)

        }
    }


    /** Language intents */

    /** Check in pickedLanguageMap if the chosen language is picked or not,
     * if yes then remove it from map and display list,
     * if no then add to the map and display list */
    fun togglePickedLanguage(language: Language) {
        pickedLanguageMap[language.id] = pickedLanguageMap[language.id] != true
    }

    fun resetPickedLanguages() {
        _pickedLanguagesUIState.value = UIState.Initial(listOf())
    }

    /** Get the current picked languages either
     *  from network or room database */
    fun getPickedLanguages(accessToken: String?) {
        if(accessToken == null){
            _pickedLanguagesUIState.value = UIState.Loaded(listOf())
            return
        }

        viewModelScope.launch {
            languageRepo.getLearningLanguages(accessToken).collectLatest {
                Log.d(TAG, "getLearningLanguages result ${it.value}")
                _pickedLanguagesUIState.value = it
                if(it is UIState.Loaded && it.value != null){
                    val languages = it.value
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
            languageRepo.pickLearningLanguages(languages, accessToken).collectLatest {
                _pickedLanguagesUIState.value = it
                if(it is UIState.Loaded && it.value != null){
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
    fun toggleSavedWord(word: Word, accessToken: String?, targetLanguage: String?) {
        Log.d("SharedViewModel", "Toggle Saved Word")
        viewModelScope.launch {
            if(accessToken == null){
                return@launch
            }
            if(targetLanguage == null){
                return@launch
            }
            currentSavedWordList(targetLanguage)?.let { currentSavedWordList ->
                wordRepo.toggleSavedWord(word, targetLanguage, currentSavedWordList, accessToken).collectLatest {
                    updateSavedWordList(targetLanguage, it)
                }
            }
        }
    }

    fun getSavedWords(accessToken: String?, targetLanguage: Language?){
        viewModelScope.launch {
            if(accessToken == null){
                return@launch
            }
            targetLanguage?.let { language ->
                Log.d("SharedViewModel", "Getting saved words")
                wordRepo.getSavedWords(language.id, accessToken).collectLatest {
                    updateSavedWordList(language.id, it)
                }
            }
        }
    }


    /** A helper function to check if the word is in the map */
    fun checkIsSaved(wordValue: String, targetLanguage: String?): Boolean {
        return if (targetLanguage != null) {
            when(targetLanguage){
                SupportedLanguage.ENGLISH.id -> {
                    savedEnWordMap[wordValue] == true
                }
                SupportedLanguage.SPANISH.id -> {
                    savedEsWordMap[wordValue] == true
                }
                SupportedLanguage.FRENCH.id -> {
                    savedFrWordMap[wordValue] == true
                }
                SupportedLanguage.GERMANY.id -> {
                    savedDeWordMap[wordValue] == true
                }
                else -> {
                    false
                }
            }
        } else false
    }

    private fun updateSavedWordList(language: String, wordsUIState: UIState<List<Word>>){
        when(language) {
            SupportedLanguage.ENGLISH.id -> {
                _savedEnWordsUIState.value = wordsUIState
                if (wordsUIState is UIState.Loaded) {
                    savedEnWordMap.clear()
                    wordsUIState.value?.forEach { word ->
                        savedEnWordMap[word.value] = true
                    }
                }
            }
            SupportedLanguage.SPANISH.id -> {
                _savedEsWordsUIState.value = wordsUIState
                if (wordsUIState is UIState.Loaded) {
                    savedEsWordMap.clear()
                    wordsUIState.value?.forEach { word ->
                        savedEsWordMap[word.value] = true
                    }
                }
            }
            SupportedLanguage.FRENCH.id -> {
                _savedFrWordsUIState.value = wordsUIState
                if (wordsUIState is UIState.Loaded) {
                    savedFrWordMap.clear()
                    wordsUIState.value?.forEach { word ->
                        savedFrWordMap[word.value] = true
                    }
                }
            }
            SupportedLanguage.GERMANY.id -> {
                _savedDeWordsUIState.value = wordsUIState
                if (wordsUIState is UIState.Loaded) {
                    savedDeWordMap.clear()
                    wordsUIState.value?.forEach { word ->
                        savedDeWordMap[word.value] = true
                    }
                }
            }
        }
    }

    private fun currentSavedWordList(currentLanguage: String?): List<Word>?{
        if (currentLanguage == null) return listOf()
        return when (currentLanguage) {
            SupportedLanguage.ENGLISH.id -> {
                savedEnWordsUIState.value.value
            }
            SupportedLanguage.SPANISH.id -> {
                savedEsWordsUIState.value.value
            }
            SupportedLanguage.FRENCH.id -> {
                savedFrWordsUIState.value.value
            }
            SupportedLanguage.GERMANY.id -> {
                savedDeWordsUIState.value.value
            }
            else -> listOf()
        }
    }

}