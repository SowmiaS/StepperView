package com.ss.stepperview

import com.ss.stepperview.layout.StepRowLayout
import com.ss.stepperview.layout.StepRowLayoutList
import com.ss.stepperview.layout.helpers.IndicatorMeasureAndPlaceHelpersImpl
import com.ss.stepperview.layoutmodifier.StepIndicatorAlignment
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class IndicatorMeasureAndPlaceHelperTest {

    private lateinit var stepRows: StepRowLayoutList
    private val indicatorHeight by lazy { 30 }

    @Before
    fun setup() {
        stepRows = mock()
        val rows : ArrayList<StepRowLayout> = mock()
        whenever(stepRows.rows).thenReturn(rows)
        whenever(stepRows.maxLeftIntrinsicWidth).thenReturn(300)
        whenever(stepRows.verticalSpacing(Mockito.anyInt())).thenReturn(20)
        (0..4).forEach {
            val stepRowLayout = mock<StepRowLayout>()
            whenever(stepRowLayout.height).thenReturn(150 * (it+1) )
            whenever(rows[it]).thenReturn(stepRowLayout)
        }
    }

    @Test
    fun `first indicator should be aligned left to all left steps`(){
        val indicators = IndicatorMeasureAndPlaceHelpersImpl(stepRows)
        assertThat(indicators.firstIndicatorPosition(indicatorHeight, StepIndicatorAlignment.CENTER).x ).isEqualTo(300)
        assertThat(indicators.firstIndicatorPosition(indicatorHeight, StepIndicatorAlignment.TOP).x ).isEqualTo(300)
        assertThat(indicators.firstIndicatorPosition(indicatorHeight, StepIndicatorAlignment.BOTTOM).x ).isEqualTo(300)
    }

    @Test
    fun `when indicator alignment is top, first indicator should be aligned to top of steps of that row`(){
        val indicatorHeight = 30
        val indicators = IndicatorMeasureAndPlaceHelpersImpl(stepRows)
        val resultYPosition = indicators.firstIndicatorPosition(indicatorHeight, StepIndicatorAlignment.TOP).y
        assertThat(resultYPosition).isEqualTo(0)
    }

    @Test
    fun `when indicator alignment is center, first indicator should be aligned to center of steps of that row`(){
        val indicatorHeight = 30
        val indicators = IndicatorMeasureAndPlaceHelpersImpl(stepRows)
        val resultYPosition = indicators.firstIndicatorPosition(indicatorHeight, StepIndicatorAlignment.CENTER).y
        assertThat(resultYPosition).isEqualTo(60)
    }

    @Test
    fun `when indicator alignment is bottom, indicator should be aligned to bottom of steps of that row`(){
        val indicatorHeight = 30
        val indicators = IndicatorMeasureAndPlaceHelpersImpl(stepRows)
        val resultYPosition = indicators.firstIndicatorPosition(indicatorHeight, StepIndicatorAlignment.BOTTOM).y
        assertThat(resultYPosition).isEqualTo(120)
    }

    // As first Indicator position is used for first position.
    @Test
    fun `when indicator alignment is Top or Center or Bottom, vertical spacing of the indicator for index 0 should be always Zero`(){
        val indicatorHeight = 30
        val indicators = IndicatorMeasureAndPlaceHelpersImpl(stepRows)

        assertThat(indicators.verticalSpacing(indicatorHeight, 0, StepIndicatorAlignment.TOP)).isEqualTo(0)
        assertThat(indicators.verticalSpacing(indicatorHeight, 0, StepIndicatorAlignment.CENTER)).isEqualTo(0)
        assertThat(indicators.verticalSpacing(indicatorHeight, 0, StepIndicatorAlignment.BOTTOM)).isEqualTo(0)
    }

    @Test
    fun `when indicator alignment is top, vertical spacing above the indicator should be sum of steprow height and vertical spacing`(){
        val indicatorHeight = 30
        val indicators = IndicatorMeasureAndPlaceHelpersImpl(stepRows)
        val verticalSpacing = indicators.verticalSpacing(indicatorHeight, 1, StepIndicatorAlignment.TOP)
        assertThat(verticalSpacing).isEqualTo(170)
    }

    @Test
    fun `when indicator alignment is center, vertical spacing above the indicator should be sum (half of current steprow height , half of previous steprow height , vertical spacing below previous stepRow`(){
        val indicatorHeight = 30
        val indicators = IndicatorMeasureAndPlaceHelpersImpl(stepRows)
        val verticalSpacing = indicators.verticalSpacing(indicatorHeight, 1, StepIndicatorAlignment.CENTER)
        assertThat(verticalSpacing).isEqualTo(245)
    }

    @Test
    fun `when indicator alignment is bottom, vertical spacing above the indicator should be sum (current steprow height, vertical spacing below previous stepRow`(){
        val indicatorHeight = 30
        val indicators = IndicatorMeasureAndPlaceHelpersImpl(stepRows)
        val verticalSpacing = indicators.verticalSpacing(indicatorHeight, 1, StepIndicatorAlignment.BOTTOM)
        assertThat(verticalSpacing).isEqualTo(320)
    }


}

