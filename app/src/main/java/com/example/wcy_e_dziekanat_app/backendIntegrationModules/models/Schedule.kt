package com.example.wcy_e_dziekanat_app.backendIntegrationModules.models

data class Schedule(
    val date_time: String,
    val classroom: String,
    val dean_group: String = "",
    val course_id: Int = 0
)
