package com.ss.stepperview.layout

import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.unit.Constraints
import com.ss.stepperview.view.StepsPerRow
import com.ss.stepperview.layout.base.StepLayout
import com.ss.stepperview.layout.helpers.StepRowMeasureAndPlaceHelpers
import com.ss.stepperview.layoutmodifier.StepAlignment
import com.ss.stepperview.layoutmodifier.StepVerticalAlignment
import kotlin.math.max

class StepRowLayout(stepMeasurables: List<Measurable>, stepAlignment: StepVerticalAlignment) {

    var stepLayouts = arrayListOf<StepLayout>()

    init {
        stepMeasurables.forEach { stepLayouts.add(StepLayout(it, stepAlignment)) }
    }

    val height
        get() = stepLayouts.maxOfOrNull { it.intrinsicHeight } ?: 0

    fun measure(leftConstraints: Constraints, rightConstraints: Constraints) {
        stepLayouts.forEach {
            when (it.stepHorizontalAlignment) {
                StepAlignment.LEFT -> it.measure(leftConstraints)
                StepAlignment.RIGHT -> it.measure(rightConstraints)
            }
        }
    }

    val place: Placeable.PlacementScope.(x: Int, y: Int, x1: Int, y1: Int) -> Unit =
        { x: Int, y: Int, x1: Int, y1: Int ->
            stepLayouts.forEach {
                if (it.stepHorizontalAlignment == StepAlignment.LEFT) {
                    val yPosition = getYPosition(stepLayouts, it, y)
                    it.place.invoke(this, x, yPosition)
                } else {
                    val yPosition = getYPosition(stepLayouts, it, y1)
                    it.place.invoke(this, x1, yPosition)
                }
            }
        }

    private fun getYPosition(stepLayouts: List<StepLayout>, stepLayout: StepLayout, y: Int): Int {
        var yPosition = y
        if (stepLayouts.size == StepsPerRow.TWO.noOfSteps) {
            val biggerStep = stepLayouts.maxByOrNull { it.placeable?.height ?: 0 }
            val smallerStep = stepLayouts.minByOrNull { it.placeable?.height ?: 0 }
            if (smallerStep == stepLayout) {
                yPosition = when (smallerStep.stepVerticalAlignment) {
                    StepVerticalAlignment.TOP -> y
                    StepVerticalAlignment.CENTER -> y + (biggerStep?.height ?: 0) / 2 - (smallerStep.height) / 2
                    StepVerticalAlignment.BOTTOM -> y + (biggerStep?.height ?: 0) - (smallerStep.height)
                }
            }
        }
        return yPosition
    }
}

class StepRowLayoutList(
    stepMeasurables: List<Measurable>,
    stepsPerRow: StepsPerRow,
    stepRowMeasureAndPlaceHelpers: StepRowMeasureAndPlaceHelpers,
    stepVerticalAlignmentList: List<StepVerticalAlignment>
) : StepRowMeasureAndPlaceHelpers by stepRowMeasureAndPlaceHelpers {

    val rows = arrayListOf<StepRowLayout>()

    init {
        for (i in stepMeasurables.indices step stepsPerRow.noOfSteps) {
            rows.add(
                StepRowLayout(
                    stepMeasurables.toMutableList().subList(i, i + stepsPerRow.noOfSteps).toList(),
                    stepVerticalAlignmentList[i / stepsPerRow.noOfSteps]
                )
            )
        }
    }

    private val steps
        get() = rows.flatMap { it.stepLayouts }
    private val leftSteps
        get() = steps.filter { it.stepHorizontalAlignment == StepAlignment.LEFT }
    private val rightSteps
        get() = steps.filter { it.stepHorizontalAlignment == StepAlignment.RIGHT }
    val maxLeftIntrinsicWidth
        get() = leftSteps.maxOfOrNull { it.intrinsicWidth } ?: 0
    private val maxRightIntrinsicWidth
        get() = rightSteps.maxOfOrNull { it.intrinsicWidth } ?: 0

    private fun remainingLeftStepWidth(constraints: Constraints): Int {
        val maxWidthEqualConstraints = equalConstraints(constraints = constraints).maxWidth
        return if (maxLeftIntrinsicWidth >= maxWidthEqualConstraints) 0
        else maxWidthEqualConstraints - maxLeftIntrinsicWidth
    }

    private fun remainingRightStepWidth(constraints: Constraints): Int {
        val maxWidthEqualConstraints = equalConstraints(constraints = constraints).maxWidth
        return if (maxRightIntrinsicWidth >= maxWidthEqualConstraints) 0
        else maxWidthEqualConstraints - maxRightIntrinsicWidth
    }

    private val isBiDirectionalStepper: Boolean
        get() {
            val steps = rows.flatMap { it.stepLayouts }
            val leftSteps = steps.filter { it.stepHorizontalAlignment == StepAlignment.LEFT }
            val rightSteps = steps.filter { it.stepHorizontalAlignment == StepAlignment.RIGHT }
            return (leftSteps.isNotEmpty() && rightSteps.isNotEmpty())
        }

    fun measure(constraints: Constraints) {
        if (!isBiDirectionalStepper) {
            rows.forEach { it.measure(constraints, constraints) }
        } else {
            rows.forEach { it.measure(leftConstraints(constraints), rightConstraints(constraints)) }
        }
    }

    val place: Placeable.PlacementScope.(x: Int, y: Int, x1: Int, y1: Int) -> Unit =
        { x: Int, y: Int, x1: Int, y1: Int ->
            var yPosition = max(y, y1)
            rows.forEachIndexed { index, stepRow ->
                stepRow.place.invoke(this, x, yPosition, x1, yPosition)
                yPosition += stepRow.height + stepRowMeasureAndPlaceHelpers.verticalSpacing(
                    belowStepRowWithIndex = index
                )
            }
        }

    private fun equalConstraints(constraints: Constraints) = constraints.copy(
        maxWidth = constraints.maxWidth / 2
    )

    private fun leftConstraints(constraints: Constraints): Constraints {
        return if (maxLeftIntrinsicWidth >= equalConstraints(constraints = constraints).maxWidth)
            constraints
        else
            constraints.copy(
                maxWidth = equalConstraints(constraints = constraints).maxWidth + remainingRightStepWidth(
                    constraints
                )
            )
    }

    private fun rightConstraints(constraints: Constraints): Constraints {
        return if (maxRightIntrinsicWidth >= equalConstraints(constraints = constraints).maxWidth)
            constraints
        else
            constraints.copy(
                maxWidth = equalConstraints(constraints = constraints).maxWidth + remainingLeftStepWidth(
                    constraints
                )
            )
    }

}
