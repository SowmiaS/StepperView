package com.ss.stepperview.layout.helpers

import com.ss.stepperview.data.Point
import com.ss.stepperview.layout.StepRowLayoutList
import com.ss.stepperview.layoutmodifier.StepIndicatorAlignment

interface IndicatorMeasureAndPlaceHelpers {
    fun verticalSpacing(
        indicatorHeight: Int,
        aboveIndicatorWithIndex: Int,
        alignment: StepIndicatorAlignment
    ): Int

    fun firstIndicatorPosition(firstIndicatorHeight: Int, alignment: StepIndicatorAlignment): Point
}
class IndicatorMeasureAndPlaceHelpersImpl(private val stepRows: StepRowLayoutList) : IndicatorMeasureAndPlaceHelpers {

    override fun firstIndicatorPosition(
        firstIndicatorHeight: Int,
        alignment: StepIndicatorAlignment
    ): Point {
        val yPosition = when (alignment) {
            StepIndicatorAlignment.CENTER -> stepRows.rows[0].height / 2 - firstIndicatorHeight / 2
            StepIndicatorAlignment.TOP -> 0
            StepIndicatorAlignment.BOTTOM -> stepRows.rows[0].height / 2 + firstIndicatorHeight
        }
        val xPosition = stepRows.maxLeftIntrinsicWidth
        return Point(xPosition, yPosition)
    }

    override fun verticalSpacing(
        indicatorHeight: Int,
        aboveIndicatorWithIndex: Int,
        alignment: StepIndicatorAlignment
    ): Int {
        return when (alignment) {
            StepIndicatorAlignment.TOP -> {
                if (aboveIndicatorWithIndex == 0)
                    0
                else
                    stepRows.rows[aboveIndicatorWithIndex - 1].height + stepRows.verticalSpacing(
                        belowStepRowWithIndex = aboveIndicatorWithIndex - 1
                    )
            }
            StepIndicatorAlignment.CENTER -> {
                if (aboveIndicatorWithIndex == 0)
                    0
                else
                    stepRows.rows[aboveIndicatorWithIndex - 1].height / 2 + stepRows.rows[aboveIndicatorWithIndex].height / 2 + stepRows.verticalSpacing(
                        belowStepRowWithIndex = aboveIndicatorWithIndex - 1
                    )

            }
            StepIndicatorAlignment.BOTTOM -> {
                if (aboveIndicatorWithIndex == 0)
                    0
                else
                    stepRows.rows[aboveIndicatorWithIndex].height + stepRows.verticalSpacing(
                        belowStepRowWithIndex = aboveIndicatorWithIndex - 1
                    )
            }
        }
    }
}
