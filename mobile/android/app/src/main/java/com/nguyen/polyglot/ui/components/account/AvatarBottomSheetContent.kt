package com.nguyen.polyglot.ui.components.account

import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Start
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nguyen.polyglot.ui.theme.Typography
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterEnd
import androidx.compose.ui.platform.LocalContext
import com.nguyen.polyglot.ui.theme.Blue
import com.nguyen.polyglot.util.Constants.animalIcons
import com.nguyen.polyglot.util.Constants.humanIcons


@Composable
fun AvatarBottomSheetContent(currentAvatar: String?, onPickAvatar: (icon: String) -> Unit,onSave: () -> Unit) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Pick your avatar",
                style = Typography.h5,
                color = Color.Black,
                modifier = Modifier.align(Center)
            )
            Text(
                text = "Save",
                style = Typography.body1,
                color = Blue,
                modifier = Modifier.align(CenterEnd).padding(end = 5.dp).clickable {
                    onSave()
                }
            )
        }

        Spacer(modifier = Modifier.padding(10.dp))
        Text(
            text = "Human",
            style = Typography.h6,
            color = Color.Black,
            fontSize = 18.sp,
            modifier = Modifier.align(Start)
        )
        Spacer(modifier = Modifier.padding(5.dp))

        AvatarRows(
            icons = humanIcons,
            current = currentAvatar ?: "human1",
            onClick = {
                onPickAvatar(it)
            }
        )
        Spacer(modifier = Modifier.padding(10.dp))

        Text(
            text = "Animal",
            style = Typography.h6,
            color = Color.Black,
            fontSize = 18.sp,
            modifier = Modifier.align(Start)
        )
        Spacer(modifier = Modifier.padding(5.dp))

        AvatarRows(
            icons = animalIcons,
            current = currentAvatar ?: "human1",
            onClick = {
                onPickAvatar(it)
            }
        )

    }
}