package com.nguyen.polygot.ui.screens.feedDetail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.nguyen.polygot.model.Feed
import com.nguyen.polygot.model.FeedDetail
import com.nguyen.polygot.model.NewsDetail
import com.nguyen.polygot.ui.SharedViewModel
import com.nguyen.polygot.ui.components.HtmlText
import com.nguyen.polygot.util.DataStoreUtils
import com.nguyen.polygot.util.UIState
import kotlin.coroutines.coroutineContext

@Composable
fun NewsDetailScreen(
    viewModel: FeedDetailViewModel,
    sharedViewModel: SharedViewModel,
    navController: NavController,
    newsId: String,
    newsUrl: String
) {

    val context = LocalContext.current

    val newsDetailUIState: UIState<FeedDetail<NewsDetail>> by viewModel.newsDetailUIState
    var newsDetail by remember { mutableStateOf(newsDetailUIState.value) }

    LaunchedEffect(true) {
        viewModel.getNewsDetail(DataStoreUtils.getAccessTokenFromDataStore(context), newsId, newsUrl)
    }

    LaunchedEffect(newsDetailUIState) {
        when(newsDetailUIState){
            is UIState.Initial -> {

            }
            is UIState.Loading -> {


            }
            is UIState.Error -> {


            }
            is UIState.Loaded -> {
                newsDetail = newsDetailUIState.value
            }


        }
    }


    Scaffold(
        backgroundColor = Color.White, modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        Column {
            newsDetail?.content?.value?.let{
                HtmlText(html = it, modifier = Modifier.fillMaxWidth().fillMaxHeight())
            }

        }
    }

}