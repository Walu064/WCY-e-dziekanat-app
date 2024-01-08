package com.example.wcy_e_dziekanat_app.dashboardActivity.dashboardView.topAppBarMenuFragments.searchCourseFragment.searchCourseFragmentV

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.wcy_e_dziekanat_app.dashboardActivity.dashboardModel.Course
import com.example.wcy_e_dziekanat_app.dashboardActivity.dashboardView.topAppBarMenuFragments.searchCourseFragment.searchCourseFragmentVM.SearchCourseFragmentViewModel
@Composable
fun SearchCourseFragmentView(viewModel: SearchCourseFragmentViewModel, navController: NavController) {
    val courses by viewModel.filteredCourses.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    var searchQuery by remember { mutableStateOf("") }

    Box(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 70.dp)) {

            OutlinedTextField(
                value = searchQuery,
                onValueChange = {
                    searchQuery = it
                    viewModel.searchCourses(it)
                },
                label = { Text("Wyszukaj przedmiot") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(courses) { course ->
                    CourseItem(course)
                }
            }

            errorMessage?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        Button(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text("Powrót")
        }
    }
}


@Composable
fun CourseItem(course: Course) {
    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 4.dp)) {

        Column(modifier = Modifier.padding(16.dp)) {
            Text(course.name, style = MaterialTheme.typography.bodyLarge)
            Text("Typ: ${course.type}", style = MaterialTheme.typography.bodyLarge)
        }
    }
}