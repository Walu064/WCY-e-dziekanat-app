package com.example.wcy_e_dziekanat_app.dashboardActivity.dashboardModel

data class UserOut(
    val id: Int,
    val album_number: String,
    val first_name: String,
    val second_name: String,
    val dean_group: String,
    val email_address: String,
    val telephone: String
)

data class Course(
    val name: String,
    val lecturer: Int,
    val type: String,
    val semester: String,
    val id: Int,
    var lecturerName: String? = null
)

data class Lecturer(
    val id: Int,
    val first_name: String,
    val last_name: String,
    val office: String,
    val telephone: String,
    val email_address: String,
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

