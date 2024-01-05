package com.example.wcy_e_dziekanat_app.dashboardActivity.dashboardView

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.wcy_e_dziekanat_app.dashboardActivity.dashboardModel.FullCourseInfo
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun CourseDetailsDialog(fullCourseInfo: FullCourseInfo?, onDismiss: () -> Unit) {
    if (fullCourseInfo != null) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = {
                Text(
                    "Szczegóły zajęć",
                    style = MaterialTheme.typography.headlineSmall,
                    textAlign = TextAlign.Center
                )
            },
            text = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                        Text(
                            "Czas zajęć: ",
                            modifier = Modifier.weight(1f),
                            fontWeight = FontWeight.Bold
                        )
                        Text(formatDateTime(fullCourseInfo.schedule.date_time), modifier = Modifier.weight(1f))
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                        Text("Sala: ", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
                        Text(fullCourseInfo.schedule.classroom, modifier = Modifier.weight(1f))
                    }
                    fullCourseInfo.courseDetails?.let {
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                            Text("Nazwa przedmiotu: ", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
                            Text(it.name, modifier = Modifier.weight(1f))
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                            Text("Prowadzący: ", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
                            Text(it.lecturer, modifier = Modifier.weight(1f))
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                            Text("Typ: ", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
                            Text(it.type, modifier = Modifier.weight(1f))
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Zamknij")
                }
            }
        )
    }
}
fun formatDateTime(dateTimeString: String): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
    val dateTime = LocalDateTime.parse(dateTimeString, formatter)
    return dateTime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"))
}