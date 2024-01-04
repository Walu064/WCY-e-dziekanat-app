package com.example.wcy_e_dziekanat_app.dashboardActivity

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.wcy_e_dziekanat_app.backendIntegrationModules.models.FullCourseInfo


@Composable
fun CourseDetailsDialog(fullCourseInfo: FullCourseInfo?, onDismiss: () -> Unit) {
    if (fullCourseInfo != null) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text("Szczegóły zajęć") },
            text = {
                Column {
                    Text("Czas zajęć: ${fullCourseInfo.schedule.date_time}")
                    Text("Sala: ${fullCourseInfo.schedule.classroom}")
                    fullCourseInfo.courseDetails?.let {
                        Text("Nazwa przedmiotu: ${it.name}")
                        Text("Prowadzący: ${it.lecturer}")
                        Text("Typ: ${it.type}")
                    }
                }
            },
            confirmButton = {
                Button(onClick = onDismiss) {
                    Text("Zamknij")
                }
            }
        )
    }
}