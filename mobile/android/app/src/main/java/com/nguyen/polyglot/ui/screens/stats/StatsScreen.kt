package com.nguyen.polyglot.ui.screens.stats

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.nguyen.polyglot.R
import com.nguyen.polyglot.model.Language
import com.nguyen.polyglot.ui.SharedViewModel
import com.nguyen.polyglot.ui.components.stats.CustomBarChart
import com.nguyen.polyglot.ui.components.stats.CustomPieChart
import com.nguyen.polyglot.ui.components.stats.LanguageBox
import com.nguyen.polyglot.ui.theme.Typography
import com.nguyen.polyglot.util.DataStoreUtils
import com.nguyen.polyglot.util.UIState
import com.nguyen.polyglot.util.UtilFunctions.generateBackgroundColorForLanguage
import java.text.SimpleDateFormat


@SuppressLint("SimpleDateFormat", "WeekBasedYear")
@Composable
fun StatsScreen(
    navController: NavController,
    sharedViewModel: SharedViewModel,
    viewModel: StatsViewModel
) {

    val languages = sharedViewModel.pickedLanguagesUIState.value.value
    val context = LocalContext.current

    val languageReportsUIState by viewModel.languageReportsUIState
    var languageReports by remember { mutableStateOf(languageReportsUIState.value) }
    var allSavedWordsCount by remember { mutableStateOf(0) }
    var pieProgress by remember { mutableStateOf<List<Float>>(listOf()) }
    var pieColors by remember { mutableStateOf<List<Color>>(listOf()) }
    var activeLanguageId by remember { mutableStateOf<String?>(null) }
    var activeLanguageIndex by remember { mutableStateOf(-1) }
    var pieTitle by remember { mutableStateOf("All saved words") }
    var pieSubtitle by remember { mutableStateOf("$allSavedWordsCount words") }

    val currentUser = sharedViewModel.authStatusUIState.value.value?.user

    fun findLanguage(id: String): Language? {
        val result = languages?.filter {
            it.id == id
        }
        return if (result.isNullOrEmpty()) null
        else result.first()
    }

    LaunchedEffect(true) {
        viewModel.getLanguageReports(DataStoreUtils.getAccessTokenFromDataStore(context))
    }

    LaunchedEffect(languageReportsUIState) {
        when (languageReportsUIState) {
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
                if (languageReportsUIState.value != null) {
                    Log.d("StatsScreen", "languageReports: ${languageReportsUIState.value}")
                    var count = 0
                    val progress = arrayListOf<Float>()
                    val colors = arrayListOf<Color>()
                    languageReportsUIState.value!!.forEach {
                        count += it.savedWordCount
                        progress.add(it.savedWordCount.toFloat())
                        colors.add(generateBackgroundColorForLanguage(it.languageId))
                    }
                    allSavedWordsCount = count
                    pieSubtitle = "$allSavedWordsCount"
                    pieProgress = progress
                    pieColors = colors
                    languageReports = languageReportsUIState.value
                }
            }

        }
    }

    Scaffold(backgroundColor = Color.White) {
        Column(
            Modifier
                .padding(bottom = 55.dp)
                .verticalScroll(rememberScrollState())) {
            languageReports?.let {
                Column(Modifier.padding(20.dp)) {
                    Row() {
                        Image(
                            painter = painterResource(context.resources.getIdentifier(currentUser?.avatar ?: "human1", "drawable", "com.nguyen.polyglot")),
                            contentDescription = "avatar",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .clickable(onClick = {})
                        )
                        Spacer(modifier = Modifier.padding(3.dp))
                        Column(Modifier.align(CenterVertically)) {
                            Text(text = currentUser?.username ?: "unknown", style = Typography.h3)
                            if (currentUser?.createdAt != null) {
                                Text(
                                    text = "${stringResource(id = R.string.joined_since)} ${
                                        SimpleDateFormat("MMM, YYYY").format(
                                            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(
                                                currentUser.createdAt
                                            )!!
                                        )
                                    }",
                                    style = Typography.body1,
                                    fontSize = 18.sp
                                )
                            }

                        }

                    }
                    Spacer(modifier = Modifier.padding(10.dp))
                    CustomPieChart(
                        title = if (activeLanguageId == null) stringResource(id = R.string.all_saved_words)
                        else findLanguage(activeLanguageId!!)?.value ?: "",
                        subtitle = "$pieSubtitle ${stringResource(id = R.string.words)}",
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                            .align(CenterHorizontally),
                        progress = pieProgress,
                        colors = pieColors,
                        isDonut = true,
                        activePie = activeLanguageIndex
                    )
                    Spacer(modifier = Modifier.padding(10.dp))

                }
                Row(
                    Modifier.horizontalScroll(rememberScrollState())
                ) {
                    LanguageBox(
                        language = Language("All", stringResource(id = R.string.all)),
                        wordCount = allSavedWordsCount,
                        isActive = activeLanguageId == null,
                        onClick = {
                            activeLanguageId = null
                            activeLanguageIndex = -1
                        }
                    )
                    languageReports?.forEachIndexed { index, item ->
                        LanguageBox(
                            language = findLanguage(item.languageId),
                            wordCount = item.savedWordCount,
                            isActive = activeLanguageId == item.languageId,
                            onClick = { id ->
                                activeLanguageId = id
                                activeLanguageIndex = index
                                pieTitle = findLanguage(activeLanguageId!!)?.value ?: "unknown"
                                pieSubtitle = "${item.savedWordCount}"
                            }
                        )
                    }
                }
                Spacer(modifier = Modifier.padding(10.dp))

                if (activeLanguageIndex > -1) {
                    CustomBarChart(
                        title = "Word Topic Stats",
                        subtitle = "This shows how many words in each category",
                        values = it[activeLanguageIndex].wordTopicReports.map { topic -> topic.wordCount },
                        labels = it[activeLanguageIndex].wordTopicReports.map { topic -> topic.value },
                        maxValue = it[activeLanguageIndex].wordTopicReports.maxOf { topic -> topic.wordCount }
                    )
                }

            }

        }
    }

}