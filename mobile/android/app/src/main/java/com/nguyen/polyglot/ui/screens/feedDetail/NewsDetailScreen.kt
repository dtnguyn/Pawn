package com.nguyen.polyglot.ui.screens.feedDetail

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.nguyen.polyglot.R
import com.nguyen.polyglot.model.FeedDetail
import com.nguyen.polyglot.model.NewsDetail
import com.nguyen.polyglot.ui.SharedViewModel
import com.nguyen.polyglot.ui.components.SelectableText
import com.nguyen.polyglot.ui.theme.Blue
import com.nguyen.polyglot.ui.theme.LightGreen
import com.nguyen.polyglot.ui.theme.LightRed
import com.nguyen.polyglot.ui.theme.Typography
import com.nguyen.polyglot.util.DataStoreUtils
import com.nguyen.polyglot.util.ShimmerAnimation
import com.nguyen.polyglot.util.UIState
import com.skydoves.landscapist.CircularReveal
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch


@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@OptIn(ExperimentalPagerApi::class)
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

    var loading by remember { mutableStateOf(false) }

    var currentFocusWord: String? by remember { mutableStateOf(null) }
    val coroutineScope = rememberCoroutineScope()
    val selectableTextRange = remember { mutableStateOf<TextRange?>(null)}

    val bottomSheetScaffoldState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden
    )

    LaunchedEffect(true) {
        viewModel.getNewsDetail(
            DataStoreUtils.getAccessTokenFromDataStore(context),
            newsId,
            newsUrl
        )
    }

    LaunchedEffect(newsDetailUIState) {
        when (newsDetailUIState) {
            is UIState.Initial -> {

            }
            is UIState.Loading -> {
                loading = true

            }
            is UIState.Error -> {
                loading = false

            }
            is UIState.Loaded -> {
                loading = false
                newsDetail = newsDetailUIState.value
            }


        }
    }
    LaunchedEffect(bottomSheetScaffoldState.isVisible){
        if(!bottomSheetScaffoldState.isVisible){
            selectableTextRange.value = null
        }
    }

    ModalBottomSheetLayout(

        sheetShape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
        sheetContent = {
            Column(Modifier.padding(20.dp)) {
                Text(text = "\"${currentFocusWord ?: ""}\"", style = Typography.h2)
                Spacer(modifier = Modifier.padding(10.dp))
                Button(
                    onClick = {

                    },
                    content = {
                        Text(text = "Look up definitions", style = Typography.h6)
                    },
                    shape = RoundedCornerShape(30.dp),
                    colors = ButtonDefaults.buttonColors(LightRed),
                )

                Spacer(modifier = Modifier.padding(5.dp))
                Button(
                    onClick = {

                    },
                    content = {
                        Text(text = "Find images", style = Typography.h6)
                    },
                    shape = RoundedCornerShape(30.dp),
                    colors = ButtonDefaults.buttonColors(LightGreen),
                )

            }
        },
        sheetState = bottomSheetScaffoldState,
    ) {

        Scaffold(
            backgroundColor = Color.White, modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()

        ) {
            LazyColumn(modifier = Modifier.padding(horizontal = 8.dp)) {


                item {
                    Spacer(modifier = Modifier.padding(10.dp))

                    Text(text = title, style = Typography.h3)
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

                    Spacer(modifier = Modifier.padding(10.dp))

                    if (loading) {

                        ShimmerAnimation(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(30.dp), shape = RoundedCornerShape(40.dp)
                        )
                        Spacer(modifier = Modifier.padding(3.dp))
                        ShimmerAnimation(
                            modifier = Modifier
                                .fillMaxWidth(0.9f)
                                .height(30.dp), shape = RoundedCornerShape(40.dp)
                        )
                        Spacer(modifier = Modifier.padding(3.dp))
                        ShimmerAnimation(
                            modifier = Modifier
                                .fillMaxWidth(0.93f)
                                .height(30.dp), shape = RoundedCornerShape(40.dp)
                        )
                        Spacer(modifier = Modifier.padding(3.dp))
                        ShimmerAnimation(
                            modifier = Modifier
                                .fillMaxWidth(0.98f)
                                .height(30.dp), shape = RoundedCornerShape(40.dp)
                        )
                        Spacer(modifier = Modifier.padding(3.dp))
                        ShimmerAnimation(
                            modifier = Modifier
                                .fillMaxWidth(0.3f)
                                .height(30.dp), shape = RoundedCornerShape(40.dp)
                        )
                        Spacer(modifier = Modifier.padding(7.dp))

                        ShimmerAnimation(
                            modifier = Modifier
                                .fillMaxWidth(0.95f)
                                .height(30.dp), shape = RoundedCornerShape(40.dp)
                        )
                        Spacer(modifier = Modifier.padding(3.dp))
                        ShimmerAnimation(
                            modifier = Modifier
                                .fillMaxWidth(0.9f)
                                .height(30.dp), shape = RoundedCornerShape(40.dp)
                        )
                        Spacer(modifier = Modifier.padding(3.dp))
                        ShimmerAnimation(
                            modifier = Modifier
                                .fillMaxWidth(0.93f)
                                .height(30.dp), shape = RoundedCornerShape(40.dp)
                        )
                        Spacer(modifier = Modifier.padding(3.dp))
                        ShimmerAnimation(
                            modifier = Modifier
                                .fillMaxWidth(0.87f)
                                .height(30.dp), shape = RoundedCornerShape(40.dp)
                        )
                        Spacer(modifier = Modifier.padding(3.dp))
                        ShimmerAnimation(
                            modifier = Modifier
                                .fillMaxWidth(0.97f)
                                .height(30.dp), shape = RoundedCornerShape(40.dp)
                        )
                        Spacer(modifier = Modifier.padding(3.dp))
                        ShimmerAnimation(
                            modifier = Modifier
                                .fillMaxWidth(0.91f)
                                .height(30.dp), shape = RoundedCornerShape(40.dp)
                        )
                        Spacer(modifier = Modifier.padding(3.dp))
                        ShimmerAnimation(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(30.dp), shape = RoundedCornerShape(40.dp)
                        )
                        Spacer(modifier = Modifier.padding(3.dp))
                        ShimmerAnimation(
                            modifier = Modifier
                                .fillMaxWidth(0.96f)
                                .height(30.dp), shape = RoundedCornerShape(40.dp)
                        )
                        Spacer(modifier = Modifier.padding(3.dp))
                        ShimmerAnimation(
                            modifier = Modifier
                                .fillMaxWidth(0.8f)
                                .height(30.dp), shape = RoundedCornerShape(40.dp)
                        )
                        Spacer(modifier = Modifier.padding(3.dp))
                    } else {
                        newsDetail?.content?.value?.let {
                            SelectableText(
                                text = it,
                                textRange = selectableTextRange,
                                onLongClick = { word ->
                                    if(word != "") {
                                        currentFocusWord = word
                                        coroutineScope.launch {
                                            bottomSheetScaffoldState.show()
                                        }
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }

}


