package com.example.wcy_e_dziekanat_app.dashboardActivity.dashboardView

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.wcy_e_dziekanat_app.dashboardActivity.dashboardModel.FullCourseInfo


@Composable
fun ScheduleRow(fullCourseInfo: FullCourseInfo, onCourseClicked: (FullCourseInfo) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onCourseClicked(fullCourseInfo) },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = extractStartTime(fullCourseInfo.schedule.date_time),
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.weight(1f),
            )
            Text(
                text = fullCourseInfo.schedule.classroom,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )
            fullCourseInfo.courseDetails?.let {
                Text(
                    text = abbreviateCourseName(it.name),
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
            }
            fullCourseInfo.lecturerDetails?.let {
                Text(
                    text = "${it.first_name} ${it.last_name}",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.weight(2f),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}




fun extractStartTime(dateTime: String): String {
    val parts = dateTime.split("T")
    return if (parts.size > 1) {
        parts[1].substring(0, 5)
    } else {
        ""
    }
}


fun abbreviateCourseName(courseName: String): String {
    return courseName.split(" ")
        .filter { it.isNotEmpty() }
        .joinToString("") { it[0].uppercase() }
}

