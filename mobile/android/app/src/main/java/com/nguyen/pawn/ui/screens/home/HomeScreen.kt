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
import com.nguyen.pawn.model.Language
import com.nguyen.pawn.model.User
import com.nguyen.pawn.model.Word
import com.nguyen.pawn.ui.SharedViewModel
import com.nguyen.pawn.ui.components.DailyWordSection
import com.nguyen.pawn.ui.components.HomeAppBar
import com.nguyen.pawn.ui.components.RoundButton
import com.nguyen.pawn.ui.components.SavedWordItem
import com.nguyen.pawn.ui.components.home.ChooseLanguageSection
import com.nguyen.pawn.ui.screens.home.HomeViewModel
import com.nguyen.pawn.ui.theme.*
import com.nguyen.pawn.util.DataStoreUtils.getAccessTokenFromDataStore
import com.nguyen.pawn.util.DataStoreUtils.getRefreshTokenFromDataStore
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

    val userUIState: UIState<User> by sharedViewModel.userUIState
    var user by remember { mutableStateOf(userUIState.value) }

    val currentPickedLanguage: Language? by sharedViewModel.currentPickedLanguage

    val savedEnWordsUIState: UIState<List<Word>> by sharedViewModel.savedEnWordsUIState
    var savedEnWords by remember { mutableStateOf(savedEnWordsUIState.value) }


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
    LaunchedEffect(null) {
        sharedViewModel.getUser(
            getAccessTokenFromDataStore(context),
            getRefreshTokenFromDataStore(context)
        )

        if (pickedLanguages == null) {
            Log.d(TAG, "GET picked languages")
            sharedViewModel.getPickedLanguages(getAccessTokenFromDataStore(context))
        }

    }

    LaunchedEffect(userUIState) {
        when (userUIState) {
            is UIState.Initial -> {
                isLoadingUser = true
            }
            is UIState.Error -> {
                isLoadingUser = false
            }
            is UIState.Loading -> {
                isLoadingUser = true
            }
            is UIState.Loaded -> {
                isLoadingUser = false
                if(user == null && userUIState.value != null){
                    // When user login
                    sharedViewModel.getPickedLanguages(getAccessTokenFromDataStore(context))
                }
                user = userUIState.value
                userUIState.value?.let { user ->
                    dailyWordCount = user.dailyWordCount
                }
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

            }
            is UIState.Loading -> {
                Log.d("TAG", "pickedLanguages loading")
            }

            is UIState.Loaded -> {
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
            sharedViewModel.getSavedWords(getAccessTokenFromDataStore(context), currentPickedLanguage)
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


    LaunchedEffect(savedEnWordsUIState){
        when(savedEnWordsUIState){
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
    }


    /**   ---HELPER FUNCTIONS---   */

    fun dailyWords(): List<Word>? {
        if (currentPickedLanguage == null) return arrayListOf()
        return when (currentPickedLanguage?.id) {
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
            else -> arrayListOf()
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
                                        onToggleSaveWord = {
                                            coroutineScope.launch {
                                                sharedViewModel.toggleSavedWord(it, getAccessTokenFromDataStore(context), currentPickedLanguage)
                                            }
                                        },
                                        checkIsSaved = {wordValue ->
                                            sharedViewModel.checkIsSaved(wordValue, currentPickedLanguage)
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

                                if(savedEnWords != null) {
                                    items(savedEnWords!!.size) { index ->
                                        SavedWordItem(
                                            word = savedEnWords!![index].value,
                                            pronunciation = savedEnWords!![index].pronunciations[0].symbol,
                                            index = index,
                                            onClick = {
                                                navController.navigate("word")
                                            }
                                        )
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


