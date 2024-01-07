package com.example.wcy_e_dziekanat_app.dashboardActivity.dashboardView.topAppBarMenuFragments.searchFragments.searchsearchLecturerFragment.searchLecturerFragmentV
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.wcy_e_dziekanat_app.dashboardActivity.dashboardModel.Lecturer
import com.example.wcy_e_dziekanat_app.dashboardActivity.dashboardView.topAppBarMenuFragments.searchFragments.searchsearchLecturerFragment.searchLecturerFragmentVM.SearchLecturerFragmentVM

@Composable
fun SearchLecturerFragmentV(viewModel: SearchLecturerFragmentVM, navController: NavController) {
    val lecturers by viewModel.filteredLecturers.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    val errorMessage by viewModel.errorMessage.collectAsState()

    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { newValue ->
                searchQuery = newValue
                viewModel.searchLecturers(newValue)
            },
            label = { Text("Wyszukaj wykładowcę") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(lecturers) { lecturer ->
                LecturerItem(lecturer)
            }
        }

        errorMessage?.let {
            Text(text = it, color = MaterialTheme.colorScheme.error)
        }

        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text("Powrót")
        }
    }
}

@Composable
fun LecturerItem(lecturer: Lecturer) {
    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 4.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Imię: ${lecturer.first_name}", style = MaterialTheme.typography.bodyLarge)
            Text("Nazwisko: ${lecturer.last_name}", style = MaterialTheme.typography.bodyMedium)
            Text("Gabinet: ${lecturer.office}", style = MaterialTheme.typography.bodyMedium)
        }
    }
}
