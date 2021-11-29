package com.ss.stepperview.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ss.stepperview.Step
import com.ss.stepperview.StepperView
import com.ss.stepperview.sample.data.steps
import com.ss.stepperview.sample.ui.theme.StepperViewTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // for testing different sized steps
        val heights = arrayOf( 50.dp , 100.dp , 40.dp, 30.dp, 20.dp )
        setContent {
            StepperViewTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    StepperView(items = steps) {
                        steps.forEachIndexed { index, it ->
                            Step {
                                Text(
                                    modifier = Modifier.height(heights[index]).background(Color.Red),
                                    textAlign = TextAlign.Center,
                                    text = it
                                )
                            }

                        }
                    }
                }
            }
        }
    }

    @Composable
    fun Greeting(name: String) {
        Text(text = "Hello $name!")
    }

    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        StepperViewTheme {
            Greeting("Android")
        }
    }

}
