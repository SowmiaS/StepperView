package com.ss.stepperview

import android.graphics.Point
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.layout.*
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import com.ss.stepperview.StepIndicatorScopeInstance.align
import kotlin.math.max

private const val LAYOUTID_STEPPERVIEW_INDICATOR = "LAYOUTID_STEPPERVIEW_INDICATOR"
private const val LAYOUTID_STEPPERVIEW_LINE = "LAYOUTID_STEPPERVIEW_LINE"

enum class StepsPerRow(val noOfSteps: Int){
    ONE(1), TWO(2)
}
@Composable
fun StepperView(
    items: List<Any>,
    stepsPerRow : StepsPerRow = StepsPerRow.ONE,
    content: @Composable StepScope.() -> Unit
) {
    StepperViewLayout(
        Modifier,
        stepsPerRow  = stepsPerRow,
        indicatorContent = {
            repeat(items.size) {
                StepperViewIndicator(
                    modifier = Modifier
                        .layoutId(
                            LAYOUTID_STEPPERVIEW_INDICATOR.plus(it)
                        )
                        .align(StepIndicatorAlignment.TOP)
                )
            }
        },
        lineContent = {
            repeat(items.size - 1) {
                StepperViewLine(modifier = Modifier.layoutId(LAYOUTID_STEPPERVIEW_LINE.plus(it)))
            }
        },
        stepContent = {
            StepScopeInstance.content()
        }
    )

}

@Composable
fun Step(
    content: @Composable () -> Unit
) {
    content()
}

private val Measurable.stepAlignment
    get() = (parentData as? StepAlignmentData)?.stepAlignment ?: StepAlignment.RIGHT

private val Measurable.stepIndicatorAlignment
    get() = (parentData as? StepIndicatorAlignmentData)?.stepIndicatorAlignment ?: StepIndicatorAlignment.CENTER


@Composable
fun StepperViewLayout(
    modifier: Modifier = Modifier,
    stepsPerRow : StepsPerRow,
    indicatorContent: @Composable () -> Unit,
    lineContent: @Composable () -> Unit,
    stepContent: @Composable StepScope.() -> Unit
) {
    Layout(
        content = {
            indicatorContent()
            lineContent()
            StepScopeInstance.stepContent()
        },
        modifier = modifier
    ) { measurables, constraints ->

        val horizontalSpacing = 0 // Spacing between Indicator and Step.
        var verticalSpacing = 20 // Spacing between Steps.

        val stepsMeasurables = measurables
            .filter {
                it.layoutId.toString().contains(LAYOUTID_STEPPERVIEW_LINE)
                    .not() && it.layoutId.toString().contains(LAYOUTID_STEPPERVIEW_INDICATOR)
                    .not()
            }
        val indicatorMeasurables = measurables
            .filter { it.layoutId.toString().contains(LAYOUTID_STEPPERVIEW_INDICATOR) }
        val lineMeasurables = measurables
            .filter { it.layoutId.toString().contains(LAYOUTID_STEPPERVIEW_LINE) }


        layout(constraints.maxWidth, constraints.maxHeight) {

            val stepRows = Rows(stepsMeasurables, stepsPerRow)
            val indicators = StepIndicators(indicatorMeasurables)
            val lineMeasureAndPlaceHelpersImpl = LineMeasureAndPlaceHelpersImpl(indicators)
            val lines = StepLines(lineMeasurables, lineMeasureAndPlaceHelpersImpl)

            //TODO : constraintsManager

            stepRows.measure(
                constraints.copy(
                    maxWidth = constraints.maxWidth - Math.max(
                        indicators.maxIntrinsicWidth,
                        lines.maxIntrinsicWidth
                    )
                )
            )
            indicators.measure(constraints)
            stepRows.place.invoke(
                this,
                0,
                0,
                stepRows.maxLeftInstrinsicWidth + Math.max(
                    indicators.maxIntrinsicWidth,
                    lines.maxIntrinsicWidth
                ),
                0,
                verticalSpacing
            )
            val verticalSpacingForIndicator: (indicatorIndex: Int, alignment: StepIndicatorAlignment) -> Int =
                { index, alignment: StepIndicatorAlignment ->

                    when (alignment) {
                        StepIndicatorAlignment.TOP -> {
                            if (index == 0)
                                0
                            else
                                stepRows.rows[index - 1].height + verticalSpacing
                        }
                        StepIndicatorAlignment.CENTER -> {
                            if (index == 0)
                                stepRows.rows[index].height / 2
                            else
                                stepRows.rows[index - 1].height / 2 + stepRows.rows[index].height / 2 + verticalSpacing
                        }
                        StepIndicatorAlignment.BOTTOM -> {
                            if (index == 0)
                                stepRows.rows[index].height
                            else
                                stepRows.rows[index].height + verticalSpacing
                        }
                    }
                }
            indicators.place.invoke(
                this,
                stepRows.maxLeftInstrinsicWidth,
                -(indicators.indicators[0].placeable?.height ?:0 ) / 2  ,
                verticalSpacingForIndicator
            )


            lines.measure(constraints)
            val lineStartPoint = lineMeasureAndPlaceHelpersImpl.firstLinePosition(lines.firstLineWidth)
            lines.place.invoke(this, lineStartPoint.x, lineStartPoint.y)

        }

    }
}

