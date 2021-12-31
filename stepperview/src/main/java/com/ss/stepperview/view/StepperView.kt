package com.ss.stepperview.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import com.ss.stepperview.layout.StepRowLayoutList
import com.ss.stepperview.layout.StepIndicatorLayoutList
import com.ss.stepperview.layout.StepLineLayoutList
import com.ss.stepperview.layout.helpers.DEFAULT_VERTICAL_SPACING
import com.ss.stepperview.layout.helpers.IndicatorMeasureAndPlaceHelpersImpl
import com.ss.stepperview.layout.helpers.LineMeasureAndPlaceHelpersImpl
import com.ss.stepperview.layout.helpers.StepRowMeasureAndPlaceHelpersImpl
import com.ss.stepperview.layoutmodifier.StepIndicatorAlignment
import com.ss.stepperview.layoutmodifier.StepIndicatorScopeInstance
import com.ss.stepperview.layoutmodifier.StepScope
import com.ss.stepperview.layoutmodifier.StepScopeInstance
import com.ss.stepperview.layoutmodifier.StepIndicatorScope
import com.ss.stepperview.layoutmodifier.StepIndicatorAlignmentData

import kotlin.math.max

const val LAYOUTID_STEPPERVIEW_INDICATOR = "LAYOUTID_STEPPERVIEW_INDICATOR"
const val LAYOUTID_STEPPERVIEW_LINE = "LAYOUTID_STEPPERVIEW_LINE"

enum class StepsPerRow(val noOfSteps: Int) {
    ONE(1), TWO(2)
}

@Composable
fun StepperView(
    items: List<Any>,
    stepsPerRow: StepsPerRow = StepsPerRow.ONE,
    verticalSpacing : Int = DEFAULT_VERTICAL_SPACING,
    indicator: @Composable StepIndicatorScope.() -> Unit = {
        StepperViewIndicator(modifier = Modifier
        .align(StepIndicatorAlignment.BOTTOM)) },
    content: @Composable StepScope.() -> Unit
) {
    StepperViewLayout(
        Modifier,
        verticalSpacing = verticalSpacing,
        stepsPerRow = stepsPerRow,
        indicatorContent = {
            repeat(items.size) {
                    StepIndicatorScopeInstance.indicator()
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
fun StepperViewLayout(
    modifier: Modifier = Modifier,
    stepsPerRow: StepsPerRow,
    verticalSpacing:Int,
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
                    .not() && it.parentData !is StepIndicatorAlignmentData
            }
        val indicatorMeasurables = measurables
            .filter { it.parentData is StepIndicatorAlignmentData }
        val lineMeasurables = measurables
            .filter { it.layoutId.toString().contains(LAYOUTID_STEPPERVIEW_LINE) }


        layout(constraints.maxWidth, constraints.maxHeight) {
            val stepRowsMeasureAndPlaceHelpersImpl = StepRowMeasureAndPlaceHelpersImpl(verticalSpacing)
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