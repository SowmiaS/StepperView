package com.ss.stepperview.layout.helpers

private const val VERTICAL_SPACING = 20

interface StepRowMeasureAndPlaceHelpers {
    fun verticalSpacing(belowStepRowWithIndex: Int): Int
}

class StepRowMeasureAndPlaceHelpersImpl : StepRowMeasureAndPlaceHelpers {
    override fun verticalSpacing(belowStepRowWithIndex: Int): Int = VERTICAL_SPACING
}