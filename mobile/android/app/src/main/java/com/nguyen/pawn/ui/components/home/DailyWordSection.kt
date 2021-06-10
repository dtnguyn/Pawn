package com.nguyen.pawn.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.PagerState
import com.nguyen.pawn.R
import com.nguyen.pawn.model.Word
import com.nguyen.pawn.ui.theme.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun DailyWordSection(pagerState: PagerState, navController: NavController, words: List<Word>) {

    val coroutineScope = rememberCoroutineScope()

    Column {
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
                word = words[page].value,
                definition = words[page].definition,
                pronunciation = words[page].pronunciation,
                onClick = {
                    navController.navigate("Word")

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
                    val page = if(pagerState.currentPage == 0) 0 else pagerState.currentPage - 1
                    pagerState.animateScrollToPage(page)
                }
            })
            RoundedSquareButton(Grey, R.drawable.trash, onClick = {})
            RoundedSquareButton(LightRed, R.drawable.heart, onClick = {})
            RoundedSquareButton(LightOrange, R.drawable.next2, onClick = {
                coroutineScope.launch {
                    val page = if(pagerState.currentPage == 2) 2 else pagerState.currentPage + 1
                    pagerState.animateScrollToPage(page)
                }
            })
        }

    }
}
