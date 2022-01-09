package com.ss.stepperview.layoutmodifier

import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.ParentDataModifier
import androidx.compose.ui.unit.Density

enum class StepAlignment {
    LEFT,
    RIGHT
}

enum class StepVerticalAlignment{
    TOP,
    CENTER,
    BOTTOM
}

internal class StepAlignmentData(val stepAlignment: StepAlignment) : ParentDataModifier {
    override fun Density.modifyParentData(parentData: Any?): Any {
        return this@StepAlignmentData
    }
}

interface StepScope {
    fun Modifier.align(stepAlignment: StepAlignment): Modifier
}

internal object StepScopeInstance : StepScope {
    override fun Modifier.align(stepAlignment: StepAlignment): Modifier = this.then(
        StepAlignmentData(stepAlignment)
    )
}

internal val Measurable.stepAlignment
    get() = (parentData as? StepAlignmentData)?.stepAlignment ?: StepAlignment.RIGHT