// ---
interface LineMeasureAndPlaceHelpers {

    fun shouldLineOverlapIndicator() : Boolean
    fun verticalspacing( forLineIndex: Int ) : Int
    fun lineHeight( forLineIndex: Int ) : Int
    fun firstLinePosition(lineWidth: Int) : Point
}

class LineMeasureAndPlaceHelpersImpl(val indicators: StepIndicators) : LineMeasureAndPlaceHelpers{

    override fun shouldLineOverlapIndicator(): Boolean = false

    override fun verticalspacing(forLineIndex: Int): Int {
        return getOverlappingOffset(forLineIndex)
    }

    override fun lineHeight(forLineIndex: Int): Int {
        return indicators.indicators[forLineIndex + 1].yPosition - indicators.indicators[forLineIndex].yPosition - getOverlappingOffset(forLineIndex)
    }

    private fun getOverlappingOffset(index: Int) :Int {
        return if(shouldLineOverlapIndicator())  0 else indicators.getIndicatorHeight(index)
    }

    override fun firstLinePosition(firstLineWidth: Int): Point {
        val xPosition = indicators.indicators[0].xPosition + ( indicators.getIndicatorWidth(0) - firstLineWidth ) / 2
        val yPosition = indicators.indicators[0].yPosition + getOverlappingOffset(0)
        return Point(xPosition, yPosition)
    }
}
//---
@Composable
fun StepperViewIndicator(modifier: Modifier) {
    OutlinedButton(
        onClick = { /*TODO*/ },
        modifier = modifier.size(10.dp),
        shape = CircleShape,
        border = BorderStroke(1.dp, Color.Blue),
        contentPadding = PaddingValues(0.dp),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Blue)
    ) {
        //Icon(Icons.Default.Add, contentDescription = "StepperViewIndicator")
    }
}

@Composable
fun StepperViewLine(modifier: Modifier) {
    Box(
        modifier = modifier
            .width(1.dp)
            .background(Color.Blue)
    )
}

enum class StepAlignment {
    LEFT,
    RIGHT
}

class StepAlignmentData(val stepAlignment: StepAlignment) : ParentDataModifier {

    override fun Density.modifyParentData(parentData: Any?): Any? {
        return this@StepAlignmentData
    }
}

interface StepScope {
    fun Modifier.align(stepAlignment: StepAlignment) : Modifier
}

internal object StepScopeInstance : StepScope {

    override fun Modifier.align(stepAlignment: StepAlignment) : Modifier = this.then(
        StepAlignmentData(stepAlignment)
    )
}

enum class StepIndicatorAlignment {
    TOP,
    BOTTOM,
    CENTER
}

class StepIndicatorAlignmentData(val stepIndicatorAlignment: StepIndicatorAlignment) :
    ParentDataModifier {

    override fun Density.modifyParentData(parentData: Any?): Any? {
        return this@StepIndicatorAlignmentData
    }
}

interface StepIndicatorScope {
    fun Modifier.align(stepIndicatorAlignment: StepIndicatorAlignment) : Modifier
}

internal object StepIndicatorScopeInstance : StepIndicatorScope {
    override fun Modifier.align(stepIndicatorAlignment: StepIndicatorAlignment) : Modifier = this.then(
        StepIndicatorAlignmentData(stepIndicatorAlignment)
    )
}



// ---- ----

interface StepComponentLayout{
    val intrinsicWidth : Int
    val intrinsicHeight : Int
    fun measure(constraints: Constraints)
    var placeable : Placeable?
    val place : Placeable.PlacementScope.(x :Int, y :Int) -> Unit
}

