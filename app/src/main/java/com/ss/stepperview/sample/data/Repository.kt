package com.ss.stepperview.sample.data
import com.ss.stepperview.sample.R

val steps = listOf(
    "Create WooCommerce website Create WooCommerce website Create WooCommerce website Create WooCommerce website",
    "Install Jetpack plugin Create WooCommerce website",
    "Install Woocommerce plugin Create WooCommerce website",
    "Activate Woocommerce plugin",
    "Activate Jetpack plugin",
    "Install app",
    "Code",
    "Write test",
    "Test the app",
    "Congrats!!"
)


enum class MeetingType(val link: String){
    GoogleMeet("Google Meet") {
        override val imageResId: Int
            get() = R.drawable.ic_event
    },
    Zoom("Zoom") {
        override val imageResId: Int
            get() = R.drawable.ic_event
    },
    GoogleDocs("Google Docs") {
        override val imageResId: Int
            get() = R.drawable.ic_event
    };
    abstract val imageResId : Int
}
data class StudyTask(val subject: String, val chapter: String, val tutorName: String, val tutorPic: String, val meetingType: MeetingType, val startTime: String, val endTime: String)
val studyTaskList = arrayListOf<StudyTask>().apply {
    add(
        StudyTask(
            subject = "Physics",
            chapter = "Chapter 3: Force",
            tutorName = "Alex Jesus",
            tutorPic = "",
            meetingType = MeetingType.GoogleMeet,
            startTime = "9:30",
            endTime = "10:20"
    ))
    add(
        StudyTask(
            subject = "Geography",
            chapter = "Chapter 12: Soil Profile",
            tutorName = "Jenifer Clark",
            tutorPic = "",
            meetingType = MeetingType.Zoom,
            startTime = "11:00",
            endTime = "11:50"
        ))

    add(
        StudyTask(
            subject = "Assignment",
            chapter = "World Regional Pattern",
            tutorName = "Alexa Tenorio",
            tutorPic = "",
            meetingType = MeetingType.GoogleDocs,
            startTime = "12:20",
            endTime = "13:00"
        ))


    add(
        StudyTask(
            subject = "Assignment",
            chapter = "World Regional Pattern",
            tutorName = "Alexa Tenorio",
            tutorPic = "",
            meetingType = MeetingType.GoogleDocs,
            startTime = "12:20",
            endTime = "13:00"
        ))


    add(
        StudyTask(
            subject = "Assignment",
            chapter = "World Regional Pattern",
            tutorName = "Alexa Tenorio",
            tutorPic = "",
            meetingType = MeetingType.GoogleDocs,
            startTime = "12:20",
            endTime = "13:00"
        ))


    add(
        StudyTask(
            subject = "Assignment",
            chapter = "World Regional Pattern",
            tutorName = "Alexa Tenorio",
            tutorPic = "",
            meetingType = MeetingType.GoogleDocs,
            startTime = "12:20",
            endTime = "13:00"
        ))


}


