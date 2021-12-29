package com.nguyen.polyglot.ui.components

import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.accompanist.pager.*
import com.nguyen.polyglot.R
import com.nguyen.polyglot.model.Word
import com.nguyen.polyglot.ui.components.home.DailyWordCard
import com.nguyen.polyglot.ui.theme.*
import kotlinx.coroutines.launch

private const val TAG = "DailyWordSection"

@ExperimentalAnimationApi
@OptIn(ExperimentalPagerApi::class)
@Composable
fun DailyWordSection(
    navController: NavController,
    words: List<Word>,
    isLoading: Boolean,
    onDailyWordClick: (wordValue: String) -> Unit,
    onToggleSaveWord: (word: Word) -> Unit,
    onRemoveWord: (word: Word) -> Unit,
    checkIsSaved: (wordValue: String) -> Boolean
) {

    val pagerState = rememberPagerState(pageCount = words.size)

    val coroutineScope = rememberCoroutineScope()
    val isSaved = remember { mutableStateOf(false) }

    Log.d(TAG, "isLoadingDailyWords: $isLoading")
//    if (words.size > 0 || isLoading) {
    Column {
        if (isLoading) {
            Text(
                text = "Daily words",
                modifier = Modifier.padding(
                    horizontal = 30.dp,
                    vertical = 20.dp
                ),
                style = Typography.h6,
                fontSize = 18.sp

            )
            DailyWordCard(
                isLoading = isLoading,
                word = "",
                definition = "",
                onClick = {}
            )
        } else if (words.isNotEmpty()) {
            Text(
                text = "Daily words",
                modifier = Modifier.padding(
                    horizontal = 30.dp,
                    vertical = 20.dp
                ),
                style = Typography.h6,
                fontSize = 18.sp

            )
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxWidth()
            ) { page ->
                DailyWordCard(
                    isLoading = isLoading,
                    word = words[page].value,
                    definition = words[page].mainDefinition,
                    pronunciationSymbol = words[page].pronunciationSymbol,
                    pronunciationAudio = words[page].pronunciationAudio,
                    onClick = {
                        onDailyWordClick(words[page].value)
                    }
                )
            }
            HorizontalPagerIndicator(
                pagerState = pagerState,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 30.dp, bottom = 10.dp)
            )
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp)
            ) {
                RoundedSquareButton(LightGreen, R.drawable.next2, onClick = {
                    coroutineScope.launch {
                        val page =
                            if (pagerState.currentPage == 0) 0 else pagerState.currentPage - 1
                        pagerState.animateScrollToPage(page)
                    }
                })
                RoundedSquareButton(Grey, R.drawable.trash) {
                    onRemoveWord(words[pagerState.currentPage])
                }
                RoundedSquareButton(
                    LightRed,
                    icon = if (checkIsSaved(words[pagerState.currentPage].value)) R.drawable.heart_red else R.drawable.heart,
                    onClick = {
                        onToggleSaveWord(words[pagerState.currentPage])
                    })


                RoundedSquareButton(LightOrange, R.drawable.next2_right, onClick = {
                    coroutineScope.launch {
                        val nextPage =
                            if (pagerState.currentPage == words.size - 1) 0 else pagerState.currentPage + 1
                        pagerState.animateScrollToPage(nextPage)
                    }
                })

            }
        }


    }
}