class BaseStepComponentLayout(val measurable: Measurable) : StepComponentLayout{
    override val intrinsicWidth: Int
        get() = measurable.maxIntrinsicWidth(Int.MAX_VALUE)
    override val intrinsicHeight: Int
        get() = measurable.maxIntrinsicHeight(Int.MAX_VALUE)
    override var placeable: Placeable? = null
    override fun measure(constraints: Constraints) {
        placeable = measurable.measure(constraints)
    }
    override val place : Placeable.PlacementScope.(x :Int, y :Int) -> Unit = { x : Int, y : Int ->
        placeable?.placeRelative(x = x, y = y)
    }
}

class Step(val measurable: Measurable) : StepComponentLayout by BaseStepComponentLayout(measurable = measurable) {
    // create new class for step alignment related code
    val stepAlignment
        get() = (measurable.parentData as? StepAlignmentData)?.stepAlignment ?: StepAlignment.RIGHT
}

class StepLine(val measurable: Measurable) : StepComponentLayout by BaseStepComponentLayout(measurable){

}

class StepIndicator(val measurable: Measurable) : StepComponentLayout by BaseStepComponentLayout(measurable = measurable) {
    //TODO: create separate class for step Indicator alignment
    var alignment : StepIndicatorAlignment? = null

    var xPosition = 0
    var yPosition = 0

    override val place : Placeable.PlacementScope.(x : Int, y : Int) -> Unit = { x : Int, y : Int ->
        xPosition = x
        yPosition = y
        placeable?.placeRelative(x,y)
    }
}



class StepIndicators(val indicatorMeasurables: List<Measurable>){
    var indicators = arrayListOf<StepIndicator>()

    init {
        indicatorMeasurables.forEach {
            indicators.add(StepIndicator(it))
        }
    }

    val maxIntrinsicWidth : Int
        get(){
            return indicatorMeasurables.maxOfOrNull { it.maxIntrinsicWidth(Int.MAX_VALUE) } ?: 0
        }

    fun getIndicatorHeight(index: Int) :Int = indicators[index].placeable?.height ?: 0

    fun getIndicatorWidth(index: Int) :Int = indicators[index].placeable?.width ?: 0

    val firstIndicatorPosition : Point = Point( indicators[0].xPosition , indicators[0].yPosition)

    fun measure(constraints: Constraints){
        indicators.forEach {
            it.measure(constraints)
        }
    }

    val place : Placeable.PlacementScope.(x : Int, y : Int, gap: ( index:Int, alignment : StepIndicatorAlignment ) -> Int ) -> Unit = { x : Int, y : Int, gap: (index : Int, alignment : StepIndicatorAlignment) -> Int ->
        var yPosition = 0

        when(indicatorMeasurables[0].stepIndicatorAlignment){
            StepIndicatorAlignment.CENTER -> yPosition = y
            StepIndicatorAlignment.TOP -> yPosition = 0
            StepIndicatorAlignment.BOTTOM -> yPosition = -( y * 2 )
        }
        indicators.forEachIndexed { index, it ->
            yPosition += gap.invoke(index, it.alignment ?: StepIndicatorAlignment.CENTER)
            it.place.invoke(this, x, yPosition)
        }
    }
}



open abstract class BaseComponentList<T : StepComponentLayout>(measurables: List<Measurable>){

    var components = arrayListOf<T>()

    abstract fun instance(measurable: Measurable) : T

    init {
        measurables.forEach {
            components.add(instance(it))
        }
    }
    val maxIntrinsicWidth : Int
        get(){
            return components.maxOfOrNull { it.intrinsicWidth } ?: 0
        }
}

class StepLines(lineMeasurables: List<Measurable>, private val lineMeasureAndPlaceHelpers: LineMeasureAndPlaceHelpers) : BaseComponentList<StepLine>(lineMeasurables){

    override fun instance(measurable: Measurable) = StepLine(measurable)

    val firstLineWidth
            get() = components[0].placeable?.width ?: 0

    fun measure(constraints: Constraints){
        components.forEachIndexed { index, it ->
            it.measure(constraints = constraints.copy(
                minHeight = lineMeasureAndPlaceHelpers.lineHeight(index),
                maxHeight = lineMeasureAndPlaceHelpers.lineHeight(index)
            ))
        }
    }

    val place : Placeable.PlacementScope.( x :Int, y :Int) -> Unit = { x , y ->
        var yPosition = y
        components.forEachIndexed { index, it ->
            it.place.invoke(this, x, yPosition)
            yPosition += (it.placeable?.height ?: 0) + lineMeasureAndPlaceHelpers.verticalspacing(index)
        }
    }
}

class StepRow(stepMeasurables: List<Measurable>, stepsPerRow: StepsPerRow = StepsPerRow.ONE) {

    var steps = arrayListOf<Step>()

    init {
        stepMeasurables.forEach { steps.add(Step(it)) }
    }

