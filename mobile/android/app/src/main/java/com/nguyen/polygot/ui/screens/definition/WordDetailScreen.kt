package com.nguyen.polygot.ui.screens

import android.util.Log
import androidx.compose.animation.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.nguyen.polygot.R
import com.nguyen.polygot.model.WordDetail
import com.nguyen.polygot.ui.components.RoundButton
import com.nguyen.polygot.ui.components.RoundedSquareButton
import com.nguyen.polygot.ui.components.word.DefinitionItem
import com.nguyen.polygot.ui.components.word.WordCollapseSection
import com.nguyen.polygot.ui.components.word.WordTopBar
import com.nguyen.polygot.ui.screens.definition.WordDetailViewModel
import com.nguyen.polygot.ui.theme.*
import com.nguyen.polygot.util.UIState
import androidx.compose.runtime.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import com.nguyen.polygot.model.Word
import com.nguyen.polygot.ui.SharedViewModel
import com.nguyen.polygot.util.DataStoreUtils
import com.nguyen.polygot.util.ShimmerAnimation
import com.nguyen.polygot.util.UtilFunctions
import kotlinx.coroutines.launch

private const val TAG = "WordDetailScreen"

@ExperimentalAnimationApi
@ExperimentalFoundationApi
@Composable
fun WordDetailScreen(
    navController: NavController,
    viewModel: WordDetailViewModel,
    sharedViewModel: SharedViewModel,
    wordValue: String?,
    language: String?
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val lazyListState = rememberLazyListState()
    val wordDetailUIState: UIState<WordDetail> by viewModel.wordDetailUIState
    val savedEnWordsUIState: UIState<List<Word>> by sharedViewModel.savedEnWordsUIState
    val savedEsWordsUIState: UIState<List<Word>> by sharedViewModel.savedEsWordsUIState
    val savedFrWordsUIState: UIState<List<Word>> by sharedViewModel.savedFrWordsUIState
    val savedDeWordsUIState: UIState<List<Word>> by sharedViewModel.savedDeWordsUIState
    var wordDetail by remember { mutableStateOf(wordDetailUIState.value) }
    var isSaved by remember {
        mutableStateOf(
            sharedViewModel.checkIsSaved(
                wordValue ?: "",
                language
            )
        )
    }
    var loading by remember { mutableStateOf(false) }

    LaunchedEffect(null) {
        viewModel.getWordDetail(wordValue, language)
    }

    LaunchedEffect(wordDetailUIState) {
        when (wordDetailUIState) {
            is UIState.Initial -> {
                loading = true
            }
            is UIState.Loading -> {
                loading = true
            }
            is UIState.Error -> {
                // Show error dialog
                loading = false
                Log.d(TAG, "error ${wordDetailUIState.errorMsg}")
            }
            is UIState.Loaded -> {
                if (wordDetailUIState.value != null) {
                    loading = false
                    wordDetail = wordDetailUIState.value
                }
            }
        }
    }

    LaunchedEffect(
        savedEnWordsUIState,
        savedEsWordsUIState,
        savedDeWordsUIState,
        savedFrWordsUIState
    ) {
        isSaved = sharedViewModel.checkIsSaved(wordValue ?: "", language)
    }


    Surface {

        Scaffold(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(),
            backgroundColor = Color.White,
            topBar = {
                WordTopBar(
                    lazyListState = lazyListState,
                    word = wordDetail?.value ?: "",
                    onBackClick = { navController.popBackStack() })
            }
        ) {
            WordCollapseSection(wordDetail, loading)
            LazyColumn(
                modifier = Modifier.fillMaxHeight(),
                state = lazyListState,
            ) {

                item {
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp),
                    )
                }

                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .zIndex(1f)
                            .background(Color.White)
                            .offset(y = (-35).dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        RoundedSquareButton(backgroundColor = LightGreen, icon = R.drawable.copy) {}
                        RoundButton(
                            backgroundColor = LightGrey,
                            size = 70.dp,
                            icon = R.drawable.speaker,
                            padding = 10.dp
                        ) {}
                        RoundedSquareButton(
                            backgroundColor = LightRed,
                            icon = if (isSaved) R.drawable.heart_red else R.drawable.heart
                        ) {
                            wordDetail?.let {
                                coroutineScope.launch {
                                    sharedViewModel.toggleSavedWord(
                                        Word(
                                            value = it.value,
                                            language = it.language,
                                            mainDefinition = if (it.definitions.isNotEmpty()) it.definitions.first().meaning else "",
                                            pronunciationAudio = if (it.pronunciations.isNotEmpty()) it.pronunciations.first().audio else null,
                                            pronunciationSymbol = if (it.pronunciations.isNotEmpty()) it.pronunciations.first().symbol else null
                                        ),
                                        DataStoreUtils.getAccessTokenFromDataStore(context),
                                        it.language
                                    )
                                }
                            }
                        }
                    }
                }
                if (loading) {
                    items(3) { index ->
                        Card(
                            shape = RoundedCornerShape(15.dp),
                            elevation = 4.dp,
                            backgroundColor = UtilFunctions.generateRandomPastelColor(),
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.White)
                                .padding(15.dp)
                                .clip(RoundedCornerShape(15.dp))
                        ) {

                            Column(
                                Modifier.padding(15.dp)
                            ) {
                                ShimmerAnimation(
                                    modifier = Modifier
                                        .width(200.dp)
                                        .height(30.dp),
                                    shape = RoundedCornerShape(30.dp)
                                )
                                Spacer(modifier = Modifier.padding(2.dp))
                                ShimmerAnimation(
                                    modifier = Modifier
                                        .width(130.dp)
                                        .height(30.dp),
                                    shape = RoundedCornerShape(30.dp)
                                )
                                Spacer(modifier = Modifier.padding(10.dp))
                                ShimmerAnimation(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(50.dp),
                                    shape = RoundedCornerShape(30.dp)
                                )
                                Spacer(modifier = Modifier.padding(2.dp))

                                ShimmerAnimation(
                                    modifier = Modifier
                                        .width(150.dp)
                                        .height(50.dp),
                                    shape = RoundedCornerShape(30.dp)
                                )
                                Spacer(modifier = Modifier.padding(10.dp))
                                ShimmerAnimation(
                                    modifier = Modifier
                                        .width(200.dp)
                                        .height(30.dp),
                                    shape = RoundedCornerShape(30.dp)
                                )
                                Spacer(modifier = Modifier.padding(5.dp))
                                ShimmerAnimation(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(30.dp),
                                    shape = RoundedCornerShape(30.dp)
                                )
                                Spacer(modifier = Modifier.padding(2.dp))
                                ShimmerAnimation(
                                    modifier = Modifier
                                        .width(150.dp)
                                        .height(30.dp),
                                    shape = RoundedCornerShape(30.dp)
                                )
                            }
                        }
                    }
                } else {
                    wordDetail?.let {
                        if (it.definitions.isNotEmpty()) {
                            items(it.definitions.size) { index ->
                                DefinitionItem(
                                    index = index,
                                    definition = it.definitions[index]
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}