package com.example.wcy_e_dziekanat_app.models

data class Schedule(
    val courseId: Int,
    val dateTime: String,
    val classroom: String,
    val deanGroup: String
)
