package com.nguyen.pawn.ui.screens

import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.nguyen.pawn.R
import com.nguyen.pawn.ui.components.RoundButton
import com.nguyen.pawn.ui.components.auth.Login
import com.nguyen.pawn.util.AuthTab
import com.nguyen.pawn.util.UtilFunctions.convertHeightToDp
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.draw.clip
import com.nguyen.pawn.model.Token
import com.nguyen.pawn.ui.SharedViewModel
import com.nguyen.pawn.ui.components.CustomDialog
import com.nguyen.pawn.ui.components.auth.LanguageBottomSheetContent
import com.nguyen.pawn.ui.components.auth.Register
import com.nguyen.pawn.ui.theme.*
import com.nguyen.pawn.ui.screens.auth.AuthViewModel
import com.nguyen.pawn.util.DataStoreUtils
import com.nguyen.pawn.util.DataStoreUtils.getAccessTokenFromDataStore
import com.nguyen.pawn.util.DataStoreUtils.getRefreshTokenFromDataStore
import com.nguyen.pawn.util.UIState
import kotlinx.coroutines.launch

@ExperimentalMaterialApi
@Composable
fun AuthScreen(authViewModel: AuthViewModel, sharedViewModel: SharedViewModel, navController: NavController) {

    val TAG = "AuthScreen"

    val deviceWidthDp = (convertHeightToDp(
        LocalContext.current.resources.displayMetrics.widthPixels,
        LocalContext.current.resources.displayMetrics
    ))

    var currentTab by remember { mutableStateOf(AuthTab.LOGIN) }

    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberBottomSheetState(
            initialValue = BottomSheetValue.Collapsed,
        )
    )
    val coroutineScope = rememberCoroutineScope()
    var nativeLanguage by remember { mutableStateOf("") }
    val languages = listOf("vie", "eng")
    val context = LocalContext.current
    val token: Token by authViewModel.token
    val uiState: UIState? by authViewModel.uiState.observeAsState()
    var errorMsg by remember { mutableStateOf("") }


    LaunchedEffect(null) {
        authViewModel.initializeToken(getAccessTokenFromDataStore(context), getRefreshTokenFromDataStore(context))
    }

    LaunchedEffect(token) {
        DataStoreUtils.saveAccessTokenToAuthDataStore(context, authViewModel.token.value.accessToken)
        DataStoreUtils.saveRefreshTokenToAuthDataStore(context, authViewModel.token.value.refreshToken)
        if (token.accessToken != null && token.refreshToken != null) {
            navController.navigate("home") {
                popUpTo("home") { inclusive = true }
            }
        }
    }

    LaunchedEffect(uiState) {
        when(uiState){
            is UIState.Idle -> {
                // Hide loading animation and error dialog
                errorMsg = ""
            }
            is UIState.Loading -> {
                // Display loading animation
            }
            is UIState.Error -> {
                // Display error message to user
                errorMsg = (uiState as UIState.Error).msg
            }
        }
    }

    fun toggleBottomSheet() {
        coroutineScope.launch {
            if (bottomSheetScaffoldState.bottomSheetState.isCollapsed) {
                bottomSheetScaffoldState.bottomSheetState.expand()
            } else {
                bottomSheetScaffoldState.bottomSheetState.collapse()
            }
        }
    }

    BottomSheetScaffold(
        backgroundColor = Color.White,
        sheetPeekHeight = 0.dp,
        sheetContent = {
            LanguageBottomSheetContent(
                languages = languages,
                onLanguageClick = { language ->
                    nativeLanguage = language
                    toggleBottomSheet()
                }
            )
        },
        scaffoldState = bottomSheetScaffoldState,
        sheetShape = RoundedCornerShape(topStart = 60.dp, topEnd = 60.dp),
    ) {
        Column(
            Modifier.verticalScroll(rememberScrollState())
        ) {
            Box {
                Card(
                    elevation = 0.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .offset(
                            x = (deviceWidthDp * 0.3f * -1).dp,
                            y = (deviceWidthDp * 0.3 * -1).dp
                        ),
                    backgroundColor = ReallyRed,
                    shape = CircleShape,
                    content = {}
                )
                Column(
                    modifier = Modifier
                        .requiredWidth((deviceWidthDp * 0.6).dp)
                        .padding(10.dp)
                ) {
                    if (currentTab == AuthTab.LOGIN) {
                        Text(
                            text = "Sign in to your account",
                            style = Typography.h2,
                            color = Color.White,
                        )
                        Spacer(modifier = Modifier.padding(10.dp))
                        Text(
                            text = "Welcome back!",
                            style = Typography.h6,
                            color = Color.White,
                        )
                    } else {
                        Text(
                            text = "Welcome to Pawn!",
                            style = Typography.h2,
                            color = Color.White,
                        )
                        Spacer(modifier = Modifier.padding(10.dp))
                        Text(
                            text = "Learn multiple languages in one place",
                            style = Typography.h6,
                            color = Color.White,
                        )
                    }

                }
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(15.dp)
                ) {
                    RoundButton(
                        backgroundColor = DarkBlue,
                        size = 64.dp,
                        icon = R.drawable.home,
                        padding = 15.dp,
                        onClick = {
                            navController.popBackStack()
                        })
                }
            }

            Column(Modifier.offset(y = (((deviceWidthDp * 0.3) - 30) * -1).dp)) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 30.dp)
                        .aspectRatio(5f)
                        .background(Grey, RoundedCornerShape(20.dp))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(20.dp))
                            .clickable {
                                currentTab = if (currentTab == AuthTab.LOGIN) AuthTab.REGISTER
                                else AuthTab.LOGIN
                            }

                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .weight(1f)
                                .background(
                                    color = if (currentTab == AuthTab.LOGIN) DarkBlue else Color.Transparent,
                                    RoundedCornerShape(20.dp)
                                )
                                .clip(RoundedCornerShape(20.dp)),
                            contentAlignment = Center
                        ) {
                            Text(text = "Log in", style = Typography.h6, color = Color.White)
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .weight(1f)
                                .background(
                                    color = if (currentTab == AuthTab.REGISTER) DarkBlue else Color.Transparent,
                                    RoundedCornerShape(20.dp)
                                )
                                .clip(RoundedCornerShape(20.dp)),
                            contentAlignment = Center
                        ) {
                            Text(text = "Register", style = Typography.h6, color = Color.White)
                        }
                    }
                }

                if (currentTab == AuthTab.LOGIN) Login(
                    navController,
                    onLogin = { emailOrUsername, password ->
                        authViewModel.login(emailOrUsername, password)
                    })
                else Register(
                    nativeLanguage,
                    onClickNativeLanguage = {
                        toggleBottomSheet()
                    },
                    onRegister = { email, username, password, passwordVerify, nativeLanguage ->
                        authViewModel.registerAccount(
                            email,
                            username,
                            password,
                            passwordVerify,
                            nativeLanguage
                        )
                    }
                )
            }
        }
        if (errorMsg.isNotEmpty()) {
            CustomDialog(
                title = "Whoops!",
                content = errorMsg,
                icon = R.drawable.error,
                onDismiss = { authViewModel.clearError() }
            )
        }
    }

}



