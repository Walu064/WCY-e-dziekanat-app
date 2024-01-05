package com.example.wcy_e_dziekanat_app.dashboardActivity.dashboardView.topAppBarMenuFragments.fullScheduleFragment.fullScheduleFragmentView

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import java.time.LocalDate

@Composable
fun DayView(
    day: LocalDate,
    isSelected: Boolean,
    onDaySelected: (LocalDate) -> Unit
) {
    TextButton(
        onClick = { onDaySelected(day) },
        modifier = Modifier.size(35.dp)
    ) {
        Text(
            text = day.dayOfMonth.toString(),
            color = if (isSelected) Color.Blue else Color.Black
        )
    }
}
