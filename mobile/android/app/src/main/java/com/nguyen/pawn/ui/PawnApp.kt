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
import com.nguyen.pawn.R
import com.nguyen.pawn.ui.screens.ChatScreen
import com.nguyen.pawn.ui.screens.FeedScreen
import com.nguyen.pawn.ui.screens.HomeScreen
import com.nguyen.pawn.ui.theme.PawnTheme
import com.nguyen.pawn.ui.navigation.PawnScreens
import com.nguyen.pawn.ui.screens.WordScreen
import com.nguyen.pawn.ui.viewmodels.WordViewModel


@ExperimentalAnimationApi
@ExperimentalFoundationApi
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PawnApp(wordViewModel: WordViewModel) {

    val navController = rememberNavController()
    val items = listOf(
        PawnScreens.Home,
        PawnScreens.Feeds,
        PawnScreens.Chat
    )

    PawnTheme {
        Scaffold(
            bottomBar = {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route
                if(currentRoute in setOf("home", "feeds", "chat")){
                    BottomNavigation {
                        items.forEach { screen ->
                            BottomNavigationItem(
                                icon = {
                                    Icon(
                                        painterResource(id = screen.icon?: R.drawable.home), null,
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
                composable(PawnScreens.Home.route) { HomeScreen(viewModel = wordViewModel, navController = navController) }
                composable(PawnScreens.Feeds.route) { FeedScreen() }
                composable(PawnScreens.Chat.route) { ChatScreen() }
                composable(PawnScreens.Word.route) { WordScreen(navController = navController) }
            }
        }
    }
}



