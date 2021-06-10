package com.nguyen.pawn.ui.components.word

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nguyen.pawn.model.Definition
import com.nguyen.pawn.ui.theme.LightGreen
import com.nguyen.pawn.ui.theme.Typography

@Composable
fun DefinitionItem(index: Int, definition: Definition) {
    Card(
        shape = RoundedCornerShape(10.dp),
        elevation = 4.dp,
        backgroundColor = LightGreen,
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp)
    ) {

        Column(
            Modifier.padding(15.dp)
        ) {
            Text(text = "Definitions ${index + 1}", style = Typography.h6)
            Text(text = definition.partOfSpeech, style = Typography.subtitle1)
            Spacer(modifier = Modifier.padding(5.dp))
            Text(text = definition.meaning, style = Typography.h6)
            Spacer(modifier = Modifier.padding(10.dp))
            Text(text = "Example: ", style = Typography.h6)
            Spacer(modifier = Modifier.padding(5.dp))
            Text(text = definition.example, style = Typography.body1)
        }

    }
}