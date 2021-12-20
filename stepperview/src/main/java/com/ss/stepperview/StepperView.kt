package com.ss.stepperview

import android.os.Parcel
import android.os.Parcelable
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.*
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import kotlin.math.max

private const val LAYOUTID_STEPPERVIEW_INDICATOR = "LAYOUTID_STEPPERVIEW_INDICATOR"
private const val LAYOUTID_STEPPERVIEW_LINE = "LAYOUTID_STEPPERVIEW_LINE"

enum class StepsPerRow(val noOfSteps: Int){
    ONE(1), TWO(2)
}
@Composable
fun StepperView(
    items: List<Any>,
    stepsPerRow : StepsPerRow = StepsPerRow.ONE,
    content: @Composable StepScope.() -> Unit
) {

    IconButton(onClick = { /*TODO*/ }) {

    }
    StepperViewLayout(
        Modifier,
        stepsPerRow  = stepsPerRow,
        indicatorContent = {
            repeat(items.size) {
                StepperViewIndicator(
                    modifier = Modifier.layoutId(
                        LAYOUTID_STEPPERVIEW_INDICATOR.plus(it)
                    )
                )
            }
        },
        lineContent = {
            repeat(items.size - 1) {
                StepperViewLine(modifier = Modifier.layoutId(LAYOUTID_STEPPERVIEW_LINE.plus(it)))
            }
        },
        stepContent = {
            StepScopeInstance.content()
        }
    )

}

@Composable
fun Step(
    content: @Composable () -> Unit
) {
    content()
}

private val Measurable.stepAlignment
    get() = (parentData as? StepAlignmentData)?.stepAlignment ?: StepAlignment.RIGHT


