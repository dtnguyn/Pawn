package com.nguyen.polygot.ui.screens.feedDetail

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.nguyen.polygot.R
import com.nguyen.polygot.model.Feed
import com.nguyen.polygot.model.FeedDetail
import com.nguyen.polygot.model.NewsDetail
import com.nguyen.polygot.ui.SharedViewModel
import com.nguyen.polygot.ui.components.HtmlText
import com.nguyen.polygot.ui.theme.Typography
import com.nguyen.polygot.util.DataStoreUtils
import com.nguyen.polygot.util.UIState
import com.skydoves.landscapist.CircularReveal
import com.skydoves.landscapist.glide.GlideImage
import kotlin.coroutines.coroutineContext

@Composable
fun NewsDetailScreen(
    viewModel: FeedDetailViewModel,
    sharedViewModel: SharedViewModel,
    navController: NavController,
    title: String,
    publishedDate: String?,
    thumbnail: String?,
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
        LazyColumn(modifier = Modifier.padding(horizontal = 8.dp)) {

            item{
                Spacer(modifier = Modifier.padding(10.dp))

                Text(text = title, style = Typography.h5)
                Spacer(modifier = Modifier.padding(2.dp))
                Text(text = publishedDate?.substring(0, 10) ?: "", style = Typography.subtitle2)
                Log.d("NewsDetailScreen", "url: ${newsUrl}")

                Spacer(modifier = Modifier.padding(5.dp))

                GlideImage(
                    imageModel = thumbnail ?: "",
                    // Crop, Fit, Inside, FillHeight, FillWidth, None
                    contentScale = ContentScale.FillWidth,
                    // shows an image with a circular revealed animation.
                    circularReveal = CircularReveal(duration = 250),
                    // shows a placeholder ImageBitmap when loading.
                    placeHolder = ImageBitmap.imageResource(id = R.drawable.cat_loading_icon),
                    // shows an error ImageBitmap when the request failed.
                    error = ImageBitmap.imageResource(R.drawable.image_loading_error),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(15.dp))
                )

                Spacer(modifier = Modifier.padding(5.dp))

                newsDetail?.content?.value?.let{
                    HtmlText(html = it, modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight())
                }
            }


        }
    }

}