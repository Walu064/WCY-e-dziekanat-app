package com.example.wcy_e_dziekanat_app.dashboardActivity.dashboardView

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun InformationAboutGroupSection(deanGroupName : String){
    Text(
        text = "Studiujesz w grupie: $deanGroupName. Plan zajęć na dziś:",
        style = MaterialTheme.typography.bodySmall
    )
}