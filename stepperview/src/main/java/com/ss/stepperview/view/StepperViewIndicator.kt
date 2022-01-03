package com.ss.stepperview.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonColors
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun StepperViewIndicator(
    modifier: Modifier,
    border: BorderStroke = BorderStroke(1.dp, Color.Blue),
    contentPadding: PaddingValues = PaddingValues(5.dp),
    size: Dp = 10.dp,
    color: Color = Color.Yellow
) {
    OutlinedButton(
        onClick = { /*TODO*/ },
        modifier = modifier.size(size),
        shape = CircleShape,
        border = border,
        contentPadding = contentPadding,
        colors = ButtonDefaults.outlinedButtonColors(backgroundColor = color)
    ) {
        //Icon(Icons.Default.Add, contentDescription = "StepperViewIndicator")
    }
}