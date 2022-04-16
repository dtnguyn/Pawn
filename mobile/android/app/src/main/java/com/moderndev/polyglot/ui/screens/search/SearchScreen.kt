package com.moderndev.polyglot.ui.screens

import android.widget.Toast
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.moderndev.polyglot.R
import com.moderndev.polyglot.model.Language
import com.moderndev.polyglot.ui.SharedViewModel
import com.moderndev.polyglot.ui.components.SavedWordItem
import com.moderndev.polyglot.ui.navigation.PolyglotScreens
import com.moderndev.polyglot.ui.screens.search.SearchViewModel
import com.moderndev.polyglot.ui.theme.*
import com.moderndev.polyglot.util.ShimmerAnimation
import com.moderndev.polyglot.util.UIState
import com.moderndev.polyglot.util.UtilFunctions
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


private const val TAG = "SearchScreen"

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SearchScreen(
    searchViewModel: SearchViewModel,
    sharedViewModel: SharedViewModel,
    navController: NavController
) {


    val autoCompleteWordsUIState by searchViewModel.autoCompleteWordsUIState
    var autoCompleteWords by remember { mutableStateOf(autoCompleteWordsUIState.value) }

    var searchValue by searchViewModel.searchValue
    var loading by remember { mutableStateOf(false) }
    var searchJob: Job? = null
    val coroutineScope = rememberCoroutineScope()
    val currentPickedLanguage: Language? by sharedViewModel.currentPickedLanguage
    val context = LocalContext.current

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
                Toast.makeText(context, autoCompleteWordsUIState.errorMsg, Toast.LENGTH_SHORT).show()

            }
            is UIState.Loaded -> {
                loading = false
                autoCompleteWords = autoCompleteWordsUIState.value
            }
        }
    }

    LaunchedEffect(currentPickedLanguage) {
        if (currentPickedLanguage != null &&
            searchValue.isNotBlank() &&
            currentPickedLanguage?.id != searchViewModel.lastSearchLanguage
        ) {
            searchViewModel.lastSearchLanguage = currentPickedLanguage!!.id
            searchViewModel.searchWords(searchValue, currentPickedLanguage?.id)
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth(),
        backgroundColor = Color.White,
    ) {
        Column() {
            Column(
                modifier = Modifier.padding(
                    top = 20.dp,
                    start = 20.dp,
                    end = 20.dp,
                    bottom = 0.dp
                )
            ) {
                Text(text = stringResource(id = R.string.search_words), style = Typography.h1)
                Row(
                    modifier = Modifier.padding(
                        top = 5.dp
                    ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    sharedViewModel.pickedLanguagesUIState.value.value?.forEach { language ->
                        Image(
                            painter = painterResource(
                                id = UtilFunctions.generateFlagForLanguage(
                                    language.id
                                )
                            ),
                            contentDescription = "language icon",
                            modifier = Modifier
                                .padding(horizontal = 5.dp)
                                .size(if (language.id == currentPickedLanguage?.id) 46.dp else 38.dp)
                                .clip(CircleShape)
                                .border(
                                    if (language.id == currentPickedLanguage?.id) 3.dp else 0.dp,
                                    DarkBlue,
                                    CircleShape
                                )
                                .clickable {
                                    sharedViewModel.changeCurrentPickedLanguage(
                                        language
                                    )
                                }
                        )
                    }
                }

                TextField(
                    value = searchValue,
                    onValueChange = { newValue ->
                        searchValue = newValue
                        searchJob?.cancel()
                        searchJob = coroutineScope.launch {
                            delay(1000L)
                            searchViewModel.lastSearchLanguage = currentPickedLanguage?.id ?: ""
                            searchViewModel.searchWords(searchValue, currentPickedLanguage?.id)
                        }
                    },
                    placeholder = {
                        Text(
                            text = stringResource(id = R.string.word_placholder),
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
            }

            Card(
                shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                backgroundColor = LightGrey,
                elevation = 4.dp
            ) {


                LazyColumn(Modifier.padding(start = 20.dp, end = 20.dp, bottom = 50.dp)) {
                    if (loading) {
                        items(10) { index ->
                            Card(
                                shape = RoundedCornerShape(15.dp),
                                modifier = Modifier
                                    .padding(vertical = 15.dp)
                                    .fillMaxWidth()
                                    .height(70.dp)
                                    .clip(RoundedCornerShape(15.dp)),
                                backgroundColor = UtilFunctions.generateRandomPastelColor()
                            ) {
                                ShimmerAnimation(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .fillMaxHeight(),
                                    shape = RoundedCornerShape(15.dp)
                                )
                            }
                        }
                    } else {
                        autoCompleteWords?.let { words ->
                            items(words.size) { index ->
                                val word = words[index]
                                SavedWordItem(
                                    word = word.value,
                                    index = index,
                                    onClick = {
                                        navController.navigate("${PolyglotScreens.WordDetail.route}/${words[index].value}/${currentPickedLanguage?.id}")
                                    }
                                )
                            }
                        }
                    }

                }

            }
        }
    }
}