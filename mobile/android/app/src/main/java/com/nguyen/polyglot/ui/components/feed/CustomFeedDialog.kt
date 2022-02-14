package com.nguyen.polyglot.ui.components.feed

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.ImageLoader
import coil.compose.rememberImagePainter
import com.nguyen.polyglot.R
import com.nguyen.polyglot.ui.theme.DarkBlue
import com.nguyen.polyglot.ui.theme.Grey
import com.nguyen.polyglot.ui.theme.ReallyRed
import com.nguyen.polyglot.ui.theme.Typography

@Composable
fun CustomFeedDialog(
    customUrl: String,
    currentType: String,
    imageLoader: ImageLoader,
    isLocked: Boolean,
    onDismiss: () -> Unit,
    onHandleUrlChange: (url: String) -> Unit,
    onChangeType: (type: String) -> Unit,
    onAddClick: (url: String) -> Unit,
) {
    Dialog(onDismissRequest = { onDismiss() }) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(15.dp)
        ) {

            Column(Modifier.padding(15.dp)) {
                Box(Modifier.fillMaxWidth()) {
                    IconButton(onClick = { onDismiss() }) {
                        Icon(Icons.Filled.Close, "")
                    }
                    Text(
                        text = stringResource(id = R.string.custom_feed),
                        style = Typography.h4,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                Image(
                    painter = rememberImagePainter(
                        data = if (currentType == "news") R.drawable.news_gif else R.drawable.video_gif,
                        imageLoader = imageLoader
                    ),
                    contentDescription = null,
                    modifier = Modifier
                        .size(256.dp)
                        .align(Alignment.CenterHorizontally)
                )
                Text(
                    text = stringResource(id = R.string.custom_feed_subtitle),
                    fontSize = 18.sp,
                    style = Typography.body1,
                    modifier = Modifier.padding(5.dp)
                )
                Spacer(Modifier.padding(3.dp))
                Row {
                    Text(
                        text = stringResource(id = R.string.news),
                        color = Color.White,
                        style = Typography.body1,
                        modifier = Modifier
                            .background(
                                if (currentType == "news") DarkBlue else Grey,
                                RoundedCornerShape(40.dp)
                            )
                            .clip(RoundedCornerShape(40.dp))
                            .clickable {
                                onChangeType("news")
                            }
                            .padding(10.dp)
                    )
                    Spacer(Modifier.padding(3.dp))
                    Text(
                        text = stringResource(id = R.string.video),
                        color = Color.White,
                        style = Typography.body1,
                        modifier = Modifier
                            .background(
                                if (currentType == "video") DarkBlue else Grey,
                                RoundedCornerShape(40.dp)
                            )
                            .clip(RoundedCornerShape(40.dp))
                            .clickable {
                                onChangeType("video")
                            }
                            .padding(10.dp)
                    )
                }
                Spacer(modifier = Modifier.padding(5.dp))

                TextField(
                    value = customUrl,
                    onValueChange = onHandleUrlChange,
                    label = {
                        Text(
                            text = stringResource(id = R.string.url_placholder),
                            style = Typography.body2,
                            color = Color.Gray
                        )
                    },
                    singleLine = true,
                    colors = TextFieldDefaults.textFieldColors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    ),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(5f)
                )


                Spacer(modifier = Modifier.padding(10.dp))

                if(isLocked){
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(5f),
                        colors = ButtonDefaults.buttonColors(ReallyRed),
                        shape = RoundedCornerShape(15.dp),
                        onClick = { onAddClick(customUrl) }) {
                        Text(
                            "Unlock Premium Plan",
                            color = Color.White,
                            style = Typography.h6,
                            modifier = Modifier.align(Alignment.CenterVertically)
                        )
                    }
                } else {
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(5f),
                        colors = ButtonDefaults.buttonColors(ReallyRed),
                        shape = RoundedCornerShape(15.dp),
                        onClick = { onAddClick(customUrl) }) {
                        Text(
                            stringResource(id = R.string.add),
                            color = Color.White,
                            style = Typography.h4,
                            modifier = Modifier.align(Alignment.CenterVertically)
                        )
                    }
                }


            }
        }

    }
}