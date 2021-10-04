package com.nguyen.polyglot.ui.navigation

import androidx.annotation.DrawableRes
import com.nguyen.polyglot.R

sealed class PolyglotScreens(val route: String, @DrawableRes val icon: Int?) {
    object Home : PolyglotScreens("home", R.drawable.home)
    object Feeds : PolyglotScreens("feeds", R.drawable.newspaper)
    object Search : PolyglotScreens("search", R.drawable.search_white)
    object WordDetail : PolyglotScreens("wordDetail", null)
    object Auth : PolyglotScreens("auth", null)
    object ChangePassword : PolyglotScreens("password", null)
    object VerifyCode : PolyglotScreens("verify", null)
    object FeedDetail : PolyglotScreens("feedDetail", null)
}