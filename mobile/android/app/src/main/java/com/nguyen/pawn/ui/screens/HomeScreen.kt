package com.nguyen.pawn.ui.screens

import android.content.res.Resources
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.lifecycle.MutableLiveData
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.coroutines.launch

@ExperimentalMaterialApi
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
    var height = remember { MutableLiveData<Dp>() }

    Surface(
        color = AlmostBlack,
    ) {
        Scaffold(
            Modifier
//                .fillMaxHeight()
                .fillMaxWidth()
                .onSizeChanged {
                    height.value = it.height.dp
                }
        ) {

            Column() {
                val scope = rememberCoroutineScope()
                val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
                    bottomSheetState = rememberBottomSheetState(
                        initialValue = BottomSheetValue.Collapsed
                    )
                )
                BottomSheetScaffold(
                    sheetShape = RoundedCornerShape(topStart = 60.dp, topEnd = 60.dp),
                    sheetContent = {
                        Card(
                            shape = RoundedCornerShape(topStart = 60.dp, topEnd = 60.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(),
                            elevation = 10.dp,

                        ) {
                            LazyColumn(Modifier.padding(bottom = 50.dp)) {
                                item {
                                    DailyWordSection(pagerState = pagerState, words = words)
                                }

                                stickyHeader {
                                    Column(
                                        Modifier
                                            .background(Color.White)
                                            .padding(start = 30.dp, top = 20.dp)
                                            .fillMaxWidth()
                                    ) {
                                        Text(
                                            text = "Your saved words ${Resources.getSystem().displayMetrics.heightPixels.dp}",
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

                                items(savedWords.size) { index ->
                                    SavedWordItem(
                                        word = savedWords[index].value,
                                        pronunciation = savedWords[index].pronunciation,
                                        index = index
                                    )
                                }
                            }
                        }
                    },
                    scaffoldState = bottomSheetScaffoldState,
                    topBar = {
                        HomeAppBar()
                    },

                    sheetPeekHeight = 670.dp,
                    drawerContent = {
                        Column(
                            Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                                .background(Purple200),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("Drawer content")
                            Spacer(Modifier.height(20.dp))
                            Button(onClick = { scope.launch { bottomSheetScaffoldState.drawerState.close() } }) {
                                Text("Click to close drawer")
                            }
                        }
                    }
                ) { innerPadding ->

                }









            }
        }
    }
}