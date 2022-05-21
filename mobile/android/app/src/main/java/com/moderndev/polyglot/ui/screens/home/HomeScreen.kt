package com.moderndev.polyglot.ui.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.moderndev.polyglot.R
import com.moderndev.polyglot.model.AuthStatus
import com.moderndev.polyglot.model.Language
import com.moderndev.polyglot.model.Word
import com.moderndev.polyglot.ui.SharedViewModel
import com.moderndev.polyglot.ui.components.DailyWordSection
import com.moderndev.polyglot.ui.components.HomeAppBar
import com.moderndev.polyglot.ui.components.RoundButton
import com.moderndev.polyglot.ui.components.SavedWordItem
import com.moderndev.polyglot.ui.components.home.ChooseLanguageSection
import com.moderndev.polyglot.ui.navigation.PolyglotScreens
import com.moderndev.polyglot.ui.screens.home.HomeViewModel
import com.moderndev.polyglot.ui.theme.*
import com.moderndev.polyglot.util.DataStoreUtils
import com.moderndev.polyglot.util.DataStoreUtils.getAccessTokenFromDataStore
import com.moderndev.polyglot.util.DataStoreUtils.getRefreshTokenFromDataStore
import com.moderndev.polyglot.util.SupportedLanguage
import com.moderndev.polyglot.util.UIState
import com.moderndev.polyglot.util.UtilFunctions.convertHeightToDp
import com.moderndev.polyglot.util.UtilFunctions.generateFlagForLanguage
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

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

    /** Main states */

    val authStatusUIState: UIState<AuthStatus> by sharedViewModel.authStatusUIState
    var user by remember { mutableStateOf(authStatusUIState.value?.user) }

    // Daily word count from user settings
    var dailyWordCount by remember { mutableStateOf(if (user == null) 3 else user?.dailyWordCount) }
    // User's picked learning languages
    val pickedLanguagesUIState: UIState<List<Language>> by sharedViewModel.pickedLanguagesUIState
    var pickedLanguages by remember { mutableStateOf(pickedLanguagesUIState.value) }

    // Boolean flag to control when to open language menu
    val showAddLanguageMenu: Boolean? by homeViewModel.showAddLanguagesMenu
    // Keep the state of the current chosen picked learning languages
    val currentPickedLanguage: Language? by sharedViewModel.currentPickedLanguage

    // Daily words
    val dailyEnWordsUIState: UIState<List<Word>> by homeViewModel.dailyEnWordsUIState
    var dailyEnWords by remember { mutableStateOf(dailyEnWordsUIState.value) }
    val dailyEsWordsUIState: UIState<List<Word>> by homeViewModel.dailyEsWordsUIState
    var dailyEsWords by remember { mutableStateOf(dailyEsWordsUIState.value) }
    val dailyFrWordsUIState: UIState<List<Word>> by homeViewModel.dailyFrWordsUIState
    var dailyFrWords by remember { mutableStateOf(dailyFrWordsUIState.value) }
    val dailyDeWordsUIState: UIState<List<Word>> by homeViewModel.dailyDeWordsUIState
    var dailyDeWords by remember { mutableStateOf(dailyDeWordsUIState.value) }

    // Saved words
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

    /** Loading states */
    var isLoadingPickedLanguage by remember { mutableStateOf(true) }
    var isLoadingUser by remember { mutableStateOf(true) }
    var isLoadingDailyWords by remember { mutableStateOf(true) }

    /** Compose state */
    val coroutineScope = rememberCoroutineScope()
    val homeScaffoldState: ScaffoldState = rememberScaffoldState()
