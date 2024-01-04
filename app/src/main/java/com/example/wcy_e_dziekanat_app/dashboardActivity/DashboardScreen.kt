package com.example.wcy_e_dziekanat_app.dashboardActivity

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.wcy_e_dziekanat_app.R
import com.example.wcy_e_dziekanat_app.backendIntegrationModules.models.FullCourseInfo

@Composable
fun DashboardScreen(
    studentName: String,
    deanGroupName: String,
    setSelectedCourse: (FullCourseInfo?) -> Unit,
    setShowDialog: (Boolean) -> Unit,
    todaySchedules: MutableState<List<FullCourseInfo>>,
    isExpanded: MutableState<Boolean>,
    navController: NavController,
    activity: ComponentActivity
){

    val onCourseClicked = { courseInfo: FullCourseInfo ->
        setSelectedCourse(courseInfo)
        setShowDialog(true)
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopAppBar(
            title = "e-Dziekanat WCY",
            logo = painterResource(id = R.drawable.wcy_old_logo),
            isExpanded = isExpanded,
            navController = navController,
            activity = activity
        )
        Spacer(modifier = Modifier.height(32.dp))
        GreetingAndCurrentTimeSection(studentName = studentName)
        Spacer(modifier = Modifier.height(32.dp))
        InformationAboutGroupSection(deanGroupName = deanGroupName)
        CoursesScheduleTable(coursesList = todaySchedules.value, onCourseClicked)

    }
}