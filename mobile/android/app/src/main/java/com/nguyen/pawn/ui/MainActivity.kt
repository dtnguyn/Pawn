package com.nguyen.pawn.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import com.nguyen.pawn.ui.PawnApp
import com.nguyen.pawn.ui.viewmodels.AuthViewModel
import com.nguyen.pawn.ui.viewmodels.WordViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val wordViewModel: WordViewModel by viewModels()
    private val authViewModel: AuthViewModel by viewModels()

    @ExperimentalAnimationApi
    @OptIn(ExperimentalFoundationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PawnApp(wordViewModel, authViewModel)
        }
    }
}

