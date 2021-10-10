package com.nguyen.polyglot.ui.screens.feedDetail

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.NavController
import com.nguyen.polyglot.ui.SharedViewModel

@ExperimentalMaterialApi
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun FeedDetailScreen(
    viewModel: FeedDetailViewModel,
    sharedViewModel: SharedViewModel,
    navController: NavController,
    title: String?,
    publishedDate: String?,
    thumbnail: String?,
    feedUrl: String?,
    feedId: String?,
    feedType: String?
) {



    if (feedType == "news") {
        if (feedId != null && feedUrl != null && title != null) {
            NewsDetailScreen(
                viewModel = viewModel,
                sharedViewModel = sharedViewModel,
                navController = navController,
                newsId = feedId,
                newsUrl = feedUrl.replace("<", "/" ),
                title = title,
                publishedDate = publishedDate,
                thumbnail = thumbnail?.replace("<", "/" )
            )
        }
    } else if (feedType == "video") {
        VideoDetailScreen()
    }

}