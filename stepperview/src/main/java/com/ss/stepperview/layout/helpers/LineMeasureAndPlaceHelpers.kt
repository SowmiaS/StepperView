package com.ss.stepperview.layout.helpers

import com.ss.stepperview.data.Point
import com.ss.stepperview.layout.StepIndicatorLayoutList

interface LineMeasureAndPlaceHelpers {
    fun shouldLineOverlapIndicator(): Boolean
    fun verticalSpacing(forLineIndex: Int): Int
    fun lineHeight(forLineIndex: Int): Int
    fun firstLinePosition(firstLineWidth: Int): Point
}

class LineMeasureAndPlaceHelpersImpl(private val indicators: StepIndicatorLayoutList) : LineMeasureAndPlaceHelpers {
    override fun shouldLineOverlapIndicator(): Boolean = false

    override fun verticalSpacing(forLineIndex: Int): Int {
        return getOverlappingOffset(forLineIndex)
    }

    override fun lineHeight(forLineIndex: Int): Int {
        return indicators.components[forLineIndex + 1].yPosition - indicators.components[forLineIndex].yPosition - getOverlappingOffset(
            forLineIndex
        )
    }

    private fun getOverlappingOffset(index: Int): Int {
        return if (shouldLineOverlapIndicator()) 0 else indicators.components[index].height
    }

    override fun firstLinePosition(firstLineWidth: Int): Point {
        val xPosition =
            indicators.first.xPosition + (indicators.first.width - firstLineWidth) / 2
        val yPosition = indicators.first.yPosition + getOverlappingOffset(0)
        return Point(xPosition, yPosition)
    }
}
