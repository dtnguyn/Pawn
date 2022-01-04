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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nguyen.polyglot.R
import com.nguyen.polyglot.ui.theme.*

@Composable
fun WordReviewResultFailure(
    score: String,
    onTryAgain: () -> Unit,
){

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {

        Image(
            painter = painterResource(id = R.drawable.broken_trophy),
            contentDescription = null,
            modifier = Modifier.fillMaxWidth(80f)
        )

        Spacer(modifier = Modifier.padding(20.dp))

        Text(
            text = score,
            style = Typography.h1,
            fontSize = 60.sp,
            color = ReallyRed,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
        )
        Spacer(modifier = Modifier.padding(10.dp))
        Text(
            text = stringResource(id = R.string.fail_message),
            fontSize = 18.sp,
            style = Typography.body1,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.padding(10.dp))
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(5f),
            colors = ButtonDefaults.buttonColors(LightRed),
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
