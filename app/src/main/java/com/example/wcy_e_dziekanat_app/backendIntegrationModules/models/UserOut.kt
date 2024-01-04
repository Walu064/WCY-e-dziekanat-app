package com.example.wcy_e_dziekanat_app.backendIntegrationModules.models

data class UserOut(
    val id: Int,
    val album_number: String,
    val first_name: String,
    val second_name: String,
    val dean_group: String
)