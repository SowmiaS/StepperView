package com.ss.stepperview.layoutmodifier

import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.ParentDataModifier
import androidx.compose.ui.unit.Density

enum class StepIndicatorAlignment {
    TOP,
    BOTTOM,
    CENTER
}

internal class StepIndicatorAlignmentData(val stepIndicatorAlignment: StepIndicatorAlignment) :
    ParentDataModifier {
    override fun Density.modifyParentData(parentData: Any?): Any {
        return this@StepIndicatorAlignmentData
    }
}

interface StepIndicatorScope {
    fun Modifier.align(stepIndicatorAlignment: StepIndicatorAlignment): Modifier
}

internal object StepIndicatorScopeInstance : StepIndicatorScope {
    override fun Modifier.align(stepIndicatorAlignment: StepIndicatorAlignment): Modifier =
        this.then(
            StepIndicatorAlignmentData(stepIndicatorAlignment)
        )
}

internal val Measurable.stepIndicatorAlignment
    get() = (parentData as? StepIndicatorAlignmentData)?.stepIndicatorAlignment
        ?: StepIndicatorAlignment.CENTER
