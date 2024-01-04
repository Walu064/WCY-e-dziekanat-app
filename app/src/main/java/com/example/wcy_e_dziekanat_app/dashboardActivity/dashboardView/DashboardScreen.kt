package com.example.wcy_e_dziekanat_app.dashboardActivity.dashboardView

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.wcy_e_dziekanat_app.R
import com.example.wcy_e_dziekanat_app.dashboardActivity.dashboardViewModel.DashboardViewModel
import com.example.wcy_e_dziekanat_app.dashboardActivity.dashboardModel.FullCourseInfo

@Composable
fun DashboardScreen(viewModel: DashboardViewModel, navController: NavController) {
    val context = LocalContext.current
    val activity = context as? ComponentActivity
    val onCourseClicked = { courseInfo: FullCourseInfo ->
        viewModel.selectedCourse.value = courseInfo
        viewModel.showDialog.value = true
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopAppBar(
            title = "e-Dziekanat WCY",
            logo = painterResource(id = R.drawable.wcy_old_logo),
            isExpanded = viewModel.isExpanded,
            navController = navController,
            activity = activity
        )
        Spacer(modifier = Modifier.height(32.dp))
        GreetingAndCurrentTimeSection(studentName = viewModel.firstName.value)
        Spacer(modifier = Modifier.height(32.dp))
        InformationAboutGroupSection(deanGroupName = viewModel.deanGroup.value)
        CoursesScheduleTable(coursesList = viewModel.todaySchedules.value, onCourseClicked)

        if (viewModel.showDialog.value) {
            CourseDetailsDialog(fullCourseInfo = viewModel.selectedCourse.value) {
                viewModel.showDialog.value = false
            }
        }
    }
}
