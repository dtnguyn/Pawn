package com.nguyen.polygot.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import com.google.accompanist.pager.ExperimentalPagerApi
import com.nguyen.polygot.ui.screens.auth.AuthViewModel
import com.nguyen.polygot.ui.screens.definition.WordDetailViewModel
import com.nguyen.polygot.ui.screens.feedDetail.FeedDetailViewModel
import com.nguyen.polygot.ui.screens.feeds.FeedViewModel
import com.nguyen.polygot.ui.screens.home.HomeViewModel
import com.nguyen.polygot.ui.screens.search.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint

@ExperimentalPagerApi
@ExperimentalMaterialApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val homeViewModel: HomeViewModel by viewModels()
    private val authViewModel: AuthViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by viewModels()
    private val searchViewModel: SearchViewModel by viewModels()
    private val wordDetailViewModel: WordDetailViewModel by viewModels()
    private val feedViewModel: FeedViewModel by viewModels()
    private val feedDetailViewModel: FeedDetailViewModel by viewModels()

    @ExperimentalFoundationApi
    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            PolygotApp(
                authViewModel = authViewModel,
                homeViewModel = homeViewModel,
                sharedViewModel = sharedViewModel,
                searchViewModel = searchViewModel,
                wordDetailViewModel = wordDetailViewModel,
                feedViewModel = feedViewModel,
                feedDetailViewModel = feedDetailViewModel
            )
        }



    }
}

