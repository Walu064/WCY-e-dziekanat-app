package com.example.wcy_e_dziekanat_app.dashboardActivity.dashboardView

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.wcy_e_dziekanat_app.dashboardActivity.dashboardModel.FullCourseInfo
import com.example.wcy_e_dziekanat_app.dashboardActivity.dashboardModel.Schedule
import java.time.LocalDate


val fixedScheduleTimes = listOf("08:00", "09:50", "11:40", "13:15", "15:45", "17:35", "19:30")

@Composable
fun CoursesScheduleTable(coursesList: List<FullCourseInfo>, onCourseClicked: (FullCourseInfo) -> Unit) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        fixedScheduleTimes.forEach { time ->
            val courseAtTime = coursesList.find { extractStartTime(it.schedule.date_time) == time }
            if (courseAtTime != null) {
                ScheduleRow(courseAtTime, onCourseClicked)
            } else {
                val currentDate = LocalDate.now()
                val currentDateTime = currentDate.toString()+"T"+time+"00"
                ScheduleRow(FullCourseInfo(Schedule(date_time = currentDateTime, classroom = "Blok wolny"))) {}
            }
        }
    }
}