package com.example.wcy_e_dziekanat_app.dashboardActivity.dashboardModel

data class UserOut(
    val id: Int,
    val album_number: String,
    val first_name: String,
    val second_name: String,
    val dean_group: String
)

data class Course(
    val id: Int,
    val name: String,
    val lecturer: Int = 0,
    val type: String
)

data class Lecturer(
    val id: Int,
    val first_name: String,
    val last_name: String,
    val office: String,
    val telephone: String,
    val email_adress: String,
    val photo: String
)

data class Schedule(
    val date_time: String,
    val classroom: String,
    val dean_group: String = "",
    val course_id: Int = 0
)

data class FullCourseInfo(
    val schedule: Schedule,
    val courseDetails: Course? = null,
    val lecturerDetails: Lecturer? = null
)

