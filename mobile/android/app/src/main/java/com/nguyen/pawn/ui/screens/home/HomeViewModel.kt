package com.nguyen.pawn.ui.screens.home

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nguyen.pawn.model.Definition
import com.nguyen.pawn.model.Language
import com.nguyen.pawn.model.Word
import com.nguyen.pawn.repo.LanguageRepository
import com.nguyen.pawn.repo.WordRepository
import com.nguyen.pawn.ui.viewmodels.LanguageViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
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


    fun getDailyWords(dailyWordCount: Int) {

    }

    fun removeDailyWords(wordId: String) {
        _dailyWords.value = _dailyWords.value.filter { word ->
            word.id != wordId
        } as ArrayList<Word>
    }


}