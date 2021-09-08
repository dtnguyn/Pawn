package com.nguyen.pawn.ui.screens

import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.rememberPagerState
import com.nguyen.pawn.R
import com.nguyen.pawn.model.AuthStatus
import com.nguyen.pawn.model.Language
import com.nguyen.pawn.model.User
import com.nguyen.pawn.model.Word
import com.nguyen.pawn.ui.SharedViewModel
import com.nguyen.pawn.ui.components.DailyWordSection
import com.nguyen.pawn.ui.components.HomeAppBar
import com.nguyen.pawn.ui.components.RoundButton
import com.nguyen.pawn.ui.components.SavedWordItem
import com.nguyen.pawn.ui.components.home.ChooseLanguageSection
import com.nguyen.pawn.ui.navigation.PawnScreens
import com.nguyen.pawn.ui.screens.home.HomeViewModel
import com.nguyen.pawn.ui.theme.*
import com.nguyen.pawn.util.DataStoreUtils
import com.nguyen.pawn.util.DataStoreUtils.getAccessTokenFromDataStore
import com.nguyen.pawn.util.DataStoreUtils.getRefreshTokenFromDataStore
import com.nguyen.pawn.util.DataStoreUtils.getUserFromDataStore
import com.nguyen.pawn.util.SupportedLanguage
import com.nguyen.pawn.util.UIState
import com.nguyen.pawn.util.UtilFunctions.convertHeightToDp
import com.nguyen.pawn.util.UtilFunctions.generateFlagForLanguage
import kotlinx.coroutines.launch

private const val TAG = "HomeScreen"

