package com.nguyen.polygot.ui

import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.nguyen.polygot.R
import com.nguyen.polygot.model.AuthStatus
import com.nguyen.polygot.model.Token
import com.nguyen.polygot.ui.navigation.PolygotScreens
import com.nguyen.polygot.ui.screens.*
import com.nguyen.polygot.ui.screens.auth.AuthViewModel
import com.nguyen.polygot.ui.screens.auth.ChangePasswordScreen
import com.nguyen.polygot.ui.screens.auth.VerifyCodeScreen
import com.nguyen.polygot.ui.screens.definition.WordDetailViewModel
import com.nguyen.polygot.ui.screens.feedDetail.FeedDetailScreen
import com.nguyen.polygot.ui.screens.feedDetail.FeedDetailViewModel
import com.nguyen.polygot.ui.screens.feeds.FeedViewModel
import com.nguyen.polygot.ui.screens.home.HomeViewModel
import com.nguyen.polygot.ui.screens.search.SearchViewModel
import com.nguyen.polygot.ui.theme.PawnTheme
import com.nguyen.polygot.util.DataStoreUtils


@ExperimentalPagerApi
@ExperimentalMaterialApi
@ExperimentalAnimationApi
@ExperimentalFoundationApi
@Composable
fun PolygotApp(
    authViewModel: AuthViewModel,
    homeViewModel: HomeViewModel,
    wordDetailViewModel: WordDetailViewModel,
    searchViewModel: SearchViewModel,
    sharedViewModel: SharedViewModel,
    feedViewModel: FeedViewModel,
    feedDetailViewModel: FeedDetailViewModel,
) {

    val navController = rememberNavController()
    val items = listOf(
        PolygotScreens.Home,
        PolygotScreens.Search,
        PolygotScreens.Feeds,
    )

    val context = LocalContext.current
    LaunchedEffect(true) {
        Log.d("PawnApp", "pawn user ${DataStoreUtils.getUserFromDataStore(context)}")
        sharedViewModel.initializeAuthStatus(
            AuthStatus(
                user = DataStoreUtils.getUserFromDataStore(context),
                token = Token(
                    DataStoreUtils.getAccessTokenFromDataStore(context),
                    DataStoreUtils.getRefreshTokenFromDataStore(context)
                )
            )
        )
    }

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

            NavHost(navController, startDestination = PolygotScreens.Home.route) {
                composable(PolygotScreens.Home.route) {
                    HomeScreen(
                        homeViewModel = homeViewModel,
                        sharedViewModel = sharedViewModel,
                        navController = navController
                    )
                }
                composable(PolygotScreens.Feeds.route) {
                    FeedScreen(
                        sharedViewModel = sharedViewModel,
                        feedViewModel = feedViewModel,
                        navController = navController
                    )
                }
                composable(PolygotScreens.Search.route) {
                    SearchScreen(
                        searchViewModel,
                        sharedViewModel,
                        navController
                    )
                }
                composable("${PolygotScreens.WordDetail.route}/{wordValue}/{language}") {
                    WordDetailScreen(
                        navController = navController,
                        viewModel = wordDetailViewModel,
                        sharedViewModel = sharedViewModel,
                        wordValue = it.arguments?.getString("wordValue"),
                        language = it.arguments?.getString("language")
                    )
                }
                composable(PolygotScreens.Auth.route) {
                    AuthScreen(
                        authViewModel = authViewModel,
                        sharedViewModel = sharedViewModel,
                        navController = navController
                    )
                }
                composable(PolygotScreens.ChangePassword.route) { ChangePasswordScreen(navController = navController) }
                composable(PolygotScreens.VerifyCode.route) { VerifyCodeScreen(navController = navController) }
                composable("${PolygotScreens.FeedDetail.route}/{feedId}/{title}/{publishedDate}/{thumbnail}/{feedType}/{feedUrl}") {
                    FeedDetailScreen(
                        viewModel = feedDetailViewModel,
                        sharedViewModel = sharedViewModel,
                        navController = navController,
                        feedUrl = it.arguments?.getString("feedUrl"),
                        feedId = it.arguments?.getString("feedId"),
                        feedType = it.arguments?.getString("feedType"),
                        title = it.arguments?.getString("title"),
                        publishedDate = it.arguments?.getString("publishedDate"),
                        thumbnail = it.arguments?.getString("thumbnail"),
                    )
                }

            }
        }
    }
}



