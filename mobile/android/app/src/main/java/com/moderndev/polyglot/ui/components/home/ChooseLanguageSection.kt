package com.moderndev.polyglot.ui.components.home

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.moderndev.polyglot.model.Language
import com.moderndev.polyglot.util.Constants


@Composable
fun ChooseLanguageSection(pickedLanguages: List<Language>, onFinish: (newPickedLanguages: List<Language>) -> Unit){
    var pickedLanguageMap = HashMap<String, Boolean>()
    var displayPickedLanguages by remember {
        mutableStateOf(pickedLanguages.map {
            pickedLanguageMap[it.id] = true
            it
        }
    )}


    LazyColumn(Modifier.padding(bottom = 50.dp)) {
        item {
            ChooseLanguagesHeader(pickedLanguages = displayPickedLanguages) {
                onFinish(displayPickedLanguages)
            }
        }

        items(Constants.supportedLanguages.size) { index ->
            LanguageItem(
                language = Constants.supportedLanguages[index],
                isPicked = (displayPickedLanguages.filter { Constants.supportedLanguages[index].id == it.id }).isNotEmpty()
            ) { language ->
                displayPickedLanguages = if(pickedLanguageMap[language.id] == true){
                    displayPickedLanguages.filter { it.id != language.id }
                } else {
                    displayPickedLanguages + arrayListOf(language)
                }
                pickedLanguageMap[language.id] = pickedLanguageMap[language.id]?.not() ?: true
            }
        }
    }
}