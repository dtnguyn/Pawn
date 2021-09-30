package com.nguyen.polygot.ui.screens.feedDetail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import com.nguyen.polygot.ui.SharedViewModel

@Composable
fun FeedDetailScreen(
    viewModel: FeedDetailViewModel,
    sharedViewModel: SharedViewModel,
    navController: NavController,
    feedUrl: String?,
    feedId: String?,
    feedType: String?
) {



    if (feedType == "news") {
        if (feedId != null && feedUrl != null) {
            NewsDetailScreen(
                viewModel = viewModel,
                sharedViewModel = sharedViewModel,
                navController = navController,
                newsId = feedId,
                newsUrl = feedUrl.replace("<", "/" )
            )
        }
    } else if (feedType == "video") {
        VideoDetailScreen()
    }

}