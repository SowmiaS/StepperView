package com.ss.stepperview.layout.base

import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.unit.Constraints

abstract class BaseComponentList<T : StepComponentLayout>(measurables: List<Measurable>) {
    var components = arrayListOf<T>()

    abstract fun instance(measurable: Measurable): T

    init {
        measurables.forEach {
            components.add(instance(it))
        }
    }

    val first: T
        get() = components[0]

    val maxIntrinsicWidth : Int
        get(){
            return components.maxOfOrNull { it.intrinsicWidth } ?: 0
        }

    open fun measure(constraints: Constraints) {
        components.forEach { it.measure(constraints) }
    }
}
