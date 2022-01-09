package com.ss.stepperview.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.unit.dp
import com.ss.stepperview.layout.StepIndicatorLayoutList
import com.ss.stepperview.layout.StepLineLayoutList
import com.ss.stepperview.layout.StepRowLayoutList
import com.ss.stepperview.layout.helpers.IndicatorMeasureAndPlaceHelpersImpl
import com.ss.stepperview.layout.helpers.LineMeasureAndPlaceHelpersImpl
import com.ss.stepperview.layout.helpers.StepRowMeasureAndPlaceHelpersImpl
import com.ss.stepperview.layoutmodifier.*
import com.ss.stepperview.layoutmodifier.StepIndicatorScopeInstance
import com.ss.stepperview.layoutmodifier.StepScopeInstance
import kotlin.math.max

internal const val LAYOUTID_STEPPERVIEW_INDICATOR = "LAYOUTID_STEPPERVIEW_INDICATOR"
internal const val LAYOUTID_STEPPERVIEW_LINE = "LAYOUTID_STEPPERVIEW_LINE"

enum class StepsPerRow(val noOfSteps: Int) {
    ONE(1), TWO(2)
}
@Composable
fun DefaultStepIndicator(): @Composable() (StepIndicatorScope.() -> Unit) =  {
    StepperViewIndicator(
        modifier = Modifier
            .align(StepIndicatorAlignment.TOP),
        size = 25.dp,
        border = BorderStroke(1.dp, Color.Blue),
        color = Color.Yellow)
}

@Composable
fun DefaultStepLine(): @Composable () -> Unit =  {
    StepperViewLine(color = Color.Blue)
}


@Composable
fun StepperView(
    items: List<Any>,
    stepsPerRow: StepsPerRow = StepsPerRow.ONE,
    indicatorContent: @Composable StepIndicatorScope.() -> Unit = DefaultStepIndicator(),
    lineContent: @Composable () -> Unit = DefaultStepLine(),
    content: @Composable StepScope.() -> Unit
) {
    StepperViewLayout(
        Modifier,
        stepsPerRow = stepsPerRow,
        indicatorContent = {
            repeat(items.size) {
                StepIndicatorScopeInstance.indicatorContent()
            }
        },
        lineContent = {
            repeat(items.size - 1) {
                lineContent()
            }
        },
        stepContent = {
            StepScopeInstance.content()
        }
    )
}

@Composable
fun StepperViewLayout(
    modifier: Modifier = Modifier,
    stepsPerRow: StepsPerRow,
    indicatorContent: @Composable StepIndicatorScope.() -> Unit,
    lineContent: @Composable () -> Unit,
    stepContent: @Composable StepScope.() -> Unit
) {
    Layout(
        content = {
            StepIndicatorScopeInstance.indicatorContent()
            lineContent()
            StepScopeInstance.stepContent()
        },
        modifier = modifier
    ) { measurables, constraints ->

        val stepsMeasurables = measurables
            .filter {
                it.layoutId.toString().contains(LAYOUTID_STEPPERVIEW_LINE)
                    .not() && (it.parentData is StepIndicatorAlignmentData)
                    .not()
            }
        val indicatorMeasurables = measurables
            .filter { it.parentData is StepIndicatorAlignmentData }
        val lineMeasurables = measurables
            .filter { it.layoutId.toString().contains(LAYOUTID_STEPPERVIEW_LINE) }


        layout(constraints.maxWidth, constraints.maxHeight) {
            val stepRowsMeasureAndPlaceHelpersImpl = StepRowMeasureAndPlaceHelpersImpl()
            val stepRowsLayout = StepRowLayoutList(stepsMeasurables, stepsPerRow, stepRowsMeasureAndPlaceHelpersImpl)
            val indicatorMeasureAndPlaceHelpersImpl = IndicatorMeasureAndPlaceHelpersImpl(stepRowsLayout)
            val indicatorsLayout =
                StepIndicatorLayoutList(indicatorMeasurables, indicatorMeasureAndPlaceHelpersImpl)
            val lineMeasureAndPlaceHelpersImpl = LineMeasureAndPlaceHelpersImpl(indicatorsLayout)
            val linesLayout = StepLineLayoutList(lineMeasurables, lineMeasureAndPlaceHelpersImpl)

            stepRowsLayout.measure(
                constraints.copy(
                    maxWidth = constraints.maxWidth - max(
                        indicatorsLayout.maxIntrinsicWidth,
                        linesLayout.maxIntrinsicWidth
                    )
                )
            )
            indicatorsLayout.measure(constraints)

            stepRowsLayout.place.invoke(
                this,
                0,
                0,
                stepRowsLayout.maxLeftIntrinsicWidth + max(
                    indicatorsLayout.maxIntrinsicWidth,
                    linesLayout.maxIntrinsicWidth
                ),
                0
            )
            val indicatorStartPoint = indicatorMeasureAndPlaceHelpersImpl.firstIndicatorPosition(
                indicatorsLayout.first.height,
                indicatorsLayout.first.stepIndicatorAlignment
            )
            indicatorsLayout.place.invoke(this, indicatorStartPoint.x, indicatorStartPoint.y)

            linesLayout.measure(constraints)
            val lineStartPoint =
                lineMeasureAndPlaceHelpersImpl.firstLinePosition(linesLayout.first.width)
            linesLayout.place.invoke(this, lineStartPoint.x, lineStartPoint.y)
        }
    }
}