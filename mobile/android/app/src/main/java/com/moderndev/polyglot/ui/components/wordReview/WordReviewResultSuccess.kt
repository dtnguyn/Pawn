package com.moderndev.polyglot.ui.components.wordReview

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.moderndev.polyglot.ui.theme.Green
import com.moderndev.polyglot.ui.theme.LightGreen
import com.moderndev.polyglot.ui.theme.Typography
import kotlin.math.roundToInt
import com.moderndev.polyglot.R


@Composable
fun WordReviewResultSuccess(
    score: String,
    scoreInPercentage: Float,
    onTryAgain: () -> Unit,
){

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {

        Image(
            painter = painterResource(id = R.drawable.trophy),
            contentDescription = null,
            modifier = Modifier.fillMaxWidth(80f)
        )

        Spacer(modifier = Modifier.padding(20.dp))

        Text(
            text = score,
            style = Typography.h1,
            fontSize = 60.sp,
            color = Green,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
        )
        Spacer(modifier = Modifier.padding(10.dp))
        Text(
            text = "${stringResource(id = R.string.success_message1)} ${scoreInPercentage.roundToInt()}% ${stringResource(id = R.string.success_message2)}",
            fontSize = 18.sp,
            style = Typography.body1,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.padding(10.dp))
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(5f),
            colors = ButtonDefaults.buttonColors(LightGreen),
            shape = RoundedCornerShape(15.dp),
            onClick = {
                onTryAgain()
            }) {
            Text(
                text = stringResource(id = R.string.try_again),
                style = Typography.h4,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
        }


    }

}