package com.ss.stepperview

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.dp

@Composable
fun StepperView(
    items: List<Any>,
    content: @Composable () -> Unit
) {
    StepperViewLayout {
        repeat(items.size - 1) {
            StepperViewIndicator()
        }
        repeat(items.size - 1) {
            StepperViewLine()
        }
        content()
    }
}

@Composable
fun Step(
    content: @Composable () -> Unit
) {
    content()
}

@Composable
fun StepperViewLayout(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Layout(content = { content() }) { measurables, constraints ->
        val placeables = measurables.map { it.measure(constraints = constraints) }
        layout(constraints.maxWidth, constraints.maxHeight) {
            var y = 0
            placeables.forEach {
                it.placeRelative(x = 0, y = y)
                y += it.height
            }
        }
    }
}

@Composable
fun StepperViewIndicator() {
    OutlinedButton(
        onClick = { /*TODO*/ },
        modifier = Modifier.size(50.dp),
        shape = CircleShape,
        border = BorderStroke(1.dp, Color.Blue),
        contentPadding = PaddingValues(0.dp),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Blue)
    ) {
        Icon(Icons.Default.Add, contentDescription = "StepperViewIndicator")
    }
}

@Composable
fun StepperViewLine() {
    Box(
        modifier = Modifier
            .height(50.dp)
            .width(10.dp)
            .background(Color.Blue)
    )
}
