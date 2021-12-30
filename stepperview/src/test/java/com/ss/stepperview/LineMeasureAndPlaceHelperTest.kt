package com.ss.stepperview

import com.ss.stepperview.layout.StepIndicatorLayoutList
import com.ss.stepperview.layout.base.StepIndicatorLayout
import com.ss.stepperview.layout.helpers.LineMeasureAndPlaceHelpersImpl
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.spy
import org.mockito.kotlin.whenever

class LineMeasureAndPlaceHelperTest {

    private lateinit var indicators: StepIndicatorLayoutList

    @Before
    fun setup() {
        indicators = mock()
        whenever(indicators.components).thenReturn(mock())
        lateinit var first: StepIndicatorLayout
        (0..4).forEach {
            val indicatorLayout = mock<StepIndicatorLayout>()
            whenever(indicatorLayout.width).thenReturn(30)
            whenever(indicatorLayout.height).thenReturn(30)
            whenever(indicatorLayout.xPosition).thenReturn(20)
            whenever(indicatorLayout.yPosition).thenReturn(100 * it)
            whenever(indicators.components[it]).thenReturn(indicatorLayout)
            if (it == 0) first = indicatorLayout
        }
        whenever(indicators.first).thenReturn(first)

    }

    // This shouldLineOverlapIndicator should be make configurable once implemented. For now, it should always be defaulted to false.
    @Test
    fun `Line should not overlap indicator`() {
        val lineHelper = LineMeasureAndPlaceHelpersImpl(indicators)

        assertThat(lineHelper.shouldLineOverlapIndicator()).isEqualTo(false)
    }

    @Test
    fun `when lineoverlapindicator is false, vertical spacing should be equal to indicators height`() {
        val lineHelper = spy(LineMeasureAndPlaceHelpersImpl(indicators))
        doReturn(false).whenever(lineHelper).shouldLineOverlapIndicator()

        (0..4).forEach {
            assertThat(lineHelper.verticalSpacing(it)).isEqualTo(30)
        }
    }

    @Test
    fun `when lineoverlapindicator is false, line height of the Line layout should be gap between two indicators ( excluding indicator itself )`() {
        val lineHelper = spy(LineMeasureAndPlaceHelpersImpl(indicators))

        (0..3).forEach {
            assertThat(lineHelper.lineHeight(it)).isEqualTo(70)
        }
    }

    @Test
    fun `when lineoverlapindicator is false, line height of the Line lat should be gap between two indicators ( excluding indicator itself )`() {
        val lineHelper = spy(LineMeasureAndPlaceHelpersImpl(indicators))
        val firstLineWidth = 4
        assertThat(lineHelper.firstLinePosition(firstLineWidth).x).isEqualTo(33)
        assertThat(lineHelper.firstLinePosition(firstLineWidth).y).isEqualTo(30)
    }
}
