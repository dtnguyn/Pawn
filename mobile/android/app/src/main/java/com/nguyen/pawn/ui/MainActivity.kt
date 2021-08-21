package com.nguyen.pawn.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import com.google.accompanist.pager.ExperimentalPagerApi
import com.nguyen.pawn.ui.screens.auth.AuthViewModel
import com.nguyen.pawn.ui.screens.definition.WordDetailViewModel
import com.nguyen.pawn.ui.screens.home.HomeViewModel
import com.nguyen.pawn.ui.screens.search.SearchViewModel
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

    @ExperimentalFoundationApi
    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PawnApp(
                authViewModel = authViewModel,
                homeViewModel = homeViewModel,
                sharedViewModel = sharedViewModel,
                searchViewModel = searchViewModel,
                wordDetailViewModel = wordDetailViewModel
            )
        }
    }
}

