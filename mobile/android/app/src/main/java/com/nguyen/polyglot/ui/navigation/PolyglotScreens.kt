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
    object NewsDetail : PolyglotScreens("newsDetail", null)
    object VideoDetail : PolyglotScreens("videoDetail", null)
    object WordReviewMenu : PolyglotScreens("wordReviewMenu", null)
    object WordReview : PolyglotScreens("wordReview", null)
    object WordReviewResult : PolyglotScreens("wordReviewResult", null)
    object Stats : PolyglotScreens("stats", R.drawable.stats)
    object Account : PolyglotScreens("account", null)
    object Setting : PolyglotScreens("setting", null)


}