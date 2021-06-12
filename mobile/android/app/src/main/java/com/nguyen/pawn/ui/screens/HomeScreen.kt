package com.nguyen.pawn.ui.screens

import android.util.DisplayMetrics
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.rememberPagerState
import com.nguyen.pawn.R
import com.nguyen.pawn.model.Word
import com.nguyen.pawn.ui.components.DailyWordSection
import com.nguyen.pawn.ui.components.HomeAppBar
import com.nguyen.pawn.ui.components.RoundButton
import com.nguyen.pawn.ui.components.SavedWordItem
import com.nguyen.pawn.ui.theme.AlmostBlack
import com.nguyen.pawn.ui.theme.Blue
import com.nguyen.pawn.ui.theme.Grey
import com.nguyen.pawn.ui.theme.Typography
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.nguyen.pawn.ui.viewmodels.HomeViewModel


@ExperimentalAnimationApi
@ExperimentalMaterialApi
@ExperimentalFoundationApi
@OptIn(ExperimentalPagerApi::class)
@Composable
fun HomeScreen(viewModel: HomeViewModel, navController: NavController) {

    val words: ArrayList<Word> by viewModel.dailyWords

    val savedWords: ArrayList<Word> by viewModel.savedWords

    val pagerState = rememberPagerState(pageCount = words.size)


    Surface(
        color = AlmostBlack,
    ) {
        Scaffold(
            Modifier
                .fillMaxHeight()
                .fillMaxWidth()

        ) {

            val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
                bottomSheetState = rememberBottomSheetState(
                    initialValue = BottomSheetValue.Collapsed,
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
                            println("Test: ${words.size}")
                            if (words.isNotEmpty()) {
                                item {
                                    DailyWordSection(
                                        pagerState = pagerState,
                                        navController = navController,
                                        words = words,
                                        savedWords = savedWords
                                    )
                                }
                            }


                            stickyHeader {
                                Column(
                                    Modifier
                                        .background(Color.White)
                                        .padding(start = 30.dp, top = 20.dp)
                                        .fillMaxWidth()
                                ) {
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

                sheetPeekHeight = (convertHeightToDp(
                    LocalContext.current.resources.displayMetrics.heightPixels,
                    LocalContext.current.resources.displayMetrics
                )).dp - 140.dp,

                ) {

            }


        }
    }
}


fun convertHeightToDp(pixel: Int, displayMetrics: DisplayMetrics): Int {
    return (pixel / (displayMetrics.density)).toInt()
}