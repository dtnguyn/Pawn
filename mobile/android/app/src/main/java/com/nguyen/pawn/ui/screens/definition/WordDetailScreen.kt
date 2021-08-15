package com.nguyen.pawn.ui.screens

import android.util.Log
import androidx.compose.animation.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
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
import com.nguyen.pawn.R
import com.nguyen.pawn.model.Definition
import com.nguyen.pawn.model.WordDetail
import com.nguyen.pawn.ui.components.RoundButton
import com.nguyen.pawn.ui.components.RoundedSquareButton
import com.nguyen.pawn.ui.components.word.DefinitionItem
import com.nguyen.pawn.ui.components.word.WordCollapseSection
import com.nguyen.pawn.ui.components.word.WordTopBar
import com.nguyen.pawn.ui.screens.definition.WordDetailViewModel
import com.nguyen.pawn.ui.theme.*
import com.nguyen.pawn.util.UIState
import androidx.compose.runtime.*
import com.nguyen.pawn.model.Language

private const val TAG = "WordDetailScreen"

@ExperimentalAnimationApi
@ExperimentalFoundationApi
@Composable
fun WordDetailScreen(
    navController: NavController,
    viewModel: WordDetailViewModel,
    wordValue: String?,
    language: String?
) {


    val lazyListState = rememberLazyListState()
    val wordDetailUIState: UIState<WordDetail> by viewModel.wordDetailUIState
    var wordDetail by remember { mutableStateOf(wordDetailUIState.value) }

    LaunchedEffect(null) {
        viewModel.getWordDetail(wordValue, language)
    }

    LaunchedEffect(wordDetailUIState) {
        when (wordDetailUIState) {
            is UIState.Initial -> {
                // Do nothing
            }
            is UIState.Loading -> {
                // Display loading animation
            }
            is UIState.Error -> {
                // Show error dialog
                Log.d(TAG, "error ${wordDetailUIState.errorMsg}")
            }
            is UIState.Loaded -> {
                if (wordDetailUIState.value != null) {
                    Log.d(TAG, "word Detail ${wordDetailUIState.value}")
                    wordDetail = wordDetailUIState.value
                }
            }
        }
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
            WordCollapseSection(wordDetail)
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
                        RoundedSquareButton(backgroundColor = LightRed, icon = R.drawable.heart) {}
                    }
                }

                wordDetail?.let {
                    if(it.definitions.isNotEmpty()){
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