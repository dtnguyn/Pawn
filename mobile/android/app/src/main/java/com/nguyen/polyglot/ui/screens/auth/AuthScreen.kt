package com.nguyen.polyglot.ui.screens

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
import com.nguyen.polyglot.R
import com.nguyen.polyglot.ui.components.RoundButton
import com.nguyen.polyglot.ui.components.auth.Login
import com.nguyen.polyglot.util.AuthTab
import com.nguyen.polyglot.util.UtilFunctions.convertHeightToDp
import androidx.compose.runtime.*
import androidx.compose.ui.draw.clip
import com.nguyen.polyglot.model.AuthStatus
import com.nguyen.polyglot.model.Token
import com.nguyen.polyglot.ui.SharedViewModel
import com.nguyen.polyglot.ui.components.CustomDialog
import com.nguyen.polyglot.ui.components.auth.LanguageBottomSheetContent
import com.nguyen.polyglot.ui.components.auth.Register
import com.nguyen.polyglot.ui.theme.*
import com.nguyen.polyglot.ui.screens.auth.AuthViewModel
import com.nguyen.polyglot.util.Constants.allLanguages
import com.nguyen.polyglot.util.DataStoreUtils.getAccessTokenFromDataStore
import com.nguyen.polyglot.util.DataStoreUtils.getRefreshTokenFromDataStore
import com.nguyen.polyglot.util.DataStoreUtils.saveTokenToDataStore
import com.nguyen.polyglot.util.DataStoreUtils.saveUserToDataStore
import com.nguyen.polyglot.util.UIState
import kotlinx.coroutines.launch

private const val TAG = "AuthScreen"

@ExperimentalMaterialApi
@Composable
fun AuthScreen(
    authViewModel: AuthViewModel,
    sharedViewModel: SharedViewModel,
    navController: NavController
) {


    /**   ---STATES---   */

    /** States from AuthViewModel */
    val tokenUIState: UIState<Token> by authViewModel.tokenUIState
    val authStatusUIState: UIState<AuthStatus> by sharedViewModel.authStatusUIState

    /** Local ui states */
    var errorMsg by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    var nativeLanguage by remember { mutableStateOf("") }
    var currentTab by remember { mutableStateOf(AuthTab.LOGIN) }


    /** Compose state */
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberBottomSheetState(
            initialValue = BottomSheetValue.Collapsed,
        )
    )
    val coroutineScope = rememberCoroutineScope()


    /** Helper variables */
    val deviceWidthDp = (convertHeightToDp(
        LocalContext.current.resources.displayMetrics.widthPixels,
        LocalContext.current.resources.displayMetrics
    ))
    val context = LocalContext.current


    /**   ---OBSERVERS---   */

    /** Initialize the token with the token
     * value stored in DataStore */
    LaunchedEffect(null) {
        authViewModel.initializeToken(
            getAccessTokenFromDataStore(context),
            getRefreshTokenFromDataStore(context)
        )
    }

    /** React to changes of [tokenUIState]*/
    LaunchedEffect(tokenUIState) {
        when (tokenUIState) {
            is UIState.Initial -> {
                // Do nothing
            }
            is UIState.Error -> {
                // Emit error message
                isLoading = false
                errorMsg = tokenUIState.errorMsg ?: ""
            }
            is UIState.Loading -> {
                // Turn on loading
                isLoading = true
            }
            is UIState.Loaded -> {
                // If the token is not null then go back home
                isLoading = false
                tokenUIState.value?.let {
                    sharedViewModel.checkAuthStatus(it.accessToken, it.accessToken)
                }
            }
        }
    }

    LaunchedEffect(authStatusUIState) {
        when (authStatusUIState) {
            is UIState.Initial -> {

            }
            is UIState.Loading -> {

            }
            is UIState.Error -> {

            }
            is UIState.Loaded -> {
                authStatusUIState.value?.user?.let {user ->
                    saveTokenToDataStore(context, authStatusUIState.value!!.token)
                    saveUserToDataStore(context, user)
                    sharedViewModel.resetPickedLanguages()
                    navController.navigate("home") {
                        popUpTo("home") { inclusive = true }
                    }
                }
            }

        }
    }

    /**   ---HELPER FUNCTIONS---   */

    /** Expand or collapse the bottom sheet view */
    fun toggleBottomSheet() {
        coroutineScope.launch {
            if (bottomSheetScaffoldState.bottomSheetState.isCollapsed) {
                bottomSheetScaffoldState.bottomSheetState.expand()
            } else {
                bottomSheetScaffoldState.bottomSheetState.collapse()
            }
        }
    }


    /**   ---COMPOSE UI---   */

    BottomSheetScaffold(
        backgroundColor = Color.White,
        sheetPeekHeight = 0.dp,
        sheetContent = {
            LanguageBottomSheetContent(
                languages = allLanguages,
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
                onDismiss = { coroutineScope.launch { authViewModel.clearError() } }
            )
        }
        if (isLoading) {
            // Show loading animation
        }
    }

}



