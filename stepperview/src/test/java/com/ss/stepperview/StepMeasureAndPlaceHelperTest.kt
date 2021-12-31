package com.ss.stepperview

import com.ss.stepperview.layout.helpers.StepRowMeasureAndPlaceHelpersImpl
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class StepMeasureAndPlaceHelperTest {

    // Once vertical spacing is made configurable, test has to be updated.
    @Test
    fun `vertical spacing should be equal to default value - 20`(){
        val stepHelper = StepRowMeasureAndPlaceHelpersImpl()
        assertThat(stepHelper.verticalSpacing(0) ).isEqualTo(20)
    }
}

