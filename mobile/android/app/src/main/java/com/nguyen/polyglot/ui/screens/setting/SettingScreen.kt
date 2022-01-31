package com.nguyen.polyglot.ui.screens.setting

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterEnd
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.android.billingclient.api.*
import com.nguyen.polyglot.R
import com.nguyen.polyglot.ui.SharedViewModel
import com.nguyen.polyglot.ui.components.CircularLoadingBar
import com.nguyen.polyglot.ui.components.auth.LanguageBottomSheetContent
import com.nguyen.polyglot.ui.theme.*
import com.nguyen.polyglot.util.Constants
import com.nguyen.polyglot.util.Constants.dailyWordTopics
import com.nguyen.polyglot.util.Constants.productIds
import com.nguyen.polyglot.util.DataStoreUtils
import com.nguyen.polyglot.util.UIState
import com.nguyen.polyglot.util.UtilFunctions.fromLanguageId
import kotlinx.coroutines.launch
import java.util.*

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SettingScreen(activity: Activity, navController: NavController, sharedViewModel: SharedViewModel) {

    val authStatusUIState by sharedViewModel.authStatusUIState
    var user by remember { mutableStateOf(authStatusUIState.value?.user) }
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    var dailyWordCount by remember {
        mutableStateOf(
            sharedViewModel.authStatusUIState.value.value?.user?.dailyWordCount?.toFloat() ?: 3f
        )
    }
    var topicMenuExpanded by remember { mutableStateOf(false) }

    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState()
    val coroutineScope = rememberCoroutineScope()
    var loading by remember { mutableStateOf(false) }

    var skuDetails: SkuDetails? = null


    val billingClient = BillingClient.newBuilder(context)
        .enablePendingPurchases()
        .setListener { billingResult, mutableList ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && mutableList != null) {
                for (purchase in mutableList) {
                    if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED && !purchase.isAcknowledged) {
                        coroutineScope.launch {
                            sharedViewModel.purchasePremium(
                                DataStoreUtils.getAccessTokenFromDataStore(
                                    context
                                ),
                                purchase.orderId,
                                purchase.purchaseToken,
                                purchase.purchaseTime.toString()
                            )
                        }
                    }
                }
            }
        }
        .build()


    billingClient.startConnection(object: BillingClientStateListener {
        override fun onBillingServiceDisconnected() {

        }

        override fun onBillingSetupFinished(result: BillingResult) {
            if(result.responseCode == BillingClient.BillingResponseCode.OK){
                val getProductDetailsQuery = SkuDetailsParams.newBuilder().setSkusList(productIds).setType(BillingClient.SkuType.INAPP).build()
                billingClient.querySkuDetailsAsync(getProductDetailsQuery, object: SkuDetailsResponseListener {
                    override fun onSkuDetailsResponse(
                        billingResult: BillingResult,
                        list: MutableList<SkuDetails>?
                    ) {
                        if(billingResult.responseCode == BillingClient.BillingResponseCode.OK && list != null){
                            skuDetails = list.first()
                        }
                    }

                })
            }
        }

    })





    fun toggleBottomSheet() {
        coroutineScope.launch {
            if (bottomSheetScaffoldState.bottomSheetState.isCollapsed) {
                bottomSheetScaffoldState.bottomSheetState.expand()
            } else {
                bottomSheetScaffoldState.bottomSheetState.collapse()
            }
        }
    }

    suspend fun updateAppLanguage(newLanguage: String) {
        if (authStatusUIState.value == null) return
        if (user == null) return

        sharedViewModel.updateUser(
            accessToken = DataStoreUtils.getAccessTokenFromDataStore(context),
            currentAuthStatus = authStatusUIState.value!!,
            username = user!!.username,
            email = user!!.email,
            avatar = user!!.avatar,
            dailyWordCount = user!!.dailyWordCount,
            notificationEnabled = user!!.notificationEnabled,
            nativeLanguageId = user!!.nativeLanguageId,
            appLanguageId = newLanguage,
            dailyWordTopic = user!!.dailyWordTopic,
            feedTopics = user!!.feedTopics
        )
    }

    suspend fun updateNotificationEnabled(newStatus: Boolean) {
        if (authStatusUIState.value == null) return
        if (user == null) return

        sharedViewModel.updateUser(
            accessToken = DataStoreUtils.getAccessTokenFromDataStore(context),
            currentAuthStatus = authStatusUIState.value!!,
            username = user!!.username,
            email = user!!.email,
            avatar = user!!.avatar,
            dailyWordCount = user!!.dailyWordCount,
            notificationEnabled = newStatus,
            nativeLanguageId = user!!.nativeLanguageId,
            appLanguageId = user!!.appLanguageId,
            dailyWordTopic = user!!.dailyWordTopic,
            feedTopics = user!!.feedTopics
        )
    }

    suspend fun updateDailyWordCount(newDailyWordCount: Int) {
        if (authStatusUIState.value == null) return
        if (user == null) return

        sharedViewModel.updateUser(
            accessToken = DataStoreUtils.getAccessTokenFromDataStore(context),
            currentAuthStatus = authStatusUIState.value!!,
            username = user!!.username,
            email = user!!.email,
            avatar = user!!.avatar,
            dailyWordCount = newDailyWordCount,
            notificationEnabled = user!!.notificationEnabled,
            nativeLanguageId = user!!.nativeLanguageId,
            appLanguageId = user!!.appLanguageId,
            dailyWordTopic = user!!.dailyWordTopic,
            feedTopics = user!!.feedTopics
        )
    }

    suspend fun updateDailyWordTopic(newTopic: String) {
        if (authStatusUIState.value == null) return
        if (user == null) return

        sharedViewModel.updateUser(
            accessToken = DataStoreUtils.getAccessTokenFromDataStore(context),
            currentAuthStatus = authStatusUIState.value!!,
            username = user!!.username,
            email = user!!.email,
            avatar = user!!.avatar,
            dailyWordCount = user!!.dailyWordCount,
            notificationEnabled = user!!.notificationEnabled,
            nativeLanguageId = user!!.nativeLanguageId,
            appLanguageId = user!!.appLanguageId,
            dailyWordTopic = newTopic,
            feedTopics = user!!.feedTopics
        )
    }


    LaunchedEffect(authStatusUIState) {
        when (authStatusUIState) {
            is UIState.Initial -> {

            }

            is UIState.Error -> {
                Toast.makeText(context, authStatusUIState.errorMsg, Toast.LENGTH_SHORT).show()
                loading = false
            }
            is UIState.Loading -> {
                loading = true
            }
            is UIState.Loaded -> {
                Log.d("SettingScreen", "authStatus Loaded ${authStatusUIState.value}")
                loading = false
                authStatusUIState.value?.user?.let {
                    user = it

                    user?.appLanguageId?.let { id ->
                        val locale = Locale(id)
                        configuration.setLocale(locale)
                        val resources = context.resources
                        resources.updateConfiguration(configuration, resources.displayMetrics)
                    }

                    // Store the state to DataStore
                    DataStoreUtils.saveTokenToDataStore(context, authStatusUIState.value!!.token)
                    DataStoreUtils.saveUserToDataStore(context, it)
                }
            }
        }
    }

    BottomSheetScaffold(
        backgroundColor = Color.White,
        sheetPeekHeight = 0.dp,
        sheetContent = {
            LanguageBottomSheetContent(
                languages = Constants.allLanguages,
                onLanguageClick = { language ->
                    coroutineScope.launch {
                        updateAppLanguage(language)
                    }
                    toggleBottomSheet()
                }
            )
        },
        scaffoldState = bottomSheetScaffoldState,
        sheetShape = RoundedCornerShape(topStart = 60.dp, topEnd = 60.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier
                        .size(30.dp)
                        .align(
                            Alignment.CenterStart
                        )
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.back_32_black),
                        contentDescription = "back icon"
                    )
                }
                Text(
                    text = stringResource(id = R.string.settings),
                    style = Typography.h4,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            Spacer(modifier = Modifier.padding(10.dp))
            Card(
                shape = RoundedCornerShape(15.dp),
                backgroundColor = DarkBlue,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)

            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Text(
                        text = stringResource(id = R.string.subscription),
                        style = Typography.h6,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.padding(10.dp))
                    Card(
                        shape = RoundedCornerShape(15.dp),
                        backgroundColor = LightGrey,
                        modifier = Modifier
                            .clip(RoundedCornerShape(15.dp))
                            .fillMaxWidth()
                            .clickable { }
                    ) {
                        Column(Modifier.padding(10.dp)) {
                            Box(Modifier.fillMaxWidth()) {
                                Text(
                                    text = stringResource(id = R.string.free),
                                    style = Typography.h6,
                                    fontSize = 18.sp,
                                    color = Color.Black
                                )
                                Image(
                                    painter = painterResource(id = R.drawable.check_icon_blue_32),
                                    contentDescription = "Check icon",
                                    modifier = Modifier
                                        .size(24.dp)
                                        .align(
                                            CenterEnd
                                        )
                                )
                            }

                            Spacer(modifier = Modifier.padding(5.dp))
                            Text(
                                text = stringResource(id = R.string.only_essential_features),
                                style = Typography.body2,
                                color = Color.Black
                            )
                            Spacer(modifier = Modifier.padding(3.dp))

                        }
                    }
                    Spacer(modifier = Modifier.padding(10.dp))
                    Card(
                        shape = RoundedCornerShape(15.dp),
                        backgroundColor = ReallyRed,
                        modifier = Modifier
                            .clip(RoundedCornerShape(15.dp))
                            .fillMaxWidth()
                            .clickable {
                                billingClient.launchBillingFlow(activity, BillingFlowParams.newBuilder().setSkuDetails(skuDetails!!).build())

//                                skuDetails?.let {
//                                    billingClient.launchBillingFlow(activity, BillingFlowParams.newBuilder().setSkuDetails(it).build())
//                                }
                            }
                    ) {
                        Column(Modifier.padding(10.dp)) {
                            Text(
                                text = stringResource(id = R.string.premium),
                                style = Typography.h5
                            )
                            Text(
                                text = "$9.99 • ${stringResource(id = R.string.one_time)}",
                                style = Typography.body2,
                            )

                            Spacer(modifier = Modifier.padding(5.dp))
                            Row {
                                Text(
                                    text = "•",
                                    style = Typography.h3,
                                )
                                Text(
                                    text = stringResource(id = R.string.no_ads),
                                    style = Typography.body1,
                                    modifier = Modifier
                                        .align(CenterVertically)
                                        .padding(start = 5.dp)
                                )
                            }

                            Spacer(modifier = Modifier.padding(3.dp))

                            Row {
                                Text(
                                    text = "•",
                                    style = Typography.h3,
                                )
                                Text(
                                    text = stringResource(id = R.string.translated_video_cation),
                                    style = Typography.body1,
                                    modifier = Modifier
                                        .align(CenterVertically)
                                        .padding(start = 5.dp)
                                )
                            }

                            Spacer(modifier = Modifier.padding(3.dp))

                            Row {
                                Text(
                                    text = "•",
                                    style = Typography.h3,
                                )
                                Text(
                                    text = stringResource(id = R.string.topic_filter),
                                    style = Typography.body1,
                                    modifier = Modifier
                                        .align(CenterVertically)
                                        .padding(start = 5.dp)
                                )
                            }

                            Spacer(modifier = Modifier.padding(3.dp))

                            Row {
                                Text(
                                    text = "•",
                                    style = Typography.h3,
                                )
                                Text(
                                    text = stringResource(id = R.string.support),
                                    style = Typography.body1,
                                    modifier = Modifier
                                        .align(CenterVertically)
                                        .padding(start = 5.dp)
                                )
                            }
                            Spacer(modifier = Modifier.padding(3.dp))
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.padding(5.dp))

            Card(
                shape = RoundedCornerShape(15.dp),
                backgroundColor = LightGrey,
                elevation = 4.dp,
                onClick = {
                    toggleBottomSheet()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)

            ) {
                Column(Modifier.padding(10.dp)) {
                    Column(Modifier.padding(10.dp)) {
                        Text(
                            text = stringResource(id = R.string.app_language),
                            style = Typography.h6
                        )
                        Spacer(modifier = Modifier.padding(5.dp))
                        Text(
                            text = fromLanguageId(user?.appLanguageId ?: "en") ?: "English",
                            style = Typography.body1,
                            color = Color.Black
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.padding(5.dp))

            Card(
                shape = RoundedCornerShape(15.dp),
                backgroundColor = LightGrey,
                elevation = 4.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                Column(Modifier.padding(10.dp)) {
                    Box(Modifier.fillMaxWidth()) {
                        Text(
                            text = stringResource(id = R.string.notifications),
                            style = Typography.h6
                        )
                        Switch(
                            checked = user?.notificationEnabled ?: true,
                            onCheckedChange = {
                                coroutineScope.launch {
                                    updateNotificationEnabled(it)
                                }
                            },
                            modifier = Modifier.align(CenterEnd)
                        )
                    }
                    Spacer(modifier = Modifier.padding(5.dp))
                    Text(
                        text = stringResource(id = R.string.notifications_sub),
                        style = Typography.body1,
                        color = Color.Black
                    )
                }
            }

            Spacer(modifier = Modifier.padding(5.dp))

            Card(
                shape = RoundedCornerShape(15.dp),
                backgroundColor = LightGrey,
                elevation = 4.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)

            ) {
                Column(Modifier.padding(10.dp)) {
                    Column(Modifier.padding(5.dp)) {
                        Text(
                            text = stringResource(id = R.string.daily_words),
                            style = Typography.h6
                        )
                        Spacer(modifier = Modifier.padding(5.dp))
                        Text(
                            text = stringResource(id = R.string.daily_word_count),
                            style = Typography.h6,
                            fontSize = 16.sp
                        )
                        Text(
                            text = stringResource(id = R.string.daily_word_count_sub),
                            style = Typography.body1,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.padding(5.dp))
                        Slider(
                            value = dailyWordCount,
                            onValueChange = {
                                coroutineScope.launch {
                                    dailyWordCount = it
                                    updateDailyWordCount(it.toInt())
                                }
                            },
                            valueRange = 0f..5f,
                            steps = 4
                        )
                        Spacer(modifier = Modifier.padding(2.dp))
                        Text(
                            text = "${dailyWordCount.toInt()} ${stringResource(id = R.string.words)}",
                            style = Typography.body1,
                            color = Color.Black,
                            modifier = Modifier.align(CenterHorizontally)
                        )
                        Spacer(modifier = Modifier.padding(5.dp))
                        Text(
                            text = stringResource(id = R.string.daily_word_topic),
                            style = Typography.h6,
                            fontSize = 16.sp
                        )
                        Spacer(modifier = Modifier.padding(3.dp))

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .fillMaxWidth()
                                .background(Grey, RoundedCornerShape(10.dp))
                                .clickable {
                                    topicMenuExpanded = !topicMenuExpanded
                                }
                                .padding(15.dp)

                        ) {
                            Text("${user?.dailyWordTopic}")
                            DropdownMenu(
                                expanded = topicMenuExpanded,
                                onDismissRequest = {
                                    topicMenuExpanded = false
                                },
                                modifier = Modifier.fillMaxWidth(0.8f)
                            ) {
                                dailyWordTopics.forEach { topic ->
                                    DropdownMenuItem(
                                        onClick = {
                                            coroutineScope.launch {
                                                updateDailyWordTopic(topic)
                                                topicMenuExpanded = false
                                            }
                                        },
                                        modifier = Modifier.background(color = if (topic == user?.dailyWordTopic) Grey else Color.White)
                                    ) {
                                        Text(
                                            text = topic,
                                            style = Typography.body1,
                                            fontSize = 18.sp
                                        )
                                    }
                                }

                            }
                        }
                    }
                }
            }

        }
    }

    if (loading) {
        CircularLoadingBar()
    }

}

fun Context.getActivity(): AppCompatActivity? = when (this) {
    is AppCompatActivity -> this
    is ContextWrapper -> baseContext.getActivity()
    else -> null
}