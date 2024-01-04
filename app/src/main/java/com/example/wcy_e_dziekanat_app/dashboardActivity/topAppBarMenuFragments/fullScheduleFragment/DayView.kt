package com.example.wcy_e_dziekanat_app.dashboardActivity.topAppBarMenuFragments.fullScheduleFragment

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import java.time.LocalDate

@Composable
fun DayView(day: LocalDate, selectedDate: MutableState<LocalDate?>) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .background(if (selectedDate.value == day) Color.Gray else Color.Transparent)
            .clickable {
                selectedDate.value = day
            },
        contentAlignment = Alignment.Center
    ) {
        Text(text = day.dayOfMonth.toString())
    }
}