package com.ss.stepperview.layout.helpers

internal const val DEFAULT_VERTICAL_SPACING = 20

interface StepRowMeasureAndPlaceHelpers {
    fun verticalSpacing(belowStepRowWithIndex: Int): Int
}

class StepRowMeasureAndPlaceHelpersImpl(val verticalSpacing: Int) : StepRowMeasureAndPlaceHelpers {
    override fun verticalSpacing(belowStepRowWithIndex: Int): Int = verticalSpacing
}