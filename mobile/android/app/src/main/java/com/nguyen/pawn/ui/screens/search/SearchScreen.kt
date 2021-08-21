package com.nguyen.pawn.ui.screens

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.nguyen.pawn.model.Language
import com.nguyen.pawn.ui.SharedViewModel
import com.nguyen.pawn.ui.components.SavedWordItem
import com.nguyen.pawn.ui.navigation.PawnScreens
import com.nguyen.pawn.ui.screens.search.SearchViewModel
import com.nguyen.pawn.ui.theme.Typography
import com.nguyen.pawn.util.UIState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SearchScreen(searchViewModel: SearchViewModel, sharedViewModel: SharedViewModel, navController: NavController) {


    val autoCompleteWordsUIState by searchViewModel.autoCompleteWordsUIState
    var autoCompleteWords by remember { mutableStateOf(autoCompleteWordsUIState.value) }

    var searchValue by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }
    var searchJob: Job? = null
    val coroutineScope = rememberCoroutineScope()

    val currentPickedLanguage: Language? by sharedViewModel.currentPickedLanguage

    LaunchedEffect(autoCompleteWordsUIState) {
        when (autoCompleteWordsUIState) {
            is UIState.Initial -> {

            }
            is UIState.Loading -> {
                loading = true
            }
            is UIState.Error -> {
                loading = false
                autoCompleteWords = listOf()
            }
            is UIState.Loaded -> {
                loading = false
                autoCompleteWords = autoCompleteWordsUIState.value
            }

        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth(),
        backgroundColor = Color.White,
    ) {
        Column(Modifier.padding(20.dp)) {
            Text(text = "Search words", style = Typography.h1)
            TextField(
                value = searchValue,
                onValueChange = { newValue ->
                    searchValue = newValue
                    searchJob?.cancel()
                    searchJob = coroutineScope.launch {
                        delay(1000L)
                        searchViewModel.searchWords(searchValue, currentPickedLanguage?.id)
                    }
                },
                placeholder = {
                    Text(
                        text = "Enter words...",
                        style = Typography.body2,
                        color = Color.Gray
                    )
                },
                singleLine = true,
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                ),
                shape = RoundedCornerShape(40.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp)
                    .aspectRatio(5f)
            )
            autoCompleteWords?.let { words ->
                LazyColumn {
                    items(words.size) { index ->
                        val word = words[index]
                        SavedWordItem(
                            word = word.value,
                            index = index,
                            onClick = {
                                navController.navigate("${PawnScreens.WordDetail.route}/${words[index].value}/${currentPickedLanguage?.id}")
                            }
                        )
                    }
                }
            }

        }
    }
}