    var height = 0
        get() = steps.maxOfOrNull { it.placeable?.height ?: 0 } ?: 0

    val maxIntrinsicWidth = steps.maxOfOrNull { it.intrinsicWidth } ?: 0
    val maxIntrinsicHeight = steps.maxOfOrNull { it.intrinsicHeight } ?: 0

    fun measure(leftConstraints: Constraints, rightConstraints: Constraints){
        steps.forEach {
            when(it.stepAlignment){
                StepAlignment.LEFT -> it.measure(leftConstraints)
                StepAlignment.RIGHT -> it.measure(rightConstraints)
            }
        }
    }

    val place : Placeable.PlacementScope.(x: Int, y:Int , x1: Int, y1:Int) -> Unit = { x: Int, y:Int , x1: Int, y1:Int ->
        steps.forEach {
            if( it.stepAlignment == StepAlignment.LEFT){
                it.place.invoke(this, x, y)
            }else{
                it.place.invoke(this, x1, y1)
            }

        }
    }
}

class Rows(stepMeasurables : List<Measurable>, val stepsPerRow: StepsPerRow){

    val rows = arrayListOf<StepRow>()

    init {
        for (i in stepMeasurables.indices step stepsPerRow.noOfSteps) {
            rows.add(
                StepRow(
                    stepMeasurables.toMutableList().subList(i, i + stepsPerRow.noOfSteps)
                        .toList(),
                    stepsPerRow = stepsPerRow
                )
            )
        }
    }

    val steps
        get() = rows.flatMap { it.steps }
    val leftSteps
        get() = steps.filter { it.stepAlignment == StepAlignment.LEFT }
    val rightSteps
        get() = steps.filter { it.stepAlignment == StepAlignment.RIGHT }

    val maxLeftInstrinsicWidth
        get() = leftSteps.maxOfOrNull { it.intrinsicWidth } ?: 0

    val maxRightInstrinsicWidth
        get() = rightSteps.maxOfOrNull { it.intrinsicWidth } ?: 0

    fun remainingLeftStepWidth(constraints: Constraints) : Int{
        val maxWidthEqualConstraints = equalConstraints(constraints = constraints).maxWidth
        if(maxLeftInstrinsicWidth >= maxWidthEqualConstraints) return 0
        else return maxWidthEqualConstraints - maxLeftInstrinsicWidth
    }

    fun remainingRightStepWidth(constraints: Constraints) : Int{
        val maxWidthEqualConstraints = equalConstraints(constraints = constraints).maxWidth
        if(maxRightInstrinsicWidth >= maxWidthEqualConstraints) return 0
        else return maxWidthEqualConstraints - maxRightInstrinsicWidth
    }


    var isBiDirectionalStepper : Boolean = false
        get(){
            val steps = rows.flatMap { it.steps }
            val leftSteps = steps.filter { it.stepAlignment == StepAlignment.LEFT }
            val rightSteps = steps.filter { it.stepAlignment == StepAlignment.RIGHT }
            return (leftSteps.isNotEmpty() && rightSteps.isNotEmpty())
        }

    fun measure(constraints: Constraints){
        if(!isBiDirectionalStepper){
            rows.forEach { it.measure(constraints, constraints) }
        }else{
            rows.forEach { it.measure(leftConstraints(constraints), rightConstraints(constraints)) }
        }
    }

    val place : Placeable.PlacementScope.(x : Int, y : Int, x1 : Int, y1 : Int, verticalSpacing: Int) -> Unit = { x : Int, y : Int, x1 : Int, y1 : Int, verticalSpacing: Int ->
        var yPosition = Math.max(y,y1)
        rows.forEach {
            it.place.invoke(this, x, yPosition, x1 , yPosition)
            yPosition = yPosition + it.height + verticalSpacing
        }
    }

    private fun equalConstraints(constraints: Constraints) = constraints.copy(
        maxWidth = constraints.maxWidth / 2
    )

    private fun leftConstraints(constraints: Constraints) : Constraints{
        if( maxLeftInstrinsicWidth >= equalConstraints(constraints = constraints).maxWidth)
            return constraints
        else
            return constraints.copy(
                maxWidth = equalConstraints(constraints = constraints).maxWidth + remainingRightStepWidth(constraints)
            )
        return constraints
    }

    private fun rightConstraints(constraints: Constraints) : Constraints{
        if( maxRightInstrinsicWidth >= equalConstraints(constraints = constraints).maxWidth)
            return constraints
        else
            return constraints.copy(
                maxWidth = equalConstraints(constraints = constraints).maxWidth + remainingLeftStepWidth(constraints)
            )
        return constraints
    }

}

