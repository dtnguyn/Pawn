package com.nguyen.pawn.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Card
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.nguyen.pawn.R
import com.nguyen.pawn.model.Definition
import com.nguyen.pawn.ui.components.RoundButton
import com.nguyen.pawn.ui.components.RoundedSquareButton
import com.nguyen.pawn.ui.components.SavedWordItem
import com.nguyen.pawn.ui.components.word.DefinitionItem
import com.nguyen.pawn.ui.theme.*

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

    Surface {
        Scaffold(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(),
            backgroundColor = Color.White,
            topBar = {
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .background(ReallyRed)) {

                }
            }
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxHeight(),
            ) {

                item {
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
                                .fillMaxHeight(),
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

                }

                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .zIndex(1f)
                            .background(Color.Transparent)
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