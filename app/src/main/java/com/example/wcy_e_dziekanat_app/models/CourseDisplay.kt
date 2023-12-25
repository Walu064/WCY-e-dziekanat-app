package com.example.wcy_e_dziekanat_app.models

data class CourseDisplay(
    val block: Int,
    val hours: String,
    val courseName: String,
    val classroom: String,
    val lecturer: String
)

val exampleCourses = listOf(
    CourseDisplay(1, "08:00-09:30", "PZWM", "101", "Dr Jan Kowalski"),
    CourseDisplay(2, "09:45-11:15", "JP2GMD", "102", "prof. Karol Wojtyła"),
)
