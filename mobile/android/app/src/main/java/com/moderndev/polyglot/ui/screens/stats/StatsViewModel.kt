package com.moderndev.polyglot.ui.screens.stats

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moderndev.polyglot.model.LanguageReport
import com.moderndev.polyglot.repo.LanguageRepository
import com.moderndev.polyglot.util.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StatsViewModel
@Inject constructor(private val repo: LanguageRepository) : ViewModel() {

    private val _languageReportsUIState: MutableState<UIState<List<LanguageReport>>> = mutableStateOf(UIState.Initial(null))
    val languageReportsUIState: State<UIState<List<LanguageReport>>> = _languageReportsUIState


    fun getLanguageReports(accessToken: String?) {
        if(accessToken == null){
            _languageReportsUIState.value = UIState.Error("Please log in first!")
            return
        }
        viewModelScope.launch {
            repo.getLanguageReports(accessToken).collectLatest {
                _languageReportsUIState.value = it
            }
        }
    }





}