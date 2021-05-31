package com.nguyen.pawn.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import com.nguyen.pawn.R
import com.nguyen.pawn.model.Word
import com.nguyen.pawn.ui.components.*
import com.nguyen.pawn.ui.theme.*
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.graphics.Color

@ExperimentalFoundationApi
@OptIn(ExperimentalPagerApi::class)
@Preview
@Composable
fun HomeScreen() {

    val words = listOf(
        Word(
            "Pepper",
            "A pungent, hot-tasting powder prepared from dried and ground peppercorns, commonly used as a spice or condiment to flavor food.",
            "/ˈpepə(r)/"
        ),
        Word(
            "Pepper",
            "A pungent, hot-tasting powder prepared from dried and ground peppercorns, commonly used as a spice or condiment to flavor food.",
            "/ˈpepə(r)/"
        ),
        Word(
            "Pepper",
            "A pungent, hot-tasting powder prepared from dried and ground peppercorns, commonly used as a spice or condiment to flavor food.",
            "/ˈpepə(r)/"
        ),
    )

    val savedWords = listOf(
        Word(
            "Phenomenal",
            "A pungent, hot-tasting powder prepared from dried and ground peppercorns, commonly used as a spice or condiment to flavor food.",
            "/fəˈnɑːmɪnl/"
        ),
        Word(
            "Phenomenal",
            "A pungent, hot-tasting powder prepared from dried and ground peppercorns, commonly used as a spice or condiment to flavor food.",
            "/fəˈnɑːmɪnl/"
        ),
        Word(
            "Phenomenal",
            "A pungent, hot-tasting powder prepared from dried and ground peppercorns, commonly used as a spice or condiment to flavor food.",
            "/fəˈnɑːmɪnl/"
        ),
        Word(
            "Phenomenal",
            "A pungent, hot-tasting powder prepared from dried and ground peppercorns, commonly used as a spice or condiment to flavor food.",
            "/fəˈnɑːmɪnl/"
        ),
        Word(
            "Phenomenal",
            "A pungent, hot-tasting powder prepared from dried and ground peppercorns, commonly used as a spice or condiment to flavor food.",
            "/fəˈnɑːmɪnl/"
        ),
        Word(
            "Phenomenal",
            "A pungent, hot-tasting powder prepared from dried and ground peppercorns, commonly used as a spice or condiment to flavor food.",
            "/fəˈnɑːmɪnl/"
        ),
        Word(
            "Phenomenal",
            "A pungent, hot-tasting powder prepared from dried and ground peppercorns, commonly used as a spice or condiment to flavor food.",
            "/fəˈnɑːmɪnl/"
        ),
        Word(
            "Phenomenal",
            "A pungent, hot-tasting powder prepared from dried and ground peppercorns, commonly used as a spice or condiment to flavor food.",
            "/fəˈnɑːmɪnl/"
        ),
        Word(
            "Phenomenal",
            "A pungent, hot-tasting powder prepared from dried and ground peppercorns, commonly used as a spice or condiment to flavor food.",
            "/fəˈnɑːmɪnl/"
        ),
        Word(
            "Phenomenal",
            "A pungent, hot-tasting powder prepared from dried and ground peppercorns, commonly used as a spice or condiment to flavor food.",
            "/fəˈnɑːmɪnl/"
        ),
        Word(
            "Phenomenal",
            "A pungent, hot-tasting powder prepared from dried and ground peppercorns, commonly used as a spice or condiment to flavor food.",
            "/fəˈnɑːmɪnl/"
        ),


        )

    val pagerState = rememberPagerState(pageCount = words.size)

    Surface(
        color = AlmostBlack,
    ) {
        Scaffold(
            Modifier
                .fillMaxHeight()
                .fillMaxWidth()
        ) {
            Column {
                HomeAppBar()
                Card(
                    shape = RoundedCornerShape(topStart = 60.dp, topEnd = 60.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    elevation = 10.dp
                ) {
                    LazyColumn(Modifier.padding(bottom = 50.dp)) {
                        item {
                            Column {
                                Text(
                                    text = "Daily words",
                                    modifier = Modifier.padding(horizontal = 30.dp, vertical = 20.dp),
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
                                        pronunciation = words[page].pronunciation
                                    )
                                }
                                HorizontalPagerIndicator(
                                    pagerState = pagerState,
                                    modifier = Modifier
                                        .align(Alignment.CenterHorizontally)
                                        .padding(30.dp)
                                )


                                Row(
                                    horizontalArrangement = Arrangement.SpaceEvenly,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    DailyWordButton(LightGreen, R.drawable.next2, onClick = {})
                                    DailyWordButton(Grey, R.drawable.trash, onClick = {})
                                    DailyWordButton(LightRed, R.drawable.heart, onClick = {})
                                    DailyWordButton(LightOrange, R.drawable.next2, onClick = {})
                                }

                            }
                        }

                        stickyHeader {
                            Column(Modifier.background(Color.White).padding(start = 30.dp, top = 20.dp).fillMaxWidth()) {
                                Text(
                                    text = "Your saved words",
                                    style = Typography.h6,
                                    fontSize = 18.sp
                                )

                                Row(Modifier.padding(vertical = 5.dp)) {
                                    RoundButton(
                                        backgroundColor = Blue,
                                        size = 32.dp,
                                        icon = R.drawable.add,
                                        onClick = {})
                                    Spacer(modifier = Modifier.padding(horizontal = 5.dp))
                                    RoundButton(
                                        backgroundColor = Grey,
                                        size = 32.dp,
                                        icon = R.drawable.review,
                                        onClick = {})
                                }
                            }
                        }

                        items(savedWords.size){index ->
                            SavedWordItem(word = savedWords[index].value, pronunciation = savedWords[index].pronunciation, index = index)
                        }
                    }

                }


            }

        }
    }
}