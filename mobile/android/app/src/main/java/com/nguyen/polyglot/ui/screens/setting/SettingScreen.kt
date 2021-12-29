package com.nguyen.polyglot.ui.screens.setting

import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterEnd
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Alignment.Companion.Top
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.nguyen.polyglot.R
import com.nguyen.polyglot.ui.SharedViewModel
import com.nguyen.polyglot.ui.theme.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.platform.LocalContext
import com.nguyen.polyglot.model.User
import com.nguyen.polyglot.ui.components.account.AvatarBottomSheetContent
import com.nguyen.polyglot.ui.components.auth.LanguageBottomSheetContent
import com.nguyen.polyglot.util.Constants
import com.nguyen.polyglot.util.DataStoreUtils
import com.nguyen.polyglot.util.UIState
import com.nguyen.polyglot.util.UtilFunctions.fromLanguageId
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SettingScreen(navController: NavController, sharedViewModel: SharedViewModel) {

    var notificationEnabled by remember { mutableStateOf(true) }
    val authStatusUIState by sharedViewModel.authStatusUIState
    var user by remember { mutableStateOf(authStatusUIState.value?.user) }
    val context = LocalContext.current
    var dailyWordCount by remember {
        mutableStateOf(
            sharedViewModel.authStatusUIState.value.value?.user?.dailyWordCount?.toFloat() ?: 3f
        )
    }

    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState()
    val coroutineScope = rememberCoroutineScope()


    fun toggleBottomSheet() {
        coroutineScope.launch {
            if (bottomSheetScaffoldState.bottomSheetState.isCollapsed) {
                bottomSheetScaffoldState.bottomSheetState.expand()
            } else {
                bottomSheetScaffoldState.bottomSheetState.collapse()
            }
        }
    }

    suspend fun updateAppLanguage(newLanguage: String) {
        if (authStatusUIState.value == null) return
        if (user == null) return

        sharedViewModel.updateUser(
            accessToken = DataStoreUtils.getAccessTokenFromDataStore(context),
            currentAuthStatus = authStatusUIState.value!!,
            username = user!!.username,
            email = user!!.email,
            avatar = user!!.avatar,
            dailyWordCount = user!!.dailyWordCount,
            notificationEnabled = user!!.notificationEnabled,
            nativeLanguageId = user!!.nativeLanguageId,
            appLanguageId = newLanguage
        )
    }

    LaunchedEffect(authStatusUIState) {
        when (authStatusUIState) {
            is UIState.Initial -> {

            }

            is UIState.Error -> {

            }
            is UIState.Loading -> {

            }
            is UIState.Loaded -> {
                Log.d("AccountScreen", "authStatus Loaded ${authStatusUIState.value}")

                authStatusUIState.value?.user?.let {
                    user = it

                    // Store the state to DataStore
                    DataStoreUtils.saveTokenToDataStore(context, authStatusUIState.value!!.token)
                    DataStoreUtils.saveUserToDataStore(context, it)
                }
            }
        }
    }

    BottomSheetScaffold(
        backgroundColor = Color.White,
        sheetPeekHeight = 0.dp,
        sheetContent = {
            LanguageBottomSheetContent(
                languages = Constants.allLanguages,
                onLanguageClick = { language ->
                    coroutineScope.launch {
                        updateAppLanguage(language)
                    }
                    toggleBottomSheet()
                }
            )
        },
        scaffoldState = bottomSheetScaffoldState,
        sheetShape = RoundedCornerShape(topStart = 60.dp, topEnd = 60.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier
                        .size(30.dp)
                        .align(
                            Alignment.CenterStart
                        )
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.back_32_black),
                        contentDescription = "back icon"
                    )
                }
                Text(
                    text = "Settings",
                    style = Typography.h4,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            Spacer(modifier = Modifier.padding(10.dp))
            Card(
                shape = RoundedCornerShape(15.dp),
                backgroundColor = DarkBlue,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)

            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Text(text = "Subscription", style = Typography.h6, color = Color.White)
                    Spacer(modifier = Modifier.padding(10.dp))
                    Card(
                        shape = RoundedCornerShape(15.dp),
                        backgroundColor = LightGrey,
                        modifier = Modifier
                            .clip(RoundedCornerShape(15.dp))
                            .fillMaxWidth()
                            .clickable { }
                    ) {
                        Column(Modifier.padding(10.dp)) {
                            Box(Modifier.fillMaxWidth()) {
                                Text(
                                    text = "Free",
                                    style = Typography.h6,
                                    fontSize = 18.sp,
                                    color = Color.Black
                                )
                                Image(
                                    painter = painterResource(id = R.drawable.check_icon_blue_32),
                                    contentDescription = "Check icon",
                                    modifier = Modifier
                                        .size(24.dp)
                                        .align(
                                            CenterEnd
                                        )
                                )
                            }

                            Spacer(modifier = Modifier.padding(5.dp))
                            Text(
                                text = "Only essential features",
                                style = Typography.body2,
                                color = Color.Black
                            )
                            Spacer(modifier = Modifier.padding(3.dp))

                        }
                    }
                    Spacer(modifier = Modifier.padding(10.dp))
                    Card(
                        shape = RoundedCornerShape(15.dp),
                        backgroundColor = ReallyRed,
                        modifier = Modifier
                            .clip(RoundedCornerShape(15.dp))
                            .fillMaxWidth()
                            .clickable { }
                    ) {
                        Column(Modifier.padding(10.dp)) {
                            Text(text = "Premium", style = Typography.h5)
                            Text(
                                text = "$9.99 • one time",
                                style = Typography.body2,
                            )

                            Spacer(modifier = Modifier.padding(5.dp))
                            Row {
                                Text(
                                    text = "•",
                                    style = Typography.h3,
                                )
                                Text(
                                    text = "No Ads",
                                    style = Typography.body1,
                                    modifier = Modifier
                                        .align(CenterVertically)
                                        .padding(start = 5.dp)
                                )
                            }

                            Spacer(modifier = Modifier.padding(3.dp))

                            Row {
                                Text(
                                    text = "•",
                                    style = Typography.h3,
                                )
                                Text(
                                    text = "Translated video caption",
                                    style = Typography.body1,
                                    modifier = Modifier
                                        .align(CenterVertically)
                                        .padding(start = 5.dp)
                                )
                            }

                            Spacer(modifier = Modifier.padding(3.dp))

                            Row {
                                Text(
                                    text = "•",
                                    style = Typography.h3,
                                )
                                Text(
                                    text = "Topic filter for daily words, news and videos feed",
                                    style = Typography.body1,
                                    modifier = Modifier
                                        .align(CenterVertically)
                                        .padding(start = 5.dp)
                                )
                            }

                            Spacer(modifier = Modifier.padding(3.dp))

                            Row {
                                Text(
                                    text = "•",
                                    style = Typography.h3,
                                )
                                Text(
                                    text = "Support us :)",
                                    style = Typography.body1,
                                    modifier = Modifier
                                        .align(CenterVertically)
                                        .padding(start = 5.dp)
                                )
                            }
                            Spacer(modifier = Modifier.padding(3.dp))
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.padding(5.dp))

            Card(
                shape = RoundedCornerShape(15.dp),
                backgroundColor = LightGrey,
                elevation = 4.dp,
                onClick = {
                    toggleBottomSheet()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)

            ) {
                Column(Modifier.padding(10.dp)) {
                    Column(Modifier.padding(10.dp)) {
                        Text(text = "App language", style = Typography.h6)
                        Spacer(modifier = Modifier.padding(5.dp))
                        Text(text = fromLanguageId(user?.appLanguageId ?: "en") ?: "English", style = Typography.body1, color = Color.Black)
                    }
                }
            }

            Spacer(modifier = Modifier.padding(5.dp))

            Card(
                shape = RoundedCornerShape(15.dp),
                backgroundColor = LightGrey,
                elevation = 4.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                Column(Modifier.padding(10.dp)) {
                    Box(Modifier.fillMaxWidth()) {
                        Text(text = "Notifications", style = Typography.h6)
                        Switch(
                            checked = notificationEnabled,
                            onCheckedChange = { notificationEnabled = it },
                            modifier = Modifier.align(CenterEnd)
                        )
                    }
                    Spacer(modifier = Modifier.padding(5.dp))
                    Text(
                        text = "If enabled, you will receive push notifications",
                        style = Typography.body1,
                        color = Color.Black
                    )

                }
            }

            Spacer(modifier = Modifier.padding(5.dp))

            Card(
                shape = RoundedCornerShape(15.dp),
                backgroundColor = LightGrey,
                elevation = 4.dp,
                onClick = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)

            ) {
                Column(Modifier.padding(10.dp)) {
                    Column(Modifier.padding(5.dp)) {
                        Text(text = "Daily word", style = Typography.h6)
                        Spacer(modifier = Modifier.padding(5.dp))
                        Text(text = "Daily word count", style = Typography.h6, fontSize = 16.sp)
                        Text(
                            text = "This will determine how many daily words you receive",
                            style = Typography.body1,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.padding(5.dp))
                        Slider(
                            value = dailyWordCount,
                            onValueChange = { dailyWordCount = it },
                            valueRange = 0f..5f,
                            steps = 4
                        )
                        Spacer(modifier = Modifier.padding(2.dp))
                        Text(
                            text = "${dailyWordCount.toInt()} words",
                            style = Typography.body1,
                            color = Color.Black,
                            modifier = Modifier.align(CenterHorizontally)
                        )
                        Spacer(modifier = Modifier.padding(5.dp))
                        Text(
                            text = "Daily word topics",
                            style = Typography.body1,
                            color = Color.Black,
                            modifier = Modifier.align(CenterHorizontally)
                        )
                    }
                }
            }

        }
    }
}