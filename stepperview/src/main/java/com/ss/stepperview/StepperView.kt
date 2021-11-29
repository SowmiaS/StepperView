package com.ss.stepperview

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.unit.dp
import kotlin.math.max

private const val LAYOUTID_STEPPERVIEW_INDICATOR = "LAYOUTID_STEPPERVIEW_INDICATOR"
private const val LAYOUTID_STEPPERVIEW_LINE = "LAYOUTID_STEPPERVIEW_LINE"

@Composable
fun StepperView(
    items: List<Any>,
    content: @Composable () -> Unit
) {
    StepperViewLayout {
        repeat(items.size) {
            StepperViewIndicator(modifier = Modifier.layoutId(LAYOUTID_STEPPERVIEW_INDICATOR.plus(it)))
        }
        repeat(items.size - 1) {
            StepperViewLine(modifier = Modifier.layoutId(LAYOUTID_STEPPERVIEW_LINE.plus(it)))
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
    Layout(
        content = { content() },
        modifier = modifier
    ) { measurables, constraints ->

        val horizontalSpacing = 50 // Spacing between Indicator and Step.
        var verticalSpacing = 100 // Spacing between Steps.

        val stepsPlaceables = measurables
            .filter {
                it.layoutId.toString().contains(LAYOUTID_STEPPERVIEW_LINE)
                    .not() && it.layoutId.toString().contains(LAYOUTID_STEPPERVIEW_INDICATOR).not()
            }
            .map { it.measure(constraints = constraints) }

        val stepIndicatorPlaceables = measurables
            .filter { it.layoutId.toString().contains(LAYOUTID_STEPPERVIEW_INDICATOR) }
            .map { it.measure(constraints = constraints) }

        val stepLinePlaceables = measurables
            .filter { it.layoutId.toString().contains(LAYOUTID_STEPPERVIEW_LINE) }
            .mapIndexed { index, measurable ->
                val lineHeight =
                    (stepsPlaceables[index].height / 2 + stepsPlaceables[index + 1].height / 2).plus(
                        verticalSpacing
                    ).minus(stepIndicatorPlaceables[index].height)
                measurable.measure(
                    constraints = constraints.copy(
                        minHeight = lineHeight,
                        maxHeight = lineHeight
                    )
                )
            }

        layout(constraints.maxWidth, constraints.maxHeight) {
            val noOfSteps = stepsPlaceables.size
            var y = 0

            // Either Step or Indicator can be bigger. So based on the size of step and height. Initial Y value is set.
            if (stepIndicatorPlaceables[0].height > stepsPlaceables[0].height) {
                y = (stepIndicatorPlaceables[0].height / 2) - (stepsPlaceables[0].height / 2)
                verticalSpacing += stepIndicatorPlaceables[0].height
            }
            for (i in 0 until noOfSteps) {
                val stepPlaceable = stepsPlaceables[i]
                val indicatorPlaceable = stepIndicatorPlaceables[i]
                var linePlaceable: Placeable? = null
                var linePlaceableWidth = 0

                // no of lines is one less than the no of steps.
                if (i <= stepLinePlaceables.size - 1) {
                    linePlaceable = stepLinePlaceables[i]
                    linePlaceableWidth = linePlaceable.width
                }

                // Placing Steps
                val x = max(indicatorPlaceable.width, linePlaceableWidth).plus(horizontalSpacing)
                stepPlaceable.placeRelative(x = x, y = y)
                y += stepPlaceable.height

                // Placing Indicators
                val indicatorY =
                    (y - (stepPlaceable.height / 2)) - (indicatorPlaceable.height / 2)   // depends on how indicator has to be shown centre, top, bottom . For now center
                indicatorPlaceable.placeRelative(x = 0, y = indicatorY)
                val indicatorXMid = indicatorPlaceable.width / 2

                // Placing Line
                val lineY = (y - (stepPlaceable.height / 2)) + indicatorPlaceable.height / 2
                linePlaceable?.placeRelative(x = indicatorXMid - linePlaceableWidth / 2, y = lineY)

                // Adding vertical spacing
                y += verticalSpacing
            }
        }
    }
}

@Composable
fun StepperViewIndicator(modifier: Modifier) {
    OutlinedButton(
        onClick = { /*TODO*/ },
        modifier = modifier.size(10.dp),
        shape = CircleShape,
        border = BorderStroke(1.dp, Color.Blue),
        contentPadding = PaddingValues(0.dp),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Blue)
    ) {
        //Icon(Icons.Default.Add, contentDescription = "StepperViewIndicator")
    }
}

@Composable
fun StepperViewLine(modifier: Modifier) {
    Box(
        modifier = modifier
            .width(1.dp)
            .background(Color.Blue)
    )
}
