package com.example.wcy_e_dziekanat_app.dashboardActivity.topAppBarMenuFragments.fullScheduleFragment

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

@Composable
fun FullScheduleFragment(navController: NavController) {
    val selectedDate = remember { mutableStateOf<LocalDate?>(null) }
    val currentMonthYear = remember { mutableStateOf(YearMonth.now()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CalendarHeader(currentMonthYear)
        CalendarView(currentMonthYear, selectedDate)

        Spacer(modifier = Modifier.height(16.dp))

        selectedDate.value?.let {
            Text("Wybrana data: ${it.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))}")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { navController.popBackStack() }) {
            Text("Powrót")
        }
    }
}
