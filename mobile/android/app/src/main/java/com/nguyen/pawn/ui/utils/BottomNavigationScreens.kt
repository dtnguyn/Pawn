package com.nguyen.pawn.ui.utils

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import com.nguyen.pawn.R

sealed class BottomNavigationScreens(val route: String, @DrawableRes val icon: Int) {
    object Home : BottomNavigationScreens("Home", R.drawable.home)
    object Feeds : BottomNavigationScreens("Feeds", R.drawable.fire)
    object Chat : BottomNavigationScreens("Chat", R.drawable.chat)
}