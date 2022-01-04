package com.nguyen.polyglot.ui.components.feedDetail.video

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.nguyen.polyglot.model.SubtitlePart
import com.nguyen.polyglot.ui.components.SelectableText
import com.nguyen.polyglot.ui.theme.Neon
import com.nguyen.polyglot.ui.theme.Typography
import com.nguyen.polyglot.util.UtilFunctions.reformatString
import kotlinx.coroutines.launch
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import com.nguyen.polyglot.R
import com.nguyen.polyglot.model.Word
import com.nguyen.polyglot.ui.SharedViewModel
import com.nguyen.polyglot.ui.components.feedDetail.news.WordDefinition
import com.nguyen.polyglot.ui.navigation.PolyglotScreens
import com.nguyen.polyglot.ui.screens.videoDetail.VideoDetailViewModel
import com.nguyen.polyglot.ui.theme.LightGreen
import com.nguyen.polyglot.ui.theme.LightRed
import com.nguyen.polyglot.util.DataStoreUtils
import com.nguyen.polyglot.util.UIState

@Composable
fun FocusSubtitleMenu(
    isOpen: Boolean,
    subtitlePart: SubtitlePart?,
    viewModel: VideoDetailViewModel,
    sharedViewModel: SharedViewModel,
    moveToWordDetail: (word: String) -> Unit
) {

    val language by sharedViewModel.currentPickedLanguage

    var currentFocusWord: String? by remember { mutableStateOf(null) }

    val wordDefinitionUIState: UIState<Word> by viewModel.wordDefinitionUIState
    var wordDefinition by remember { mutableStateOf(wordDefinitionUIState.value) }

    var isFindingDefinition by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current


    LaunchedEffect(isOpen) {
        if (!isOpen) {
            //When the bottom sheet is closed
            currentFocusWord = null
        }
    }

    LaunchedEffect(wordDefinitionUIState) {
        when (wordDefinitionUIState) {
            is UIState.Initial -> {

            }
            is UIState.Loading -> {

            }
            is UIState.Error -> {

            }
            is UIState.Loaded -> {
                wordDefinition = wordDefinitionUIState.value
                Log.d("VideoDetailScreen", "definition: ${wordDefinitionUIState.value}")
            }
        }

    }


    when {
        isFindingDefinition -> {
            WordDefinition(
                word = wordDefinition,
                isLoading = wordDefinitionUIState is UIState.Loading,
                onBackClick = {
                    isFindingDefinition = false
                },
                onDetailClick = {
                    moveToWordDetail(wordDefinition?.value ?: "")
                }
            )
        }
        else -> {
            Column(Modifier.padding(20.dp)) {
                Text(text = stringResource(id = R.string.subtitle_action), style = Typography.h6)
                Card(
                    shape = RoundedCornerShape(30.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 30.dp, vertical = 15.dp),

                    backgroundColor = Neon,
                ) {
                    Column(Modifier.padding(30.dp)) {
                        Text(text = language?.id?.substring(0,2) ?: "", style = Typography.h6)
                        Spacer(modifier = Modifier.padding(5.dp))

                        SelectableText(
                            text = reformatString(subtitlePart?.text ?: ""),
                            isFocusing = currentFocusWord != null,
                            onLongClick = { word ->
                                if (word != "") {
                                    currentFocusWord = word
                                }
                            }
                        )
                    }
                }
                Spacer(modifier = Modifier.padding(10.dp))

                if (currentFocusWord == null) Text(
                    text = stringResource(id = R.string.subtitle_action_instruction),
                    style = Typography.body1
                )
                else {
                    Spacer(modifier = Modifier.padding(5.dp))
                    Button(
                        onClick = {
                            coroutineScope.launch {
                                viewModel.getWordDefinition(
                                    DataStoreUtils.getAccessTokenFromDataStore(
                                        context
                                    ), currentFocusWord, language?.id
                                )
                                isFindingDefinition = true
                            }
                        },
                        content = {
                            Text(text = "${stringResource(id = R.string.find_definition_for)} \"${currentFocusWord!!}\" ", style = Typography.h6)
                        },
                        shape = RoundedCornerShape(30.dp),
                        colors = ButtonDefaults.buttonColors(LightGreen),
                    )
                }


            }


        }
    }


}