package com.ss.stepperview.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ss.stepperview.sample.data.StudyTaskStepperView
import com.ss.stepperview.sample.data.studyTaskList
import com.ss.stepperview.sample.ui.theme.StepperViewTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // for testing different sized steps
        val heights = arrayOf(
            50.dp,
            100.dp,
            40.dp,
            30.dp,
            20.dp,
            20.dp,
            20.dp,
            40.dp,
            30.dp,
            20.dp,
            20.dp,
            20.dp
        )
        setContent {
            StepperViewTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = Color.White) {
                    StudyTaskStepperView(
                        studyTaskList,
                    )
                }
            }
        }
    }

//                    StepperView(items = steps,
//                    stepsPerRow = StepsPerRow.ONE) {
//                        steps.forEachIndexed { index, it ->
////                            Step {
////                                Text(
////                                    modifier = Modifier
////                                        .align(StepAlignment.LEFT)
////                                        .height(heights[index])
////                                        .background(Color.Red),
////                                    textAlign = TextAlign.Center,
////                                    text = "Nowew${index}"
////                                )
////                            }
//                            Step {
//                                Text(
//                                    modifier = Modifier
//                                        .align(StepAlignment.RIGHT)
//                                        .height(heights[index])
//                                        .background(Color.Red),
//                                    textAlign = TextAlign.Center,
//                                    text = it
//                                )
//                            }
//
//                        }
//                    }
//                }
//            }
//        }


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
