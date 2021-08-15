package com.nguyen.pawn.ui

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.nguyen.pawn.R
import com.nguyen.pawn.model.WordDetail
import com.nguyen.pawn.ui.navigation.PawnScreens
import com.nguyen.pawn.ui.screens.*
import com.nguyen.pawn.ui.screens.auth.AuthViewModel
import com.nguyen.pawn.ui.screens.auth.ChangePasswordScreen
import com.nguyen.pawn.ui.screens.auth.VerifyCodeScreen
import com.nguyen.pawn.ui.screens.definition.WordDetailViewModel
import com.nguyen.pawn.ui.screens.home.HomeViewModel
import com.nguyen.pawn.ui.theme.PawnTheme


@ExperimentalPagerApi
@ExperimentalMaterialApi
@ExperimentalAnimationApi
@ExperimentalFoundationApi
@Composable
fun PawnApp(
    authViewModel: AuthViewModel,
    homeViewModel: HomeViewModel,
    wordDetailViewModel: WordDetailViewModel,
    sharedViewModel: SharedViewModel
) {

    val navController = rememberNavController()
    val items = listOf(
        PawnScreens.Home,
        PawnScreens.Search,
        PawnScreens.Feeds,
    )

    PawnTheme {
        Scaffold(
            bottomBar = {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route
                if (currentRoute in setOf("home", "feeds", "search")) {
                    BottomNavigation {
                        items.forEach { screen ->
                            BottomNavigationItem(
                                icon = {
                                    Icon(
                                        painterResource(id = screen.icon ?: R.drawable.home), null,
                                        Modifier
                                            .width(28.dp)
                                            .height(28.dp)
                                    )
                                },
                                selected = currentRoute == screen.route,
                                onClick = {
                                    navController.navigate(screen.route) {
                                        navController.graph.startDestinationRoute?.let {
                                            popUpTo(it) {
                                                saveState = true
                                            }
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            )
                        }
                    }
                }

            }
        ) {

            NavHost(navController, startDestination = PawnScreens.Home.route) {
                composable(PawnScreens.Home.route) {
                    HomeScreen(
                        homeViewModel = homeViewModel,
                        sharedViewModel = sharedViewModel,
                        navController = navController
                    )
                }
                composable(PawnScreens.Feeds.route) { FeedScreen() }
                composable(PawnScreens.Search.route) { SearchScreen() }
                composable("${PawnScreens.WordDetail.route}/{wordValue}/{language}") {
                    WordDetailScreen(
                        navController = navController,
                        viewModel = wordDetailViewModel,
                        wordValue = it.arguments?.getString("wordValue"),
                        language = it.arguments?.getString("language")
                    )
                }
                composable(PawnScreens.Auth.route) {
                    AuthScreen(
                        authViewModel = authViewModel,
                        sharedViewModel = sharedViewModel,
                        navController = navController
                    )
                }
                composable(PawnScreens.ChangePassword.route) { ChangePasswordScreen(navController = navController) }
                composable(PawnScreens.VerifyCode.route) { VerifyCodeScreen(navController = navController) }
            }
        }
    }
}



