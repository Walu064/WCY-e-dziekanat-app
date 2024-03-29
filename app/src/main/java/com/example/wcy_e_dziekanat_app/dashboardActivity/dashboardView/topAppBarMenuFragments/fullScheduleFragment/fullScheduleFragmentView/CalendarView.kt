package com.example.wcy_e_dziekanat_app.dashboardActivity.dashboardView.topAppBarMenuFragments.fullScheduleFragment.fullScheduleFragmentView

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import java.time.YearMonth

@Composable
fun CalendarView(
    currentMonthYear: YearMonth,
    selectedDate: LocalDate?,
    onDateSelected: (LocalDate) -> Unit // Dodano callback na wybór daty
) {
    val daysInMonth = currentMonthYear.lengthOfMonth()
    val firstOfMonth = currentMonthYear.atDay(1)
    val daysOffset = firstOfMonth.dayOfWeek.value - 1
    val days = (1..daysInMonth).map { day ->
        currentMonthYear.atDay(day)
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(7),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(daysOffset) {
            Spacer(modifier = Modifier.size(40.dp))
        }
        items(days) { day ->
            DayView(
                day = day,
                isSelected = day == selectedDate,
                onDaySelected = onDateSelected // Przekazujemy callback do com.example.wcy_e_dziekanat_app.dashboardActivity.dashboardView.topAppBarMenuFragments.fullScheduleFragment.fullScheduleFragmentView.DayView
            )
        }
    }
}
