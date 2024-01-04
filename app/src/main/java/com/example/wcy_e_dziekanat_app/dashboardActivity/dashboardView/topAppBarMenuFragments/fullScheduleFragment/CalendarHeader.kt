package com.example.wcy_e_dziekanat_app.dashboardActivity.dashboardView.topAppBarMenuFragments.fullScheduleFragment

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import java.time.YearMonth
import java.util.Locale

@Composable
fun CalendarHeader(currentMonthYear: MutableState<YearMonth>) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = {
            currentMonthYear.value = currentMonthYear.value.minusMonths(1)
        }) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Poprzedni miesiąc")
        }
        Text(
            text = "${currentMonthYear.value.month.name.lowercase(Locale.ROOT)
                .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }} ${currentMonthYear.value.year}",
            style = MaterialTheme.typography.headlineSmall
        )
        IconButton(onClick = {
            currentMonthYear.value = currentMonthYear.value.plusMonths(1)
        }) {
            Icon(Icons.Default.ArrowForward, contentDescription = "Następny miesiąc")
        }
    }
}
