package com.nguyen.pawn.ui.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nguyen.pawn.model.Definition
import com.nguyen.pawn.model.Language
import com.nguyen.pawn.model.Word
import com.nguyen.pawn.repo.WordRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WordViewModel
@Inject constructor(
    private val repo: WordRepository
) : ViewModel() {

    private val _dailyWords: MutableState<ArrayList<Word>> = mutableStateOf(arrayListOf())
    private val _savedWords: MutableState<ArrayList<Word>> = mutableStateOf(arrayListOf())
    private val _pickedLanguages: MutableState<ArrayList<Language>> = mutableStateOf(arrayListOf())
    private val _wordDefinition: MutableState<Definition?> = mutableStateOf(null)



    private val savedWordMap = HashMap<String, Boolean>()
    private val pickedLanguageMap = HashMap<String, Boolean>()

    val dailyWords: State<ArrayList<Word>> = _dailyWords
    val savedWords: State<ArrayList<Word>> = _savedWords
    val pickedLanguages: State<ArrayList<Language>> = _pickedLanguages


    init {
        _dailyWords.value = arrayListOf(
            Word(
                "12",
                "Pepper1",
                "A pungent, hot-tasting powder prepared from dried and ground peppercorns, commonly used as a spice or condiment to flavor food.",
                "/ˈpepə(r)/"
            ),
            Word(
                "13",
                "Pepper2",
                "A pungent, hot-tasting powder prepared from dried and ground peppercorns, commonly used as a spice or condiment to flavor food.",
                "/ˈpepə(r)/"
            ),
            Word(
                "14",
                "Pepper3",
                "A pungent, hot-tasting powder prepared from dried and ground peppercorns, commonly used as a spice or condiment to flavor food.",
                "/ˈpepə(r)/"
            ),
        )

        _savedWords.value = arrayListOf(
            Word(
                "1",
                "Phenomenal",
                "A pungent, hot-tasting powder prepared from dried and ground peppercorns, commonly used as a spice or condiment to flavor food.",
                "/fəˈnɑːmɪnl/"
            ),
            Word(
                "2",
                "Phenomenal",
                "A pungent, hot-tasting powder prepared from dried and ground peppercorns, commonly used as a spice or condiment to flavor food.",
                "/fəˈnɑːmɪnl/"
            ),
            Word(
                "3",
                "Phenomenal",
                "A pungent, hot-tasting powder prepared from dried and ground peppercorns, commonly used as a spice or condiment to flavor food.",
                "/fəˈnɑːmɪnl/"
            ),
            Word(
                "4",
                "Phenomenal",
                "A pungent, hot-tasting powder prepared from dried and ground peppercorns, commonly used as a spice or condiment to flavor food.",
                "/fəˈnɑːmɪnl/"
            ),
            Word(
                "5",
                "Phenomenal",
                "A pungent, hot-tasting powder prepared from dried and ground peppercorns, commonly used as a spice or condiment to flavor food.",
                "/fəˈnɑːmɪnl/"
            ),
            Word(
                "6",
                "Phenomenal",
                "A pungent, hot-tasting powder prepared from dried and ground peppercorns, commonly used as a spice or condiment to flavor food.",
                "/fəˈnɑːmɪnl/"
            ),
            Word(
                "7",
                "Phenomenal",
                "A pungent, hot-tasting powder prepared from dried and ground peppercorns, commonly used as a spice or condiment to flavor food.",
                "/fəˈnɑːmɪnl/"
            ),
            Word(
                "8",
                "Phenomenal",
                "A pungent, hot-tasting powder prepared from dried and ground peppercorns, commonly used as a spice or condiment to flavor food.",
                "/fəˈnɑːmɪnl/"
            ),
            Word(
                "9",
                "Phenomenal",
                "A pungent, hot-tasting powder prepared from dried and ground peppercorns, commonly used as a spice or condiment to flavor food.",
                "/fəˈnɑːmɪnl/"
            ),
            Word(
                "10",
                "Phenomenal",
                "A pungent, hot-tasting powder prepared from dried and ground peppercorns, commonly used as a spice or condiment to flavor food.",
                "/fəˈnɑːmɪnl/"
            ),
            Word(
                "11",
                "Phenomenal",
                "A pungent, hot-tasting powder prepared from dried and ground peppercorns, commonly used as a spice or condiment to flavor food.",
                "/fəˈnɑːmɪnl/"
            ),
        )

        for (word in _savedWords.value) {
            savedWordMap[word.id] = true
        }
    }

    fun removeDailyWords(wordId: String) {
        _dailyWords.value = _dailyWords.value.filter { word ->
            word.id != wordId
        } as ArrayList<Word>
    }

    fun toggleSavedWord(word: Word) {
        if (savedWordMap[word.id] == true) {
            _savedWords.value = _savedWords.value.filter { savedWord ->
                savedWord.id != word.id
            } as ArrayList<Word>
            savedWordMap[word.id] = false
        } else {
            _savedWords.value = (arrayListOf(word) + _savedWords.value) as ArrayList<Word>
            savedWordMap[word.id] = true
        }
    }

    fun savePickedLanguages(languages: List<Language>, accessToken: String?) {
        val languageString = languages.map { language -> language.id }
        if(accessToken == null) {

        } else {
            viewModelScope.launch {
                val response = repo.pickLearningLanguages(languageString, accessToken)
                if(response) {
                    _pickedLanguages.value = languages as ArrayList<Language>
                } else {

                }
            }
        }
    }

    fun togglePickedLanguage(language: Language) {
        if(pickedLanguageMap[language.id] == true) {
            pickedLanguageMap[language.id] = false
            _pickedLanguages.value = _pickedLanguages.value.filter { pickedLanguage ->
                pickedLanguage.id != language.id
            } as ArrayList<Language>
        } else {
            pickedLanguageMap[language.id] = true
            _pickedLanguages.value = (pickedLanguages.value + arrayListOf(language)) as ArrayList<Language>
        }
    }

    fun initializePickedLanguages(languages: List<Language>){
        for(language in languages) {
            pickedLanguageMap[language.id] = true
        }
        _pickedLanguages.value =  languages as ArrayList<Language>
    }

    fun checkIsSaved(wordId: String): Boolean{
        return savedWordMap[wordId] == true
    }



}