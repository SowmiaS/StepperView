package com.ss.stepperview

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout

@Composable
fun StepperView(
    items: List<Any>,
    content: @Composable () -> Unit
) {
    StepperViewLayout {
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