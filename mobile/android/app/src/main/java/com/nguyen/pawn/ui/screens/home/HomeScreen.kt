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
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import com.nguyen.pawn.model.Language
import com.nguyen.pawn.model.User
import com.nguyen.pawn.ui.SharedViewModel
import com.nguyen.pawn.ui.components.home.ChooseLanguagesHeader
import com.nguyen.pawn.ui.components.home.LanguageItem
import com.nguyen.pawn.ui.theme.*
import com.nguyen.pawn.ui.screens.home.HomeViewModel

import com.nguyen.pawn.util.Constants.supportedLanguages
import com.nguyen.pawn.util.DataStoreUtils.getAccessTokenFromDataStore
import com.nguyen.pawn.util.DataStoreUtils.getRefreshTokenFromDataStore
import com.nguyen.pawn.util.LoadingType
import com.nguyen.pawn.util.SupportedLanguage
import com.nguyen.pawn.util.UIState
import com.nguyen.pawn.util.UtilFunctions.convertHeightToDp
import com.nguyen.pawn.util.UtilFunctions.generateFlagForLanguage
import io.ktor.util.date.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
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

    /** States from viewModel */
    val dailyEnWordsUIState: UIState<List<Word>> by homeViewModel.dailyEnWordsUIState
    var dailyEnWords by remember { mutableStateOf<ArrayList<Word>>(arrayListOf()) }

    val dailyEsWordsUIState: UIState<List<Word>> by homeViewModel.dailyEsWordsUIState
    var dailyEsWords by remember { mutableStateOf<ArrayList<Word>>(arrayListOf()) }

    val dailyFrWordsUIState: UIState<List<Word>> by homeViewModel.dailyFrWordsUIState
    var dailyFrWords by remember { mutableStateOf<ArrayList<Word>>(arrayListOf()) }

    val dailyDeWordsUIState: UIState<List<Word>> by homeViewModel.dailyDeWordsUIState
    var dailyDeWords by remember { mutableStateOf<ArrayList<Word>>(arrayListOf()) }


    val savedWords: List<Word> by sharedViewModel.savedWords
    val pickedLanguages: List<Language>? by sharedViewModel.pickedLanguages
    val displayPickedLanguages: ArrayList<Language> by sharedViewModel.displayPickedLanguages
    val currentPickedLanguage: Language? by sharedViewModel.currentPickedLanguage
    val userUIState: UIState<User> by sharedViewModel.userUIState



    /** Local ui states */
    var showAddLanguageMenu by remember { mutableStateOf(pickedLanguages?.isEmpty()) }
    var isLoadingPickedLanguage by remember { mutableStateOf(true) }
    var isLoadingUser by remember { mutableStateOf(true) }
    var isLoadingDailyWords by remember { mutableStateOf(true) }
    var errorMsg by remember { mutableStateOf("") }
    var dailyWordCount by remember { mutableStateOf(3) }
    var user by remember { mutableStateOf<User?>(null) }

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
        Log.d(TAG, "Trigger 1")

        sharedViewModel.getUser(
            getAccessTokenFromDataStore(context),
            getRefreshTokenFromDataStore(context)
        )

        sharedViewModel.getPickedLanguages(getAccessTokenFromDataStore(context))
    }

    LaunchedEffect(userUIState) {
        when(userUIState){
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
                user = userUIState.loadedValue
                userUIState.loadedValue?.let{user ->
                    dailyWordCount = user.dailyWordCount
                }
            }
        }
    }


    /** Whenever picked languages change,
     * if picked languages is not null, show all the languages */
    LaunchedEffect(pickedLanguages) {
        Log.d(TAG, "Trigger 2")
        if (pickedLanguages != null) {
            showAddLanguageMenu = pickedLanguages!!.isEmpty()
        }
    }

    /** Whenever the current picked languages change,
     * if it is not null, then get the daily words of that language */
    LaunchedEffect(currentPickedLanguage) {
        Log.d(TAG, "Trigger 3 ${currentPickedLanguage?.value}")
        if (currentPickedLanguage != null) {
            homeViewModel.getDailyWords(user?.dailyWordCount ?: 3, currentPickedLanguage!!.id)
        }
    }

    /** Whenever the uiState changes, Update the UI so that
     * the user can tell the app is loading, error or idle */
    LaunchedEffect(dailyEnWordsUIState, dailyEsWordsUIState, dailyDeWordsUIState, dailyFrWordsUIState){
        when(dailyEnWordsUIState){
            is UIState.Initial -> {
                //Do nothing
            }
            is UIState.Error -> {
                if(currentPickedLanguage?.id == "en_US"){
                    isLoadingDailyWords = false
                }
            }
            is UIState.Loading -> {
                if(currentPickedLanguage?.id == "en_US"){
                    isLoadingDailyWords = true
                }
            }
            is UIState.Loaded -> {
                Log.d(TAG, "isLoadingDailyWords: $isLoadingDailyWords ${currentPickedLanguage?.id}")
                if(currentPickedLanguage?.id == "en_US"){
                    isLoadingDailyWords = false
                }
                dailyEnWordsUIState.loadedValue?.let{
                    dailyEnWords = it as ArrayList<Word>
                }
            }
        }

        when(dailyEsWordsUIState){
            is UIState.Initial -> {
                //Do nothing
            }
            is UIState.Error -> {
                if(currentPickedLanguage?.id == "es"){
                    isLoadingDailyWords = false
                }
            }
            is UIState.Loading -> {
                Log.d(TAG, "isLoadingDailyWords: $isLoadingDailyWords ${currentPickedLanguage?.id}")
                if(currentPickedLanguage?.id == "es"){
                    isLoadingDailyWords = true
                }
            }
            is UIState.Loaded -> {
                if(currentPickedLanguage?.id == "es"){
                    isLoadingDailyWords = false
                }
                dailyEsWordsUIState.loadedValue?.let{
                    dailyEsWords = it as ArrayList<Word>
                }
            }

        }

        when(dailyFrWordsUIState){
            is UIState.Initial -> {
                //Do nothing
            }
            is UIState.Error -> {
                if(currentPickedLanguage?.id == "fr"){
                    isLoadingDailyWords = false
                }
            }
            is UIState.Loading -> {
                Log.d(TAG, "isLoadingDailyWords: $isLoadingDailyWords ${currentPickedLanguage?.id}")
                if(currentPickedLanguage?.id == "fr"){
                    isLoadingDailyWords = true
                }
            }
            is UIState.Loaded -> {
                if(currentPickedLanguage?.id == "fr"){
                    isLoadingDailyWords = false
                }
                dailyFrWordsUIState.loadedValue?.let{
                    dailyFrWords = it as ArrayList<Word>
                }
            }

        }

        when(dailyDeWordsUIState){
            is UIState.Initial -> {
                //Do nothing
            }
            is UIState.Error -> {
                if(currentPickedLanguage?.id == "de"){
                    isLoadingDailyWords = false
                }
            }
            is UIState.Loading -> {
                Log.d(TAG, "isLoadingDailyWords: $isLoadingDailyWords ${currentPickedLanguage?.id}")
                if(currentPickedLanguage?.id == "de"){
                    isLoadingDailyWords = true
                }
            }
            is UIState.Loaded -> {

                if(currentPickedLanguage?.id == "de"){
                    isLoadingDailyWords = false
                }
                dailyDeWordsUIState.loadedValue?.let{
                    dailyDeWords = it as ArrayList<Word>
                }
            }

        }
    }

    /**   ---HELPER FUNCTIONS---   */

    fun dailyWords(): ArrayList<Word>? {
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
                        if (showAddLanguageMenu != null) {
                            LazyColumn(Modifier.padding(bottom = 50.dp)) {
                                if (showAddLanguageMenu!!) {
                                    item {
                                        ChooseLanguagesHeader(pickedLanguages = displayPickedLanguages) {
                                            coroutineScope.launch {
                                                showAddLanguageMenu = false
                                                sharedViewModel.savePickedLanguages(
                                                    displayPickedLanguages,
                                                    getAccessTokenFromDataStore(context)
                                                )
                                            }

                                        }
                                    }

                                    items(supportedLanguages.size) { index ->
                                        LanguageItem(
                                            language = supportedLanguages[index],
                                            isPicked = displayPickedLanguages!!.filter {
                                                it.id == supportedLanguages[index].id
                                            }.size == 1
                                        ) { language ->
                                            sharedViewModel.togglePickedLanguage(language)
                                        }
                                    }
                                } else {
                                    item {
                                        Row(
                                            modifier = Modifier.padding(
                                                start = 30.dp,
                                                end = 30.dp,
                                                top = 30.dp
                                            ),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            displayPickedLanguages.forEach { language ->
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
                                            viewModel = sharedViewModel,
                                            pagerState = pagerState,
                                            navController = navController,
                                            words = dailyWords() ?: arrayListOf(),
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
                                            pronunciation = savedWords[index].pronunciations[0].symbol,
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


