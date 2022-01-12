package com.ss.stepperview.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun StepperViewLine(
    modifier: Modifier = Modifier,
    color: Color = Color.Blue,
    width: Dp = 2.dp
) {
    Box(
        modifier = modifier
            .layoutId(LAYOUTID_STEPPERVIEW_LINE)
            .width(width)
            .background(color)
    )

}
