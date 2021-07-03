package com.nguyen.pawn.ui.screens

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import com.nguyen.pawn.model.Language
import com.nguyen.pawn.model.User
import com.nguyen.pawn.ui.components.home.ChooseLanguagesHeader
import com.nguyen.pawn.ui.components.home.LanguageItem
import com.nguyen.pawn.ui.theme.*
import com.nguyen.pawn.ui.viewmodels.AuthViewModel
import com.nguyen.pawn.ui.viewmodels.WordViewModel
import com.nguyen.pawn.util.Constants.supportedLanguages
import com.nguyen.pawn.util.DataStoreUtils.getAccessTokenFromDataStore
import com.nguyen.pawn.util.DataStoreUtils.getRefreshTokenFromDataStore
import com.nguyen.pawn.util.UtilFunctions.convertHeightToDp
import com.nguyen.pawn.util.UtilFunctions.generateFlagForLanguage
import kotlinx.coroutines.launch


@ExperimentalAnimationApi
@ExperimentalMaterialApi
@ExperimentalFoundationApi
@OptIn(ExperimentalPagerApi::class)
@Composable
fun HomeScreen(
    wordViewModel: WordViewModel,
    authViewModel: AuthViewModel,
    navController: NavController
) {

    val words: ArrayList<Word> by wordViewModel.dailyWords

    val savedWords: ArrayList<Word> by wordViewModel.savedWords
    val pickedLanguages: ArrayList<Language> by wordViewModel.pickedLanguages
    val currentPickedLanguage: Language? by wordViewModel.currentPickedLanguage
    val user: User? by authViewModel.user.observeAsState()
    var showAddLanguageMenu by remember { mutableStateOf(true) }


    val coroutineScope = rememberCoroutineScope()
    val pagerState = rememberPagerState(pageCount = words.size)
    val context = LocalContext.current
    val homeScaffoldState: ScaffoldState = rememberScaffoldState()



    LaunchedEffect(user) {
        if (user == null) {
            authViewModel.checkAuthStatus(
                getAccessTokenFromDataStore(context),
                getRefreshTokenFromDataStore(context)
            )
        } else {
            wordViewModel.initializePickedLanguages(user!!.learningLanguages)
            showAddLanguageMenu = user!!.learningLanguages.isEmpty()
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
                        elevation = 10.dp,

                        ) {

                        LazyColumn(Modifier.padding(bottom = 50.dp)) {
                            if (showAddLanguageMenu) {
                                item {
                                    ChooseLanguagesHeader(pickedLanguages = pickedLanguages) {
                                        coroutineScope.launch {
                                            showAddLanguageMenu = false
                                            wordViewModel.savePickedLanguages(
                                                pickedLanguages,
                                                getAccessTokenFromDataStore(context)
                                            )
                                        }

                                    }
                                }

                                items(supportedLanguages.size) { index ->
                                    LanguageItem(
                                        language = supportedLanguages[index],
                                        isPicked = pickedLanguages.filter {
                                            it.id == supportedLanguages[index].id
                                        }.size == 1
                                    ) { language ->
                                        wordViewModel.togglePickedLanguage(language)
                                    }
                                }


                            } else {
                                if (pickedLanguages.isNotEmpty()) {
                                    item {
                                        Row(
                                            modifier = Modifier.padding(
                                                start = 30.dp,
                                                end = 30.dp,
                                                top = 30.dp
                                            ),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            pickedLanguages.forEach { language ->
                                                Image(
                                                    painter = painterResource(
                                                        id = generateFlagForLanguage(
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
                                                            wordViewModel.changeCurrentPickedLanguage(
                                                                language
                                                            )
                                                        }
                                                )
                                            }
                                            Spacer(modifier = Modifier.padding(horizontal = 5.dp))
                                            RoundButton(backgroundColor = Grey, size = 38.dp , icon = R.drawable.add_32_black) {
                                                showAddLanguageMenu = true
                                            }
                                        }
                                    }
                                }

                                item {
                                    DailyWordSection(
                                        viewModel = wordViewModel,
                                        pagerState = pagerState,
                                        navController = navController,
                                        words = words,
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


