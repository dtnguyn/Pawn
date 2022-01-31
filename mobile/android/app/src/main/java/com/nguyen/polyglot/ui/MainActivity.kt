package com.nguyen.polyglot.ui

import android.content.Context
import android.content.ContextWrapper
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.pager.ExperimentalPagerApi
import com.nguyen.polyglot.ui.screens.account.AccountViewModel
import com.nguyen.polyglot.ui.screens.auth.AuthViewModel
import com.nguyen.polyglot.ui.screens.definition.WordDetailViewModel
import com.nguyen.polyglot.ui.screens.newsDetail.NewsDetailViewModel
import com.nguyen.polyglot.ui.screens.feeds.FeedViewModel
import com.nguyen.polyglot.ui.screens.home.HomeViewModel
import com.nguyen.polyglot.ui.screens.search.SearchViewModel
import com.nguyen.polyglot.ui.screens.stats.StatsViewModel
import com.nguyen.polyglot.ui.screens.videoDetail.VideoDetailViewModel
import com.nguyen.polyglot.ui.screens.wordReview.WordReviewViewModel
import com.nguyen.polyglot.util.DataStoreUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.*

@ExperimentalPagerApi
@ExperimentalMaterialApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val homeViewModel: HomeViewModel by viewModels()
    private val authViewModel: AuthViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by viewModels()
    private val searchViewModel: SearchViewModel by viewModels()
    private val wordDetailViewModel: WordDetailViewModel by viewModels()
    private val feedViewModel: FeedViewModel by viewModels()
    private val newsDetailViewModel: NewsDetailViewModel by viewModels()
    private val videoDetailViewModel: VideoDetailViewModel by viewModels()
    private val wordReviewViewModel: WordReviewViewModel by viewModels()
    private val statsViewModel: StatsViewModel by viewModels()
    private val accountViewModel: AccountViewModel by viewModels()



    @ExperimentalFoundationApi
    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SetUp()
            PolygotApp(
                activity = this,
                authViewModel = authViewModel,
                homeViewModel = homeViewModel,
                sharedViewModel = sharedViewModel,
                searchViewModel = searchViewModel,
                wordDetailViewModel = wordDetailViewModel,
                feedViewModel = feedViewModel,
                newsDetailViewModel = newsDetailViewModel,
                videoDetailViewModel = videoDetailViewModel,
                wordReviewViewModel = wordReviewViewModel,
                statsViewModel = statsViewModel,
                accountViewModel = accountViewModel,
            )
        }
    }

    @Composable
    fun SetUp(){

        val context = LocalContext.current
        val configuration = LocalConfiguration.current

        LaunchedEffect(true){
            val user = DataStoreUtils.getUserFromDataStore(context)
            user?.appLanguageId?.let{id ->
                val locale = Locale(id)
                configuration.setLocale(locale)
                val resources = context.resources
                resources.updateConfiguration(configuration, resources.displayMetrics)
            }

        }
    }



}

