package com.ss.stepperview

import androidx.compose.ui.layout.Measurable
import org.junit.Test
import org.mockito.kotlin.whenever
import org.assertj.core.api.Assertions.*
import org.junit.Before
import org.mockito.Mockito
import org.mockito.kotlin.mock
import org.mockito.kotlin.stub

class IndicatorMeasureAndPlaceHelperTest {
    val helper = StepRowMeasureAndPlaceHelpersImpl()
    lateinit var stepRows : Rows

    @Before
    fun setup(){
    }

    @Test
    fun `first indicator should be aligned left to all left steps`(){
        val measurable = mock<Measurable>()
        whenever(measurable.maxIntrinsicWidth(Int.MAX_VALUE)).thenReturn(100)
        whenever(measurable.parentData).thenReturn(StepAlignmentData(StepAlignment.LEFT))
        val indicatorHeight = 30
        stepRows = Rows(listOf(measurable),StepsPerRow.ONE,helper)
        val row: StepRow = Mockito.mock(StepRow::class.java)
        stepRows.rows.add(row)
        whenever(row.height).thenReturn(100)
        val indicators = IndicatorMeasureAndPlaceHelpersImpl(stepRows)
        assertThat(indicators.firstIndicatorPosition(indicatorHeight, StepIndicatorAlignment.CENTER).x ).isEqualTo(100)
    }

    @Test
    fun `when indicator alignment is top, indicator should be aligned to top of steps of that row`(){

    }

    @Test
    fun `when indicator alignment is center, indicator should be aligned to center of steps of that row`(){
        val indicatorHeight = 30
        stepRows = Rows(mock(),StepsPerRow.ONE,helper)

        whenever(stepRows.maxLeftInstrinsicWidth).thenReturn(100)
        val row: StepRow = Mockito.mock(StepRow::class.java)
        stepRows.rows.add(row)
        whenever(row.height).thenReturn(100)
        val indicators = IndicatorMeasureAndPlaceHelpersImpl(stepRows)
        val resultYPosition = indicators.firstIndicatorPosition(indicatorHeight, StepIndicatorAlignment.CENTER).y
        assertThat(resultYPosition).isEqualTo(35)
    }

    @Test
    fun `when indicator alignment is bottom, indicator should be aligned to bottom of steps of that row`(){

    }
}

