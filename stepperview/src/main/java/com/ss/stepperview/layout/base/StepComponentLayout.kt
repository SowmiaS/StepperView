package com.ss.stepperview.layout.base

import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.unit.Constraints
import com.ss.stepperview.layoutmodifier.StepIndicatorAlignment
import com.ss.stepperview.layoutmodifier.StepVerticalAlignment
import com.ss.stepperview.layoutmodifier.stepAlignment
import com.ss.stepperview.layoutmodifier.stepIndicatorAlignment

interface StepComponentLayout {
    val intrinsicWidth: Int
    val intrinsicHeight: Int
    var xPosition: Int
    var yPosition: Int
    var width: Int
    var height:Int
    var placeable: Placeable?
    fun measure(constraints: Constraints)
    val place: Placeable.PlacementScope.(x: Int, y: Int) -> Unit
}

internal class BaseStepComponentLayout(val measurable: Measurable) : StepComponentLayout {
    override val intrinsicWidth: Int
        get() = measurable.maxIntrinsicWidth(Int.MAX_VALUE)
    override val intrinsicHeight: Int
        get() = measurable.maxIntrinsicHeight(Int.MAX_VALUE)
    override var placeable: Placeable? = null
    override var xPosition: Int = 0
    override var yPosition: Int = 0
    override var width: Int = 0
    override var height: Int = 0

    override fun measure(constraints: Constraints) {
        placeable = measurable.measure(constraints)
        width = placeable?.width ?: 0
        height = placeable?.height ?: 0
    }

    override val place: Placeable.PlacementScope.(x: Int, y: Int) -> Unit = { x: Int, y: Int ->
        xPosition = x
        yPosition = y
        placeable?.placeRelative(x = x, y = y)
    }
}

class StepLayout(private val measurable: Measurable,val stepVerticalAlignment: StepVerticalAlignment) :
    StepComponentLayout by BaseStepComponentLayout(measurable = measurable) {
    val stepHorizontalAlignment
        get() = measurable.stepAlignment
}

class StepLineLayout(private val measurable: Measurable) :
    StepComponentLayout by BaseStepComponentLayout(measurable)

class StepIndicatorLayout(private val measurable: Measurable) :
    StepComponentLayout by BaseStepComponentLayout(measurable = measurable) {
    val stepIndicatorAlignment: StepIndicatorAlignment
        get() = measurable.stepIndicatorAlignment
}
