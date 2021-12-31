package com.ss.stepperview.layout

import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.Placeable
import com.ss.stepperview.layout.base.BaseComponentList
import com.ss.stepperview.layout.base.StepIndicatorLayout
import com.ss.stepperview.layout.helpers.IndicatorMeasureAndPlaceHelpers

class StepIndicatorLayoutList(
    indicatorMeasurables: List<Measurable>,
    indicatorMeasureAndPlaceHelpers: IndicatorMeasureAndPlaceHelpers
) : BaseComponentList<StepIndicatorLayout>(indicatorMeasurables),
    IndicatorMeasureAndPlaceHelpers by indicatorMeasureAndPlaceHelpers {
    override fun instance(measurable: Measurable) = StepIndicatorLayout(measurable)

    val place: Placeable.PlacementScope.(x: Int, y: Int) -> Unit = { x: Int, y: Int ->
        var yPosition = y
        components.forEachIndexed { index, it ->
            yPosition += indicatorMeasureAndPlaceHelpers.verticalSpacing(
                it.height,
                index,
                it.stepIndicatorAlignment
            )
            it.place.invoke(this, x, yPosition)
        }
    }
}
