package com.example.wcy_e_dziekanat_app.dashboardActivity.dashboardView.topAppBarMenuFragments.fullScheduleFragment.fullScheduleFragmentView

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.wcy_e_dziekanat_app.dashboardActivity.dashboardModel.FullCourseInfo
import com.example.wcy_e_dziekanat_app.dashboardActivity.dashboardView.CourseDetailsDialog
import com.example.wcy_e_dziekanat_app.dashboardActivity.dashboardView.CoursesScheduleTable
import com.example.wcy_e_dziekanat_app.dashboardActivity.dashboardView.topAppBarMenuFragments.customHeader.CustomHeader
import com.example.wcy_e_dziekanat_app.dashboardActivity.dashboardView.topAppBarMenuFragments.fullScheduleFragment.fullScheduleFragmentViewModel.FullScheduleFragmentViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun FullScheduleFragment(viewModel : FullScheduleFragmentViewModel, navController: NavController, deanGroup: String) {
    val context = LocalContext.current
    context as? ComponentActivity
    val onCourseClicked = { courseInfo: FullCourseInfo ->
        viewModel.selectedCourse.value = courseInfo
        viewModel.showDialog.value = true
    }

    val currentMonthYear = viewModel.currentMonthYear
    val selectedDate = remember { mutableStateOf<LocalDate?>(null) }

    LaunchedEffect(selectedDate.value) {
        selectedDate.value?.let {
            viewModel.getSchedulesForSpecificDay(deanGroup, it.toString())
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CustomHeader(headerContent = "Plan zajęć grupy $deanGroup")
        CalendarHeader(currentMonthYear)
        CalendarView(
            currentMonthYear = currentMonthYear.value,
            selectedDate = selectedDate.value,
            onDateSelected = { date ->
                selectedDate.value = date
                viewModel.onDateSelected(date, deanGroup)
            }
        )

        Spacer(modifier = Modifier.height(2.dp))

        selectedDate.value?.let {
            Text("Wybrana data: ${it.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))}",
                style = MaterialTheme.typography.bodyMedium)
            CoursesScheduleTable(coursesList = viewModel.specificDaySchedules.value, onCourseClicked)
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = { navController.popBackStack() }) {
            Text("Powrót")
        }

        if (viewModel.showDialog.value) {
            CourseDetailsDialog(fullCourseInfo = viewModel.selectedCourse.value) {
                viewModel.showDialog.value = false
            }
        }
    }
}
