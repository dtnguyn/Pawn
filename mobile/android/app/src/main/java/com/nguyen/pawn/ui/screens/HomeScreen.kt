package com.nguyen.pawn.ui.screens

import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nguyen.pawn.model.User
import com.nguyen.pawn.ui.viewmodels.AuthViewModel
import com.nguyen.pawn.ui.viewmodels.WordViewModel
import com.nguyen.pawn.util.DataStoreUtils.getAccessTokenFromDataStore
import com.nguyen.pawn.util.DataStoreUtils.getRefreshTokenFromDataStore
import com.nguyen.pawn.util.UtilFunction.convertHeightToDp
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch


@ExperimentalAnimationApi
@ExperimentalMaterialApi
@ExperimentalFoundationApi
@OptIn(ExperimentalPagerApi::class)
@Composable
fun HomeScreen(wordViewModel: WordViewModel, authViewModel: AuthViewModel, navController: NavController) {

    val words: ArrayList<Word> by wordViewModel.dailyWords

    val savedWords: ArrayList<Word> by wordViewModel.savedWords

    val pagerState = rememberPagerState(pageCount = words.size)
    val coroutineScope = rememberCoroutineScope()
    val user: User? by authViewModel.user.observeAsState()
    val context = LocalContext.current
    val homeScaffoldState: ScaffoldState = rememberScaffoldState()



    LaunchedEffect(homeScaffoldState) {
        if(user == null) {
            val accessToken = getAccessTokenFromDataStore(context)
            val refreshToken = getRefreshTokenFromDataStore(context)
            Log.d("Auth", "Check access token: ${accessToken}")
            Log.d("Auth", "Check refresh token: = ${refreshToken}")
            authViewModel.checkAuthStatus(accessToken, refreshToken)
        }
    }

    Surface(
        color = AlmostBlack,
    ) {

        Scaffold(
            Modifier
                .fillMaxHeight()
                .fillMaxWidth(),
            scaffoldState = homeScaffoldState

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
//                            .clickable {
//                                coroutineScope.launch {
//                                    val accessToken = getAccessTokenFromDataStore(context)
//                                    val refreshToken = getRefreshTokenFromDataStore(context)
//                                    Log.d("Auth", "Check access token: ${accessToken}")
//                                    Log.d("Auth", "Check refresh token: = ${refreshToken}")
//                                }
//                            },
                        elevation = 10.dp,

                        ) {

                        LazyColumn(Modifier.padding(bottom = 50.dp)) {

                            item {
                                DailyWordSection(
                                    viewModel = wordViewModel,
                                    pagerState = pagerState,
                                    navController = navController,
                                    words = words,
                                    savedWords = savedWords
                                )
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
                                    index = index,
                                    onClick = {
                                        navController.navigate("word")
                                    }
                                )
                            }
                        }
                    }
                },
                scaffoldState = bottomSheetScaffoldState,
                topBar = {
                    HomeAppBar(navController, user, onLogout = {
                        authViewModel.logout(it)
                    })
                },

                sheetPeekHeight = (convertHeightToDp(
                    LocalContext.current.resources.displayMetrics.heightPixels,
                    LocalContext.current.resources.displayMetrics
                )).dp - 150.dp,

                ) {

            }


        }
    }
}


