package com.ss.stepperview

import androidx.compose.ui.layout.Measurable
import org.junit.Test
import org.mockito.kotlin.whenever
import org.assertj.core.api.Assertions.*
import org.mockito.Mockito

class IndicatorMeasureAndPlaceHelperTest {
    val listofMeasurable = listOf<Measurable>()
    val helper = StepRowMeasureAndPlaceHelpersImpl()
    val stepRows = Rows(listofMeasurable,StepsPerRow.ONE,helper)
    @Test
    fun `first indicator should be aligned left to all left steps`(){
        val indicatorHeight = 30
        whenever(stepRows.maxLeftInstrinsicWidth).thenReturn(100)
        val row: StepRow = Mockito.mock(StepRow::class.java)
        whenever(row.height).thenReturn(100)
        stepRows.rows.add(row)
        val indicators = IndicatorMeasureAndPlaceHelpersImpl(stepRows)
        assertThat(indicators.firstIndicatorPosition(indicatorHeight, StepIndicatorAlignment.CENTER).x ).isEqualTo(100)
    }

    @Test
    fun `when indicator alignment is top, indicator should be aligned to top of steps of that row`(){

    }

    @Test
    fun `when indicator alignment is center, indicator should be aligned to center of steps of that row`(){
        val indicatorHeight = 30
        val row: StepRow = Mockito.mock(StepRow::class.java)
        whenever(row.height).thenReturn(100)
        stepRows.rows.add(row)
        val indicators = IndicatorMeasureAndPlaceHelpersImpl(stepRows)
        val resultYPosition = indicators.firstIndicatorPosition(indicatorHeight, StepIndicatorAlignment.CENTER).y
        assertThat(resultYPosition).isEqualTo(35)
    }

    @Test
    fun `when indicator alignment is bottom, indicator should be aligned to bottom of steps of that row`(){

    }
}

