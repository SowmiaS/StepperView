package com.ss.stepperview.layout

import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.unit.Constraints
import com.ss.stepperview.layout.base.BaseComponentList
import com.ss.stepperview.layout.base.StepLineLayout
import com.ss.stepperview.layout.helpers.LineMeasureAndPlaceHelpers

class StepLineLayoutList(
    lineMeasurables: List<Measurable>,
    private val lineMeasureAndPlaceHelpers: LineMeasureAndPlaceHelpers
) : BaseComponentList<StepLineLayout>(lineMeasurables) ,
    LineMeasureAndPlaceHelpers by lineMeasureAndPlaceHelpers{

    override fun instance(measurable: Measurable) = StepLineLayout(measurable)

    override fun measure(constraints: Constraints) {
        components.forEachIndexed { index, it ->
            it.measure(
                constraints = constraints.copy(
                    minHeight = lineMeasureAndPlaceHelpers.lineHeight(index),
                    maxHeight = lineMeasureAndPlaceHelpers.lineHeight(index)
                )
            )
        }
    }

    val place: Placeable.PlacementScope.(x: Int, y: Int) -> Unit = { x, y ->
        var yPosition = y
        components.forEachIndexed { index, it ->
            it.place.invoke(this, x, yPosition)
            yPosition += it.height.plus(lineMeasureAndPlaceHelpers.verticalSpacing(index))
        }
    }
}
