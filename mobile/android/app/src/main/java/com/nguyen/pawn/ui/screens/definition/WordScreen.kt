package com.nguyen.pawn.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.nguyen.pawn.R
import com.nguyen.pawn.model.Definition
import com.nguyen.pawn.ui.components.RoundButton
import com.nguyen.pawn.ui.components.RoundedSquareButton
import com.nguyen.pawn.ui.components.word.DefinitionItem
import com.nguyen.pawn.ui.components.word.WordCollapseSection
import com.nguyen.pawn.ui.components.word.WordTopBar
import com.nguyen.pawn.ui.theme.*

@ExperimentalAnimationApi
@ExperimentalFoundationApi
@Composable
fun WordScreen(navController: NavController) {

    val definitions = listOf<Definition>(
//        Definition(
//            "1",
//            "Sprinkle or season (food) with pepper.",
//            "noun",
//            "season to taste with salt and pepper"
//        ),
//        Definition(
//            "2",
//            "Sprinkle or season (food) with pepper.",
//            "noun",
//            "season to taste with salt and pepper"
//        ),
//        Definition(
//            "3",
//            "Sprinkle or season (food) with pepper.",
//            "noun",
//            "season to taste with salt and pepper"
//        ),
//        Definition(
//            "4",
//            "Sprinkle or season (food) with pepper.",
//            "noun",
//            "season to taste with salt and pepper"
//        ),
//        Definition(
//            "5",
//            "Sprinkle or season (food) with pepper.",
//            "noun",
//            "season to taste with salt and pepper"
//        ),
//        Definition(
//            "6",
//            "Sprinkle or season (food) with pepper.",
//            "noun",
//            "season to taste with salt and pepper"
//        ),
    )

    val lazyListState = rememberLazyListState()

    Surface {

        Scaffold(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(),
            backgroundColor = Color.White,
            topBar = {
                WordTopBar(lazyListState = lazyListState, word = "Pepper", onBackClick = { navController.popBackStack() })
            }
        ) {
            WordCollapseSection()
            LazyColumn(
                modifier = Modifier.fillMaxHeight(),
                state = lazyListState,
            ) {

                item {
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp),
                    )
                }

                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .zIndex(1f)
                            .background(Color.White)
                            .offset(y = (-35).dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        RoundedSquareButton(backgroundColor = LightGreen, icon = R.drawable.copy) {}
                        RoundButton(
                            backgroundColor = LightGrey,
                            size = 70.dp,
                            icon = R.drawable.speaker,
                            padding = 10.dp
                        ) {}
                        RoundedSquareButton(backgroundColor = LightRed, icon = R.drawable.heart) {}
                    }
                }

                items(definitions.size) { index ->
                    DefinitionItem(
                        index = index,
                        definition = definitions[index]
                    )
                }
            }


        }
    }
}