package com.ss.stepperview.sample.data

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.ss.stepperview.StepAlignment
import com.ss.stepperview.view.StepperView
import com.ss.stepperview.view.StepsPerRow
import com.ss.stepperview.sample.R
import com.ss.stepperview.sample.ui.theme.green01
import com.ss.stepperview.sample.ui.theme.purple01

@Composable
fun StudyTaskUI(selected: Boolean, studyTask: StudyTask, modifier : Modifier = Modifier){
    val backgroundColor = if(selected) purple01 else green01
    val textColor = if(backgroundColor == purple01) Color.White else Color.Black
    Column(modifier = modifier
        .padding(all = 8.dp)
        .width(140.dp)
        .background(color = backgroundColor, shape = RoundedCornerShape(10.dp))
        .padding(all = 8.dp)){
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween){
            Text(text = studyTask.subject, color = textColor, fontSize = 12.sp, fontWeight = FontWeight(FontWeight.Bold.weight))
            Image(painter = painterResource(id = R.drawable.ic_menu),contentDescription = null,
                modifier = Modifier
                    .size(8.dp)
                    .align(Alignment.CenterVertically)
            )
        }
        Spacer(modifier = Modifier.height(2.dp))

        Text(text = studyTask.chapter, color = textColor, fontSize = 10.sp, fontWeight = FontWeight(FontWeight.Normal.weight))
        Spacer(modifier = Modifier.height(10.dp))

        Row {
            Image(
                painter = rememberImagePainter(studyTask.tutorPic), contentDescription = null,
                modifier = Modifier
                    .padding(end = 3.dp)
                    .size(5.dp)
                    .align(Alignment.CenterVertically)
                    .background(color = Color.Blue, shape = CircleShape)
            )
            Text(text = studyTask.tutorName, color = textColor, fontSize = 8.sp, fontWeight = FontWeight(FontWeight.Light.weight))
        }
        Spacer(modifier = Modifier.height(2.dp))


        Row {
        Image(painter = painterResource(id = R.drawable.ic_event),contentDescription = null,
        modifier = Modifier
            .padding(end = 3.dp)
            .size(5.dp)
            .align(Alignment.CenterVertically)
        )
        Text(text = studyTask.meetingType.link, color = textColor, fontSize = 8.sp, fontWeight = FontWeight(FontWeight.Light.weight))
        }
    }
}

@Composable
fun StudyTaskTimeUI(studyTask: StudyTask, modifier : Modifier = Modifier){
    Column(
        modifier = modifier){
        Text(text = studyTask.startTime, color = Color.Companion.Black, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = studyTask.endTime, color = Color.Companion.Gray, fontSize = 8.sp, fontWeight = FontWeight.Normal)
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    StudyTaskTimeUI(
        studyTaskList[1]
    )
}


@Composable
fun StudyTaskStepperView(studyTaskList: ArrayList<StudyTask>){
    StepperView(items = studyTaskList,
    stepsPerRow = StepsPerRow.TWO) {
        studyTaskList.forEachIndexed{ index, it ->
            var alignment = StepAlignment.RIGHT
            if( index % 2 == 0 || index % 5 == 0 ){
                alignment = StepAlignment.RIGHT
            }
            StudyTaskTimeUI(studyTask = it,
                modifier = Modifier
                    .align(StepAlignment.LEFT))
            StudyTaskUI(selected = false, studyTask = it,
                modifier = Modifier
                    .align(alignment))

        }
    }
}