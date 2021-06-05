package com.nguyen.pawn.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.nguyen.pawn.ui.screens.ChatScreen
import com.nguyen.pawn.ui.screens.FeedScreen
import com.nguyen.pawn.ui.screens.HomeScreen
import com.nguyen.pawn.ui.theme.PawnTheme
import com.nguyen.pawn.ui.utils.BottomNavigationScreens

@ExperimentalFoundationApi
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PawnApp() {

    val navController = rememberNavController()
    val items = listOf(
        BottomNavigationScreens.Home,
        BottomNavigationScreens.Feeds,
        BottomNavigationScreens.Chat
    )

    PawnTheme {
        Scaffold(
            bottomBar = {
                BottomNavigation {
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentRoute = navBackStackEntry?.destination?.route
                    items.forEach { screen ->
                        BottomNavigationItem(
                            icon = {
                                Icon(
                                    painterResource(id = screen.icon), null,
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
        ) {

            NavHost(navController, startDestination = BottomNavigationScreens.Home.route) {
                composable(BottomNavigationScreens.Home.route) { HomeScreen() }
                composable(BottomNavigationScreens.Feeds.route) { FeedScreen() }
                composable(BottomNavigationScreens.Chat.route) { ChatScreen() }

            }
        }
    }
}

