package com.nguyen.polyglot.ui

import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.nguyen.polyglot.R
import com.nguyen.polyglot.model.AuthStatus
import com.nguyen.polyglot.model.Token
import com.nguyen.polyglot.ui.navigation.PolyglotScreens
import com.nguyen.polyglot.ui.screens.*
import com.nguyen.polyglot.ui.screens.auth.AuthViewModel
import com.nguyen.polyglot.ui.screens.auth.ChangePasswordScreen
import com.nguyen.polyglot.ui.screens.auth.VerifyCodeScreen
import com.nguyen.polyglot.ui.screens.definition.WordDetailViewModel
import com.nguyen.polyglot.ui.screens.newsDetail.NewsDetailViewModel
import com.nguyen.polyglot.ui.screens.newsDetail.NewsDetailScreen
import com.nguyen.polyglot.ui.screens.feeds.FeedViewModel
import com.nguyen.polyglot.ui.screens.home.HomeViewModel
import com.nguyen.polyglot.ui.screens.newsDetail.VideoDetailScreen
import com.nguyen.polyglot.ui.screens.search.SearchViewModel
import com.nguyen.polyglot.ui.screens.stats.StatsScreen
import com.nguyen.polyglot.ui.screens.stats.StatsViewModel
import com.nguyen.polyglot.ui.screens.videoDetail.VideoDetailViewModel
import com.nguyen.polyglot.ui.screens.wordReview.WordReviewResultScreen
import com.nguyen.polyglot.ui.screens.wordReview.WordReviewScreen
import com.nguyen.polyglot.ui.screens.wordReview.WordReviewViewModel
import com.nguyen.polyglot.ui.theme.PawnTheme
import com.nguyen.polyglot.util.DataStoreUtils


@OptIn(ExperimentalComposeUiApi::class)
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
    newsDetailViewModel: NewsDetailViewModel,
    videoDetailViewModel: VideoDetailViewModel,
    wordReviewViewModel: WordReviewViewModel,
    statsViewModel: StatsViewModel,
) {

    val navController = rememberNavController()
    val items = listOf(
        PolyglotScreens.Home,
        PolyglotScreens.Search,
        PolyglotScreens.Feeds,
        PolyglotScreens.Stats
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
                if (currentRoute in setOf("home", "feeds", "search", "stats")) {
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

            NavHost(navController, startDestination = PolyglotScreens.Home.route) {
                composable(PolyglotScreens.Home.route) {
                    HomeScreen(
                        homeViewModel = homeViewModel,
                        sharedViewModel = sharedViewModel,
                        navController = navController
                    )

                }
                composable(PolyglotScreens.Feeds.route) {
                    FeedScreen(
                        sharedViewModel = sharedViewModel,
                        feedViewModel = feedViewModel,
                        navController = navController
                    )
                }
                composable(PolyglotScreens.Search.route) {
                    SearchScreen(
                        searchViewModel,
                        sharedViewModel,
                        navController
                    )
                }
                composable("${PolyglotScreens.WordDetail.route}/{wordValue}/{language}") {
                    WordDetailScreen(
                        navController = navController,
                        viewModel = wordDetailViewModel,
                        sharedViewModel = sharedViewModel,
                        wordValue = it.arguments?.getString("wordValue"),
                        language = it.arguments?.getString("language")
                    )
                }
                composable(PolyglotScreens.Auth.route) {
                    AuthScreen(
                        authViewModel = authViewModel,
                        sharedViewModel = sharedViewModel,
                        navController = navController
                    )
                }
                composable(PolyglotScreens.ChangePassword.route) {
                    ChangePasswordScreen(
                        navController = navController
                    )
                }
                composable(PolyglotScreens.VerifyCode.route) { VerifyCodeScreen(navController = navController) }
                composable(
                    "${PolyglotScreens.NewsDetail.route}/{feedId}/{feedUrl}",
                    arguments = listOf()
                ) {
                    NewsDetailScreen(
                        viewModel = newsDetailViewModel,
                        sharedViewModel = sharedViewModel,
                        navController = navController,
                        newsUrl = it.arguments?.getString("feedUrl") ?: "",
                        newsId = (it.arguments?.getString("feedId")) ?: "",
//                        title = if(it.arguments?.getString("title") == "null") null else it.arguments?.getString("title"),
//                        publishedDate = if(it.arguments?.getString("publishedDate") == "null") null else it.arguments?.getString("publishedDate"),
//                        thumbnail = if(it.arguments?.getString("thumbnail") == "null") null else it.arguments?.getString("thumbnail"),
                    )
                }

                composable("${PolyglotScreens.VideoDetail.route}/{videoId}") {
                    VideoDetailScreen(
                        viewModel = videoDetailViewModel,
                        sharedViewModel = sharedViewModel,
                        navController = navController,
                        videoId = it.arguments?.getString("videoId") ?: ""
                    )
                }
                composable(PolyglotScreens.WordReviewMenu.route) {
                    WordReviewMenuScreen(
                        navController = navController
                    )
                }
                composable(PolyglotScreens.WordReview.route) {
                    WordReviewScreen(
                        navController = navController,
                        viewModel = wordReviewViewModel,
                        sharedViewModel = sharedViewModel
                    )
                }
                composable(PolyglotScreens.WordReviewResult.route) {
                    WordReviewResultScreen(
                        navController = navController,
                        viewModel = wordReviewViewModel
                    )
                }

                composable(PolyglotScreens.Stats.route) {
                   StatsScreen(navController = navController, sharedViewModel = sharedViewModel, viewModel = statsViewModel)

                }
            }
        }
    }
}