@Composable
fun StepperViewLayout(
    modifier: Modifier = Modifier,
    stepsPerRow : StepsPerRow,
    indicatorContent: @Composable () -> Unit,
    lineContent: @Composable () -> Unit,
    stepContent: @Composable StepScope.() -> Unit
) {
    Layout(
        content = {
            indicatorContent()
            lineContent()
            StepScopeInstance.stepContent()
        },
        modifier = modifier
    ) { measurables, constraints ->

        val horizontalSpacing = 50 // Spacing between Indicator and Step.
        var verticalSpacing = 100 // Spacing between Steps.

        val stepsMeasurables = measurables
            .filter {
                it.layoutId.toString().contains(LAYOUTID_STEPPERVIEW_LINE)
                    .not() && it.layoutId.toString().contains(LAYOUTID_STEPPERVIEW_INDICATOR)
                    .not()
            }
        val alignments = ArrayList<StepAlignment>()

        var maxLeftStepWidth = 0
        var maxRightStepWidth = 0
        var indicatorXPosition = 0
        val stepsPlaceables = stepsMeasurables
            .map {
              val  placeable = it.measure(constraints = constraints)
              alignments.add(it.stepAlignment)
              if(it.stepAlignment == StepAlignment.LEFT) {
                  maxLeftStepWidth = Math.max(maxLeftStepWidth, placeable.width)
              }else{
                  maxRightStepWidth = Math.max(maxRightStepWidth, placeable.width)
              }
              placeable
            }
        if(maxLeftStepWidth != 0){
            indicatorXPosition = maxLeftStepWidth + horizontalSpacing
        }


        val stepIndicatorPlaceables = measurables
            .filter { it.layoutId.toString().contains(LAYOUTID_STEPPERVIEW_INDICATOR) }
            .map { it.measure(constraints = constraints) }

        val stepLinePlaceables = measurables
            .filter { it.layoutId.toString().contains(LAYOUTID_STEPPERVIEW_LINE) }
            .mapIndexed { index, measurable ->
                val lineHeight =
                    (stepsPlaceables[index/stepsPerRow.noOfSteps].height / 2 + stepsPlaceables[(index/stepsPerRow.noOfSteps) + stepsPerRow.noOfSteps].height / 2).plus(
                        verticalSpacing
                    ).minus(stepIndicatorPlaceables[index].height)
                measurable.measure(
                    constraints = constraints.copy(
                        minHeight = lineHeight,
                        maxHeight = lineHeight
                    )
                )
            }

        val maxWidthIndicatorAndLine =
            stepIndicatorPlaceables.plus(stepLinePlaceables).maxOf { it.width }

        val leftStepXPosition = 0
        val rightStepXPosition = maxLeftStepWidth + maxWidthIndicatorAndLine + horizontalSpacing

        layout(constraints.maxWidth, constraints.maxHeight) {
            val noOfSteps = stepsPlaceables.size
            var y = 0

            // Either Step or Indicator can be bigger. So based on the size of step and height. Initial Y value is set.
            if (stepIndicatorPlaceables[0].height > stepsPlaceables[0].height) {
                y = (stepIndicatorPlaceables[0].height / 2) - (stepsPlaceables[0].height / 2)
                verticalSpacing += stepIndicatorPlaceables[0].height
            }
            for (i in 0 until noOfSteps step stepsPerRow.noOfSteps) {
                val stepPlaceable = stepsPlaceables[i]
                val indicatorPlaceable = stepIndicatorPlaceables[i/stepsPerRow.noOfSteps]
                var linePlaceable: Placeable? = null
                var linePlaceableWidth = 0

                // no of lines is one less than the no of steps.
                if ((i/stepsPerRow.noOfSteps) <= stepLinePlaceables.size - 1) {
                    linePlaceable = stepLinePlaceables[i/stepsPerRow.noOfSteps]
                    linePlaceableWidth = linePlaceable.width
                }


                // Placing Steps
                for( j in i until i+stepsPerRow.noOfSteps) {

                    var x =
                        max(indicatorPlaceable.width, linePlaceableWidth).plus(horizontalSpacing)
                    when (alignments[j]) {
                        StepAlignment.RIGHT -> x = rightStepXPosition
                        StepAlignment.LEFT -> x = leftStepXPosition
                    }
                    stepsPlaceables[j].placeRelative(x = x, y = y)
                }

                y += stepPlaceable.height

                // Placing Indicators
                val indicatorAlignment: StepIndicatorAlignment = StepIndicatorAlignment.CENTER
                var indicatorY = 0

                when (indicatorAlignment) {
                    StepIndicatorAlignment.CENTER -> indicatorY =
                        (y - (stepPlaceable.height / 2)) - (indicatorPlaceable.height / 2)   // depends on how indicator has to be shown centre, top, bottom . For now center
                    StepIndicatorAlignment.TOP -> indicatorY =
                        y - (indicatorPlaceable.height / 2)   // depends on how indicator has to be shown centre, top, bottom . For now center
                    StepIndicatorAlignment.BOTTOM -> indicatorY =
                        y - (stepPlaceable.height - (indicatorPlaceable.height / 2))  // depends on how indicator has to be shown centre, top, bottom . For now center
                }

                indicatorPlaceable.placeRelative(x = indicatorXPosition , y = indicatorY)
                val indicatorXMid = indicatorXPosition + indicatorPlaceable.width / 2

                // Placing Line
                val lineY = indicatorY + indicatorPlaceable.height
                linePlaceable?.placeRelative(
                    x = indicatorXMid - linePlaceableWidth / 2,
                    y = lineY
                )

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

enum class StepAlignment {
    LEFT,
    RIGHT
}

class StepAlignmentData(val stepAlignment: StepAlignment) : ParentDataModifier {

    override fun Density.modifyParentData(parentData: Any?): Any? {
        return this@StepAlignmentData
    }
}

interface StepScope {
    fun Modifier.align(stepAlignment: StepAlignment) : Modifier
}

internal object StepScopeInstance : StepScope {

    override fun Modifier.align(stepAlignment: StepAlignment) : Modifier = this.then(
        StepAlignmentData(stepAlignment)
    )
}

enum class StepIndicatorAlignment {
    TOP,
    BOTTOM,
    CENTER
}

class StepIndicatorAlignmentData(val stepIndicatorAlignment: StepIndicatorAlignment) :
    ParentDataModifier {

    override fun Density.modifyParentData(parentData: Any?): Any? {
        return this@StepIndicatorAlignmentData
    }
}

interface StepIndicatorScope {

    fun Modifier.align(stepAlignment: StepAlignment) {
        StepAlignmentData(stepAlignment)
    }

    companion object StepIndicatorScope
}

