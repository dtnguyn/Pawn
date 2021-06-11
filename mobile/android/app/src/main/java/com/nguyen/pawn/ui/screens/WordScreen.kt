package com.nguyen.pawn.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.materialIcon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.VerticalAlignmentLine
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.nguyen.pawn.R
import com.nguyen.pawn.model.Definition
import com.nguyen.pawn.ui.components.RoundButton
import com.nguyen.pawn.ui.components.RoundedSquareButton
import com.nguyen.pawn.ui.components.SavedWordItem
import com.nguyen.pawn.ui.components.word.DefinitionItem
import com.nguyen.pawn.ui.theme.*

@ExperimentalAnimationApi
@ExperimentalFoundationApi
@Composable
fun WordScreen() {

    val definitions = listOf(
        Definition(
            "1",
            "Sprinkle or season (food) with pepper.",
            "noun",
            "season to taste with salt and pepper"
        ),
        Definition(
            "2",
            "Sprinkle or season (food) with pepper.",
            "noun",
            "season to taste with salt and pepper"
        ),
        Definition(
            "3",
            "Sprinkle or season (food) with pepper.",
            "noun",
            "season to taste with salt and pepper"
        ),
        Definition(
            "4",
            "Sprinkle or season (food) with pepper.",
            "noun",
            "season to taste with salt and pepper"
        ),
        Definition(
            "5",
            "Sprinkle or season (food) with pepper.",
            "noun",
            "season to taste with salt and pepper"
        ),
        Definition(
            "6",
            "Sprinkle or season (food) with pepper.",
            "noun",
            "season to taste with salt and pepper"
        ),
    )

    val lazyListState = rememberLazyListState()


    Surface {
        Scaffold(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(),
            backgroundColor = Color.White,
            topBar = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .background(ReallyRed)
                        .padding(horizontal = 5.dp),
                ) {

                    IconButton(modifier = Modifier.align(Alignment.CenterStart),onClick = { /*TODO*/ }) {
                        Image(
                            painter = painterResource(id = R.drawable.back_32_white),
                            contentDescription = "Back icon"
                        )
                    }
//                    if(lazyListState.firstVisibleItemIndex >= 1) {
//
//                    }
                    AnimatedVisibility(
                        visible = lazyListState.firstVisibleItemIndex >= 1,
                        enter = slideInVertically(initialOffsetY = { 400 }) + expandVertically(expandFrom = Alignment.Top) + fadeIn(initialAlpha = 0.3f),
                        exit = slideOutVertically(targetOffsetY = { 400 }) + shrinkVertically() + fadeOut(),
                        modifier = Modifier.align(
                            Alignment.Center
                        )
                    ) {
                        Text(
                            text = "Pepper",
                            style = Typography.h4,
                            color = Color.White,

                        )
                    }

                }
            }
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp),
                backgroundColor = ReallyRed,
                elevation = 0.dp,
                shape = RectangleShape
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .padding(bottom = 50.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "Pepper", style = Typography.h1, color = Color.White)
                    Spacer(modifier = Modifier.padding(5.dp))
                    Text(
                        text = "UK /ˈpepə(r)/",
                        style = Typography.body2,
                        color = Color.White
                    )
                    Text(
                        text = "UK /ˈpepə(r)/",
                        style = Typography.body2,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.padding(10.dp))
                }
            }

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