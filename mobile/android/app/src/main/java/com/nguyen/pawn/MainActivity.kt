package com.nguyen.pawn

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import com.nguyen.pawn.ui.PawnApp
import com.nguyen.pawn.ui.viewmodels.WordViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: WordViewModel by viewModels()

    @ExperimentalAnimationApi
    @OptIn(ExperimentalFoundationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PawnApp(viewModel)
        }
    }
}

