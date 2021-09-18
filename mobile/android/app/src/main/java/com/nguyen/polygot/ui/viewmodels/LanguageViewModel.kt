package com.nguyen.polygot.ui.viewmodels
//
//import android.util.Log
//import androidx.compose.runtime.MutableState
//import androidx.compose.runtime.State
//import androidx.compose.runtime.mutableStateOf
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.nguyen.pawn.model.Language
//import com.nguyen.pawn.repo.LanguageRepository
//import dagger.hilt.android.lifecycle.HiltViewModel
//import kotlinx.coroutines.launch
//import javax.inject.Inject
//
//
//@HiltViewModel
//class LanguageViewModel
//@Inject constructor(
//    private val repo: LanguageRepository
//) : ViewModel() {
//
//    companion object{
//        private const val TAG = "LanguageViewModel"
//    }
//
//    private val _currentPickedLanguage: MutableState<Language?> = mutableStateOf(null)
//    private val _pickedLanguages: MutableState<List<Language>> = mutableStateOf(listOf())
//    private val _displayPickedLanguages: MutableState<ArrayList<Language>> = mutableStateOf(arrayListOf())
//
//
//    val pickedLanguages: State<List<Language>> = _pickedLanguages
//    val displayPickedLanguages: State<ArrayList<Language>> = _displayPickedLanguages
//    val currentPickedLanguage: State<Language?> = _currentPickedLanguage
//
//
//    private val pickedLanguageMap = HashMap<String, Boolean>()
//
//
//    fun togglePickedLanguage(language: Language) {
//        if (pickedLanguages.value == null || _displayPickedLanguages.value == null) return
//        if (pickedLanguageMap[language.id] == true) {
//            pickedLanguageMap[language.id] = false
//            _displayPickedLanguages.value = _displayPickedLanguages.value!!.filter { pickedLanguage ->
//                pickedLanguage.id != language.id
//            } as ArrayList<Language>
//        } else {
//            pickedLanguageMap[language.id] = true
//            _displayPickedLanguages.value =
//                (displayPickedLanguages.value!! + arrayListOf(language)) as ArrayList<Language>
//        }
//    }
//
//    fun getPickedLanguages(accessToken: String?) {
//        viewModelScope.launch {
//            val languages = repo.getLearningLanguages(accessToken)
//            Log.d(TAG, "languages: $languages")
//            _pickedLanguages.value = languages
//            _displayPickedLanguages.value = languages as ArrayList<Language>
//            for (language in languages) {
//                pickedLanguageMap[language.id] = true
//            }
//            if (languages.isNotEmpty()) {
//                _currentPickedLanguage.value = languages.first()
//            }
//        }
//    }
//
//    fun savePickedLanguages(languages: ArrayList<Language>, accessToken: String?) {
//        viewModelScope.launch {
//            val response = repo.pickLearningLanguages(languages, accessToken)
//            if (response) {
//                Log.d(TAG, "response $response")
//                _pickedLanguages.value = languages
//                _displayPickedLanguages.value = languages
//                if (_currentPickedLanguage.value == null) {
//                    if (languages.isNotEmpty()) {
//                        _currentPickedLanguage.value = languages.first()
//                    }
//                } else {
//                    if (languages.isNotEmpty() && languages.filter { it.id == _currentPickedLanguage.value!!.id }.isEmpty()) {
//                        _currentPickedLanguage.value = languages.first()
//                    }
//                }
//            } else {
//                //Handle error
//            }
//        }
//    }
//
//    fun changeCurrentPickedLanguage(language: Language) {
//        _currentPickedLanguage.value = language
//    }
//
//
//}