//    val pagerState = rememberPagerState(pageCount = dailyWordCount ?: 3)
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberBottomSheetState(
            initialValue = BottomSheetValue.Collapsed,
        )
    )

    /** Helper variables */
    val context = LocalContext.current
    val configuration = LocalConfiguration.current


    /**   ---OBSERVERS---   */

    /** Initialize the user and get the current
     * picked learning languages */
    LaunchedEffect(true) {

        if (authStatusUIState !is UIState.Loaded) {
            sharedViewModel.checkAuthStatus(
                getAccessTokenFromDataStore(context),
                getRefreshTokenFromDataStore(context)
            )
        }

    }

    LaunchedEffect(authStatusUIState) {
        when (authStatusUIState) {
            is UIState.Initial -> {
                Log.d(TAG, "authStatusUIState Initial")
            }
            is UIState.Error -> {
                Log.d(TAG, "authStatusUIState Error ${authStatusUIState.errorMsg}")
                isLoadingUser = false
            }
            is UIState.Loading -> {
                Log.d(TAG, "authStatusUIState Loading")
                isLoadingUser = true
                Toast.makeText(context, authStatusUIState.errorMsg, Toast.LENGTH_SHORT).show()

            }
            is UIState.Loaded -> {
                Log.d(TAG, "authStatusUIState Loaded ${authStatusUIState.value}")
                isLoadingUser = false

                if (authStatusUIState.value?.user != null) {
                    //Update daily word count state
                    dailyWordCount = authStatusUIState.value?.user!!.dailyWordCount
                    user = authStatusUIState.value?.user

                    user?.appLanguageId?.let { id ->
                        val locale = Locale(id)
                        configuration.setLocale(locale)
                        val resources = context.resources
                        resources.updateConfiguration(configuration, resources.displayMetrics)
                    }

                } else {
                    user = null
                }

                // Store the state to DataStore
                DataStoreUtils.saveTokenToDataStore(context, authStatusUIState.value!!.token)
                DataStoreUtils.saveUserToDataStore(context, authStatusUIState.value!!.user)

                // Only get the picked learning languages from network when initial load
                if ((pickedLanguagesUIState !is UIState.Loaded)) {
                    Log.d(TAG, "Getting here")
                    sharedViewModel.getPickedLanguages(authStatusUIState.value?.token?.accessToken)
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
                Toast.makeText(context, pickedLanguagesUIState.errorMsg, Toast.LENGTH_SHORT).show()
            }
            is UIState.Loading -> {
            }

            is UIState.Loaded -> {
                pickedLanguagesUIState.value?.let { languages ->
                    // Update UI
                    pickedLanguages = languages
                    homeViewModel.setAddLanguagesMenu(languages.isEmpty())
                }
            }
        }
    }

    /** Whenever the current picked languages change,
     * if it is not null, then get the daily words of that language */
    LaunchedEffect(currentPickedLanguage) {
        if (currentPickedLanguage != null) {
            isLoadingDailyWords = false
            Log.d("get access token", "token ${getAccessTokenFromDataStore(context)}")

            homeViewModel.getDailyWords(
                user?.dailyWordCount ?: 3,
                currentPickedLanguage!!.id,
                user?.dailyWordTopic ?: "Random"
            )
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
                Toast.makeText(context, dailyEnWordsUIState.errorMsg, Toast.LENGTH_SHORT).show()

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
                Toast.makeText(context, dailyEsWordsUIState.errorMsg, Toast.LENGTH_SHORT).show()

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
                Toast.makeText(context, dailyFrWordsUIState.errorMsg, Toast.LENGTH_SHORT).show()

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
                Toast.makeText(context, dailyDeWordsUIState.errorMsg, Toast.LENGTH_SHORT).show()

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
                Toast.makeText(context, savedEnWordsUIState.errorMsg, Toast.LENGTH_SHORT).show()

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
                Toast.makeText(context, savedEnWordsUIState.errorMsg, Toast.LENGTH_SHORT).show()

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
                Toast.makeText(context, savedFrWordsUIState.errorMsg, Toast.LENGTH_SHORT).show()

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
                Toast.makeText(context, savedDeWordsUIState.errorMsg, Toast.LENGTH_SHORT).show()

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
                                    homeViewModel.setAddLanguagesMenu(false)
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
                                            homeViewModel.setAddLanguagesMenu(true)
                                        }
                                    }
                                }
                                item {
                                    DailyWordSection(
                                        isLoading = isLoadingDailyWords,
                                        navController = navController,
                                        words = dailyWords() ?: listOf(),
                                        onDailyWordClick = {
                                            navController.navigate("${PolyglotScreens.WordDetail.route}/${it}/${currentPickedLanguage?.id}")
                                        },
                                        onToggleSaveWord = {
                                            if(user == null){
                                                coroutineScope.launch {
                                                    sharedViewModel.toggleSavedWord(
                                                        it,
                                                        getAccessTokenFromDataStore(context),
                                                        currentPickedLanguage?.id
                                                    )
                                                }
                                            } else {
                                                Toast.makeText(context, "Please log in first!", Toast.LENGTH_SHORT).show()
                                            }
                                        },
                                        checkIsSaved = { wordValue ->
                                            sharedViewModel.checkIsSaved(
                                                wordValue,
                                                currentPickedLanguage?.id
                                            )
                                        },
                                        onRemoveWord = { wordValue ->
                                            homeViewModel.removeDailyWords(
                                                wordValue,
                                                currentPickedLanguage?.id
                                            )
                                        }
                                    )
                                }
                                if(user != null){
                                    stickyHeader {
                                        Column(
                                            Modifier
                                                .background(Color.White)
                                                .padding(start = 30.dp, top = 20.dp)
                                                .fillMaxWidth()
                                        ) {
                                            Text(
                                                text = stringResource(id = R.string.your_saved_words),
                                                style = Typography.h6,
                                                fontSize = 18.sp
                                            )

                                            Row(Modifier.padding(vertical = 5.dp)) {
                                                RoundButton(
                                                    backgroundColor = Grey,
                                                    size = 32.dp,
                                                    icon = R.drawable.review,
                                                    onClick = {
                                                        navController.navigate(PolyglotScreens.WordReviewMenu.route)
                                                    })
                                            }
                                        }
                                    }

                                    savedWords()?.let { savedWords ->
                                        if (savedWords.isEmpty()) {
                                            item {
                                                Text(
                                                    text = stringResource(id = R.string.empty_saved_words_msg),
                                                    style = Typography.subtitle1,
                                                    textAlign = TextAlign.Center,
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .padding(30.dp)
                                                        .align(CenterHorizontally)
                                                )
                                            }
                                        } else {
                                            items(savedWords.size) { index ->
                                                val word = savedWords[index]
                                                Box(Modifier.padding(horizontal = 30.dp)) {
                                                    SavedWordItem(
                                                        word = word.value,
                                                        pronunciationSymbol = word.pronunciationSymbol,
                                                        pronunciationAudio = word.pronunciationAudio,
                                                        index = index,
                                                        onClick = {
                                                            navController.navigate("${PolyglotScreens.WordDetail.route}/${savedWords[index].value}/${currentPickedLanguage?.id}")
                                                        }
                                                    )
                                                }

                                            }
                                        }
                                    }
                                } else {
                                    item {
                                        Text(
                                            text = stringResource(id = R.string.not_log_in_saved_words_msg),
                                            style = Typography.subtitle1,
                                            textAlign = TextAlign.Center,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(30.dp)
                                                .align(CenterHorizontally)
                                        )
                                    }
                                }

                            }

                        }
                    }
                },
                scaffoldState = bottomSheetScaffoldState,
                topBar = {
                    HomeAppBar(navController, user,
                        onAccountClick = {
                            navController.navigate("account")
                        },
                        onSettingClick = {
                            navController.navigate("setting")
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


