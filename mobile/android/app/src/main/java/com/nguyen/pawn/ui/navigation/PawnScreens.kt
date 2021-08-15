package com.nguyen.pawn.ui.navigation

import androidx.annotation.DrawableRes
import com.nguyen.pawn.R

sealed class PawnScreens(val route: String, @DrawableRes val icon: Int?) {
    object Home : PawnScreens("home", R.drawable.home)
    object Feeds : PawnScreens("feeds", R.drawable.newspaper)
    object Search : PawnScreens("search", R.drawable.search_white)
    object WordDetail : PawnScreens("wordDetail", null)
    object Auth : PawnScreens("auth", null)
    object ChangePassword : PawnScreens("password", null)
    object VerifyCode : PawnScreens("verify", null)
}