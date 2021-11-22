package com.nguyen.polyglot.ui.components.wordReview

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nguyen.polyglot.R
import com.nguyen.polyglot.ui.theme.Green
import com.nguyen.polyglot.ui.theme.LightGreen
import com.nguyen.polyglot.ui.theme.Typography
import kotlin.math.roundToInt


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
            text = "Congratulation! You have got ${scoreInPercentage.roundToInt()}% of the answers correct!",
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
                text = "Try Again",
                style = Typography.h4,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
        }


    }

}