package com.moderndev.polyglot.ui.components.account

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Start
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.moderndev.polyglot.ui.theme.Typography
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterEnd
import androidx.compose.ui.res.stringResource
import com.moderndev.polyglot.R
import com.moderndev.polyglot.ui.theme.Blue
import com.moderndev.polyglot.util.Constants.animalIcons
import com.moderndev.polyglot.util.Constants.humanIcons


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
                text = stringResource(id = R.string.pick_your_avatar),
                style = Typography.h5,
                color = Color.Black,
                modifier = Modifier.align(Center)
            )
            Text(
                text = stringResource(id = R.string.save),
                style = Typography.body1,
                color = Blue,
                modifier = Modifier
                    .align(CenterEnd)
                    .padding(end = 5.dp)
                    .clickable {
                        onSave()
                    }
            )
        }

        Spacer(modifier = Modifier.padding(10.dp))
        Text(
            text = stringResource(id = R.string.animal),
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
            text = stringResource(id = R.string.animal),
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