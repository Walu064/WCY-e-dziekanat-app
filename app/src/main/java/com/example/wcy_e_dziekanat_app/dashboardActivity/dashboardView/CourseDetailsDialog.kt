package com.example.wcy_e_dziekanat_app.dashboardActivity.dashboardView
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.wcy_e_dziekanat_app.dashboardActivity.dashboardModel.FullCourseInfo
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun CourseDetailsDialog(fullCourseInfo: FullCourseInfo?, onDismiss: () -> Unit) {
    if (fullCourseInfo != null) {
        Dialog(onDismissRequest = onDismiss) {
            Surface(
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colorScheme.surface
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        "Szczegóły zajęć",
                        style = MaterialTheme.typography.headlineSmall,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                    )

                    DetailRow("Czas zajęć: ", formatDateTime(fullCourseInfo.schedule.date_time))
                    DetailRow("Sala: ", fullCourseInfo.schedule.classroom)
                    fullCourseInfo.courseDetails?.let {
                        DetailRow("Nazwa przedmiotu: ", it.name)
                        Spacer(modifier = Modifier.height(3.dp))
                        DetailRow("Prowadzący: ", it.lecturer)
                        Spacer(modifier = Modifier.height(3.dp))
                        DetailRow("Typ: ", it.type)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = onDismiss,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Zamknij")
                    }
                }
            }
        }
    }
}

@Composable
fun DetailRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(label, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
        Spacer(Modifier.width(4.dp))
        Text(value, modifier = Modifier.weight(1f))
    }
}

fun formatDateTime(dateTimeString: String): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
    val dateTime = LocalDateTime.parse(dateTimeString, formatter)
    return dateTime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"))
}
