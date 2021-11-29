package com.ss.stepperview.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.ss.stepperview.Step
import com.ss.stepperview.StepperView
import com.ss.stepperview.sample.data.steps
import com.ss.stepperview.sample.ui.theme.StepperViewTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StepperViewTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    StepperView(items = steps) {
                        steps.forEach {
                            Step {
                                Text(text = it)
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
