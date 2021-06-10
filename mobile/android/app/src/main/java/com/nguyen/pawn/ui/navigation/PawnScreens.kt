package com.nguyen.pawn.ui.navigation

import androidx.annotation.DrawableRes
import com.nguyen.pawn.R

sealed class PawnScreens(val route: String, @DrawableRes val icon: Int?) {
    object Home : PawnScreens("home", R.drawable.home)
    object Feeds : PawnScreens("feeds", R.drawable.fire)
    object Chat : PawnScreens("chat", R.drawable.chat)
    object Word : PawnScreens("word", null)
}