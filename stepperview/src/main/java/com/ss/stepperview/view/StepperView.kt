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
import com.ss.stepperview.layoutmodifier.StepIndicatorAlignment
import com.ss.stepperview.layoutmodifier.StepIndicatorScopeInstance.align
import com.ss.stepperview.layoutmodifier.StepScope
import com.ss.stepperview.layoutmodifier.StepScopeInstance
import kotlin.math.max

private const val LAYOUTID_STEPPERVIEW_INDICATOR = "LAYOUTID_STEPPERVIEW_INDICATOR"
private const val LAYOUTID_STEPPERVIEW_LINE = "LAYOUTID_STEPPERVIEW_LINE"

enum class StepsPerRow(val noOfSteps: Int) {
    ONE(1), TWO(2)
}

@Composable
fun StepperView(
    items: List<Any>,
    stepsPerRow: StepsPerRow = StepsPerRow.ONE,
    content: @Composable StepScope.() -> Unit
) {
    StepperViewLayout(
        Modifier,
        stepsPerRow = stepsPerRow,
        indicatorContent = {
            repeat(items.size) {
                StepperViewIndicator(
                    modifier = Modifier
                        .layoutId(
                            LAYOUTID_STEPPERVIEW_INDICATOR.plus(it)
                        )
                        .align(StepIndicatorAlignment.TOP),
                    size = 25.dp,
                    border = BorderStroke(1.dp, Color.Blue),
                    color = Color.Yellow,
                )
            }
        },
        lineContent = {
            repeat(items.size - 1) {
                StepperViewLine(modifier = Modifier.layoutId(LAYOUTID_STEPPERVIEW_LINE.plus(it)),
                color = Color.Blue)
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

        val stepsMeasurables = measurables
            .filter {
                it.layoutId.toString().contains(LAYOUTID_STEPPERVIEW_LINE)
                    .not() && it.layoutId.toString().contains(LAYOUTID_STEPPERVIEW_INDICATOR)
                    .not()
            }
        val indicatorMeasurables = measurables
            .filter { it.layoutId.toString().contains(LAYOUTID_STEPPERVIEW_INDICATOR) }
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