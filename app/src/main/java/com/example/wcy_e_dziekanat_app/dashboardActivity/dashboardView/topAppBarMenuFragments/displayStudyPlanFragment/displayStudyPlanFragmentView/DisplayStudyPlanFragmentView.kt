package com.example.wcy_e_dziekanat_app.dashboardActivity.dashboardView.topAppBarMenuFragments.displayStudyPlanFragment.displayStudyPlanFragmentView

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.wcy_e_dziekanat_app.dashboardActivity.dashboardModel.Course
import com.example.wcy_e_dziekanat_app.dashboardActivity.dashboardView.topAppBarMenuFragments.displayStudyPlanFragment.displayStudyPlanFragmentViewModel.DisplayStudyPlanFragmentViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DisplayStudyPlanFragmentView(viewModel: DisplayStudyPlanFragmentViewModel, navController: NavController) {
    val studyPlan by viewModel.studyPlan.collectAsState()
    val scope = rememberCoroutineScope()

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(modifier = Modifier.padding(bottom = 56.dp)) {
            studyPlan.forEach { (semester, courses) ->
                stickyHeader {
                    SemesterHeader(semester)
                }
                items(courses) { course ->
                    CourseItem(course)
                }
                item { Spacer(modifier = Modifier.height(8.dp)) }
            }
        }

        Button(
            onClick = { scope.launch { navController.popBackStack() } },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            Text("Powrót")
        }
    }
}

@Composable
fun SemesterHeader(semester: String) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(8.dp)
    ) {
        Text(
            text = "Semestr $semester",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}

@Composable
fun CourseItem(course: Course) {
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = course.name, style = MaterialTheme.typography.bodyLarge)
            Text(text = "Typ: ${course.type}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Prowadzący: ${course.lecturerName}", style = MaterialTheme.typography.bodyMedium)
        }
    }
}