@ExperimentalPagerApi
@ExperimentalAnimationApi
@ExperimentalMaterialApi
@ExperimentalFoundationApi
@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel,
    sharedViewModel: SharedViewModel,
    navController: NavController
) {

    /**   ---STATES---   */

    /** States from viewModel */
    val dailyEnWordsUIState: UIState<List<Word>> by homeViewModel.dailyEnWordsUIState
    var dailyEnWords by remember { mutableStateOf(dailyEnWordsUIState.value) }

    val dailyEsWordsUIState: UIState<List<Word>> by homeViewModel.dailyEsWordsUIState
    var dailyEsWords by remember { mutableStateOf(dailyEsWordsUIState.value) }

    val dailyFrWordsUIState: UIState<List<Word>> by homeViewModel.dailyFrWordsUIState
    var dailyFrWords by remember { mutableStateOf(dailyFrWordsUIState.value) }

    val dailyDeWordsUIState: UIState<List<Word>> by homeViewModel.dailyDeWordsUIState
    var dailyDeWords by remember { mutableStateOf(dailyDeWordsUIState.value) }

    val pickedLanguagesUIState: UIState<List<Language>> by sharedViewModel.pickedLanguagesUIState
    var pickedLanguages by remember { mutableStateOf(pickedLanguagesUIState.value) }
    var showAddLanguageMenu by remember { mutableStateOf(pickedLanguagesUIState.value?.isEmpty()) }

    val authStatusUIState: UIState<AuthStatus> by sharedViewModel.authStatusUIState
    var user by remember { mutableStateOf(authStatusUIState.value?.user) }

    val currentPickedLanguage: Language? by sharedViewModel.currentPickedLanguage

    val savedEnWordsUIState: UIState<List<Word>> by sharedViewModel.savedEnWordsUIState
    var savedEnWords by remember { mutableStateOf(savedEnWordsUIState.value) }

    val savedEsWordsUIState: UIState<List<Word>> by sharedViewModel.savedEsWordsUIState
    var savedEsWords by remember { mutableStateOf(savedEsWordsUIState.value) }

    val savedFrWordsUIState: UIState<List<Word>> by sharedViewModel.savedFrWordsUIState
    var savedFrWords by remember { mutableStateOf(savedFrWordsUIState.value) }

    val savedDeWordsUIState: UIState<List<Word>> by sharedViewModel.savedDeWordsUIState
    var savedDeWords by remember { mutableStateOf(savedDeWordsUIState.value) }


    /** Error states */
    var errorMsg by remember { mutableStateOf("") }
    var dailyWordCount by remember { mutableStateOf(3) }

    /** Local ui states */
    var isLoadingPickedLanguage by remember { mutableStateOf(true) }
    var isLoadingUser by remember { mutableStateOf(true) }
    var isLoadingDailyWords by remember { mutableStateOf(true) }

    /** Compose state */
    val coroutineScope = rememberCoroutineScope()
    val homeScaffoldState: ScaffoldState = rememberScaffoldState()
    val pagerState = rememberPagerState(pageCount = dailyWordCount ?: 3)
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberBottomSheetState(
            initialValue = BottomSheetValue.Collapsed,
        )
    )

    /** Helper variables */
    val context = LocalContext.current


    /**   ---OBSERVERS---   */

    /** Initialize the user and get the current
     * picked learning languages */
    LaunchedEffect(true) {
        sharedViewModel.checkAuthStatus(
            getAccessTokenFromDataStore(context),
            getRefreshTokenFromDataStore(context)
        )

        user = getUserFromDataStore(context)

        if (pickedLanguages == null) {
            sharedViewModel.getPickedLanguages(getAccessTokenFromDataStore(context))
        }

    }

    LaunchedEffect(authStatusUIState) {
        when (authStatusUIState) {
            is UIState.Initial -> {

            }
            is UIState.Error -> {
                isLoadingUser = false
            }
            is UIState.Loading -> {
                isLoadingUser = true
            }
            is UIState.Loaded -> {
                isLoadingUser = false
                if (user == null && authStatusUIState.value?.user != null) {
                    // When user login
                    sharedViewModel.getPickedLanguages(getAccessTokenFromDataStore(context))
                }

                authStatusUIState.value?.user?.let {
                    dailyWordCount = it.dailyWordCount
                    DataStoreUtils.saveTokenToDataStore(context, authStatusUIState.value!!.token)
                    DataStoreUtils.saveUserToDataStore(context, it)
                }



                Log.d("TAG", "test this2 ${authStatusUIState.value}")

            }
        }
    }


    /** Whenever picked languages change,
     * if picked languages is not null, show all the languages */
    LaunchedEffect(pickedLanguagesUIState) {

        when (pickedLanguagesUIState) {
            is UIState.Initial -> {
            }
            is UIState.Error -> {
                pickedLanguages = pickedLanguagesUIState.value
                showAddLanguageMenu = false
            }
            is UIState.Loading -> {
                Log.d("TAG", "pickedLanguages loading")
            }

            is UIState.Loaded -> {
                Log.d(TAG, "picked languages result ${pickedLanguagesUIState.value}")
                if (pickedLanguagesUIState.value != null) {
                    pickedLanguages = pickedLanguagesUIState.value
                    showAddLanguageMenu = pickedLanguages?.isEmpty()
                }
            }
        }
    }

    /** Whenever the current picked languages change,
     * if it is not null, then get the daily words of that language */
    LaunchedEffect(currentPickedLanguage) {
        if (currentPickedLanguage != null) {
            isLoadingDailyWords = false
            homeViewModel.getDailyWords(user?.dailyWordCount ?: 3, currentPickedLanguage!!.id)
            sharedViewModel.getSavedWords(
                getAccessTokenFromDataStore(context),
                currentPickedLanguage
            )
        }
    }

    /** Whenever the uiState changes, Update the UI so that
     * the user can tell the app is loading, error or idle */
    LaunchedEffect(
        dailyEnWordsUIState,
        dailyEsWordsUIState,
        dailyDeWordsUIState,
        dailyFrWordsUIState
    ) {
        when (dailyEnWordsUIState) {
            is UIState.Initial -> {
                //Do nothing
            }
            is UIState.Error -> {
                Log.d(TAG, "error ${dailyEnWordsUIState.errorMsg}")
                if (currentPickedLanguage?.id == "en_US") {
                    isLoadingDailyWords = false
                }
            }
            is UIState.Loading -> {
                if (currentPickedLanguage?.id == "en_US") {
                    isLoadingDailyWords = true
                }
            }
            is UIState.Loaded -> {
                if (currentPickedLanguage?.id == "en_US") {
                    isLoadingDailyWords = false
                }
                dailyEnWordsUIState.value?.let {
                    dailyEnWords = it as ArrayList<Word>
                }
            }
        }

        when (dailyEsWordsUIState) {
            is UIState.Initial -> {
                if (currentPickedLanguage?.id == "es") {
                    isLoadingDailyWords = true
                }
            }
            is UIState.Error -> {
                if (currentPickedLanguage?.id == "es") {
                    isLoadingDailyWords = false
                }
            }
            is UIState.Loading -> {
                if (currentPickedLanguage?.id == "es") {
                    isLoadingDailyWords = true
                }
            }
            is UIState.Loaded -> {
                if (currentPickedLanguage?.id == "es") {
                    isLoadingDailyWords = false
                }
                dailyEsWordsUIState.value?.let {
                    dailyEsWords = it
                }
            }

        }

        when (dailyFrWordsUIState) {
            is UIState.Initial -> {
                //Do nothing
            }
            is UIState.Error -> {
                if (currentPickedLanguage?.id == "fr") {
                    isLoadingDailyWords = false
                }
            }
            is UIState.Loading -> {
                if (currentPickedLanguage?.id == "fr") {
                    isLoadingDailyWords = true
                }
            }
            is UIState.Loaded -> {
                if (currentPickedLanguage?.id == "fr") {
                    isLoadingDailyWords = false
                }
                dailyFrWordsUIState.value?.let {
                    dailyFrWords = it as ArrayList<Word>
                }
            }

        }

        when (dailyDeWordsUIState) {
            is UIState.Initial -> {
                //Do nothing
            }
            is UIState.Error -> {
                if (currentPickedLanguage?.id == "de") {
                    isLoadingDailyWords = false
                }
            }
            is UIState.Loading -> {
                if (currentPickedLanguage?.id == "de") {
                    isLoadingDailyWords = true
                }
            }
            is UIState.Loaded -> {

                if (currentPickedLanguage?.id == "de") {
                    isLoadingDailyWords = false
                }
                dailyDeWordsUIState.value?.let {
                    dailyDeWords = it as ArrayList<Word>
                }
            }

        }
    }


    LaunchedEffect(
        savedEnWordsUIState,
        savedDeWordsUIState,
        savedFrWordsUIState,
        savedEsWordsUIState
    ) {
        when (savedEnWordsUIState) {
            is UIState.Error -> {
                Log.d(TAG, "saved words error: ${savedEnWordsUIState.errorMsg}")
            }
            is UIState.Initial -> {
                savedEnWords = savedEnWordsUIState.value
            }
            is UIState.Loading -> {
                Log.d(TAG, "saved words loading: ${savedEnWordsUIState.value}")

            }
            is UIState.Loaded -> {
                Log.d(TAG, "saved words loaded: ${savedEnWordsUIState.value}")
                savedEnWords = savedEnWordsUIState.value
            }
        }

        when (savedEsWordsUIState) {
            is UIState.Error -> {
                Log.d(TAG, "saved words error: ${savedEsWordsUIState.errorMsg}")
            }
            is UIState.Initial -> {
                savedEsWords = savedEsWordsUIState.value
            }
            is UIState.Loading -> {
                Log.d(TAG, "saved words loading: ${savedEsWordsUIState.value}")

            }
            is UIState.Loaded -> {
                Log.d(TAG, "saved words loaded: ${savedEsWordsUIState.value}")
                savedEsWords = savedEsWordsUIState.value
            }
        }

        when (savedFrWordsUIState) {
            is UIState.Error -> {
                Log.d(TAG, "saved words error: ${savedFrWordsUIState.errorMsg}")
            }
            is UIState.Initial -> {
                savedFrWords = savedFrWordsUIState.value
            }
            is UIState.Loading -> {
                Log.d(TAG, "saved words loading: ${savedFrWordsUIState.value}")

            }
            is UIState.Loaded -> {
                Log.d(TAG, "saved words loaded: ${savedFrWordsUIState.value}")
                savedFrWords = savedFrWordsUIState.value
            }
        }

        when (savedDeWordsUIState) {
            is UIState.Error -> {
                Log.d(TAG, "saved words error: ${savedDeWordsUIState.errorMsg}")
            }
            is UIState.Initial -> {
                savedDeWords = savedDeWordsUIState.value
            }
            is UIState.Loading -> {
                Log.d(TAG, "saved words loading: ${savedDeWordsUIState.value}")

            }
            is UIState.Loaded -> {
                Log.d(TAG, "saved words loaded: ${savedDeWordsUIState.value}")
                savedDeWords = savedDeWordsUIState.value
            }
        }
    }


    /**   ---HELPER FUNCTIONS---   */

    fun dailyWords(): List<Word>? {
        if (currentPickedLanguage == null) return listOf()
        return when (currentPickedLanguage!!.id) {
            SupportedLanguage.ENGLISH.id -> {
                dailyEnWords
            }
            SupportedLanguage.SPANISH.id -> {
                dailyEsWords
            }
            SupportedLanguage.FRENCH.id -> {
                dailyFrWords
            }
            SupportedLanguage.GERMANY.id -> {
                dailyDeWords
            }
            else -> listOf()
        }
    }

    fun savedWords(): List<Word>? {
        if (currentPickedLanguage == null) return listOf()
        return when (currentPickedLanguage!!.id) {
            SupportedLanguage.ENGLISH.id -> {
                savedEnWords
            }
            SupportedLanguage.SPANISH.id -> {
                savedEsWords
            }
            SupportedLanguage.FRENCH.id -> {
                savedFrWords
            }
            SupportedLanguage.GERMANY.id -> {
                savedDeWords
            }
            else -> listOf()
        }
    }


    /**   ---COMPOSE UI---   */

    Surface(
        color = AlmostBlack,
    ) {
        Scaffold(
            Modifier
                .fillMaxHeight()
                .fillMaxWidth(),
            scaffoldState = homeScaffoldState

        ) {
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
                        if (showAddLanguageMenu == true) {
                            ChooseLanguageSection(pickedLanguages = pickedLanguages!!) {
                                coroutineScope.launch {
                                    showAddLanguageMenu = false
                                    sharedViewModel.savePickedLanguages(
                                        it,
                                        getAccessTokenFromDataStore(context)
                                    )
                                }
                            }
                        } else if (showAddLanguageMenu == false) {
                            LazyColumn(Modifier.padding(bottom = 50.dp)) {

                                item {
                                    Row(
                                        modifier = Modifier.padding(
                                            start = 30.dp,
                                            end = 30.dp,
                                            top = 30.dp
                                        ),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        pickedLanguages?.forEach { language ->
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
                                                        sharedViewModel.changeCurrentPickedLanguage(
                                                            language
                                                        )
                                                    }
                                            )
                                        }
                                        Spacer(modifier = Modifier.padding(horizontal = 5.dp))
                                        RoundButton(
                                            backgroundColor = Grey,
                                            size = 38.dp,
                                            icon = R.drawable.add_32_black
                                        ) {
                                            showAddLanguageMenu = true
                                        }
                                    }
                                }
                                item {
                                    DailyWordSection(
                                        isLoading = isLoadingDailyWords,
                                        pagerState = pagerState,
                                        navController = navController,
                                        words = dailyWords() ?: listOf(),
                                        onDailyWordClick = {
                                            navController.navigate("${PawnScreens.WordDetail.route}/${it}/${currentPickedLanguage?.id}")
                                        },
                                        onToggleSaveWord = {
                                            coroutineScope.launch {
                                                sharedViewModel.toggleSavedWord(
                                                    it,
                                                    getAccessTokenFromDataStore(context),
                                                    currentPickedLanguage?.id
                                                )
                                            }
                                        },
                                        checkIsSaved = { wordValue ->
                                            sharedViewModel.checkIsSaved(
                                                wordValue,
                                                currentPickedLanguage?.id
                                            )
                                        },
                                        onRemoveWord = {}
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

                                savedWords()?.let { savedWords ->
                                    items(savedWords.size) { index ->
                                        val word = savedWords[index]
                                        Box(Modifier.padding(horizontal = 30.dp)) {
                                            SavedWordItem(
                                                word = word.value,
                                                pronunciationSymbol = word.pronunciationSymbol,
                                                pronunciationAudio = word.pronunciationAudio,
                                                index = index,
                                                onClick = {
                                                    navController.navigate("${PawnScreens.WordDetail.route}/${savedWords[index].value}/${currentPickedLanguage?.id}")
                                                }
                                            )
                                        }

                                    }
                                }
                            }

                        }
                    }
                },
                scaffoldState = bottomSheetScaffoldState,
                topBar = {
                    HomeAppBar(navController, user, onLogout = {
                        sharedViewModel.logout(it)
                        sharedViewModel.getPickedLanguages(null)
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


