package com.nguyen.polygot.ui.navigation

import androidx.annotation.DrawableRes
import com.nguyen.polygot.R

sealed class PolygotScreens(val route: String, @DrawableRes val icon: Int?) {
    object Home : PolygotScreens("home", R.drawable.home)
    object Feeds : PolygotScreens("feeds", R.drawable.newspaper)
    object Search : PolygotScreens("search", R.drawable.search_white)
    object WordDetail : PolygotScreens("wordDetail", null)
    object Auth : PolygotScreens("auth", null)
    object ChangePassword : PolygotScreens("password", null)
    object VerifyCode : PolygotScreens("verify", null)
    object FeedDetail : PolygotScreens("feedDetail", null)
}