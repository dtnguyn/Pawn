package com.nguyen.polyglot.ui.screens.stats

import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.nguyen.polyglot.R
import com.nguyen.polyglot.model.Language
import com.nguyen.polyglot.ui.SharedViewModel
import com.nguyen.polyglot.ui.components.stats.CustomPieChart
import com.nguyen.polyglot.ui.components.stats.LanguageBox
import com.nguyen.polyglot.ui.theme.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.nguyen.polyglot.util.DataStoreUtils
import com.nguyen.polyglot.util.UIState


@Composable
fun StatsScreen(navController: NavController, sharedViewModel: SharedViewModel, viewModel: StatsViewModel) {

    val languages by remember { mutableStateOf(sharedViewModel.pickedLanguagesUIState.value.value) }
    val context = LocalContext.current

    val languageReportsUIState by viewModel.languageReportsUIState
    var languageReports by remember { mutableStateOf(languageReportsUIState.value)}

    LaunchedEffect(true){
        viewModel.getLanguageReports(DataStoreUtils.getAccessTokenFromDataStore(context))
    }

    LaunchedEffect(languageReportsUIState){
        when(languageReportsUIState){
            is UIState.Initial -> {
                Log.d("StatsScreen", "languageReports initial: ${languageReportsUIState.value}")

            }
            is UIState.Loading -> {
                Log.d("StatsScreen", "languageReports loading: ${languageReportsUIState.value}")

            }
            is UIState.Error -> {
                Log.d("StatsScreen", "languageReports error: ${languageReportsUIState.value}")
            }
            is UIState.Loaded -> {
                if(languageReportsUIState.value != null){
                    Log.d("StatsScreen", "languageReports: ${languageReportsUIState.value}")
                    languageReports = languageReportsUIState.value
                }
            }

        }
    }

    Scaffold(backgroundColor = Color.White) {
        Column {
            Column(Modifier.padding(20.dp)) {
                Row() {
                    Image(
                        painter = painterResource(R.drawable.me),
                        contentDescription = "avatar",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .clickable(onClick = {})
                    )
                    Spacer(modifier = Modifier.padding(3.dp))
                    Column(Modifier.align(CenterVertically)) {
                        Text(text = "Adron", style = Typography.h3)
                        Text(text = "Joined since 2020", style = Typography.body1, fontSize = 18.sp)
                    }

                }
                Spacer(modifier = Modifier.padding(10.dp))
                CustomPieChart(
                    title = "All saved words",
                    subtitle = "100 words",
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.5f)
                        .align(CenterHorizontally),
                    progress = listOf(30f, 20f, 10f, 40f),
                    colors = listOf(
                        LightRed, LightGreen, LightOrange, Blue
                    ),
                    isDonut = true,
                )
                Spacer(modifier = Modifier.padding(10.dp))

            }
            Row(
                Modifier.horizontalScroll(rememberScrollState())
            ) {
                LanguageBox(language = Language("All", "All"))
                languages?.forEach {
                    LanguageBox(language = it, isActive = it.value == "English")
                }
            }
        }
    }

}