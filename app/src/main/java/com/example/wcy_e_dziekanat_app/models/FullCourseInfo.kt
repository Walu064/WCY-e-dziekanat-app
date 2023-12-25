package com.example.wcy_e_dziekanat_app.models

data class FullCourseInfo(
    val schedule: Schedule,
    val courseDetails: Course? = null
)
