package com.example.wcy_e_dziekanat_app.dashboardActivity.dashboardView.topAppBarMenuFragments.searchStudentFragment.searchStudentFragmentView


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.wcy_e_dziekanat_app.dashboardActivity.dashboardModel.UserOut
import com.example.wcy_e_dziekanat_app.dashboardActivity.dashboardView.topAppBarMenuFragments.searchStudentFragment.searchStudentFragmentViewModel.SearchStudentFragmentViewModel

@Composable
fun SearchStudentFragmentView(viewModel: SearchStudentFragmentViewModel, navController: NavController) {
    val users by viewModel.filteredUsers.collectAsState()
    val errorMessageState by viewModel.errorMessage.collectAsState()
    val errorMessage = errorMessageState
    var searchQuery by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    var selectedUser by remember { mutableStateOf<UserOut?>(null) }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = {
                    searchQuery = it
                    viewModel.searchStudents(it)
                },
                label = { Text("Wyszukaj studenta") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (!errorMessage.isNullOrEmpty()) {
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error
                )
            }

            LazyColumn(modifier = Modifier.fillMaxWidth().weight(1f)) {
                items(users) { user ->
                    UserItem(user, onUserClick = {
                        selectedUser = user
                        showDialog = true
                    })
                }
            }

            if (showDialog) {
                UserDetailsDialog(user = selectedUser, onDismiss = {
                    showDialog = false
                    selectedUser = null
                })
            }

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
}

@Composable
fun UserItem(user: UserOut, onUserClick: (UserOut) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onUserClick(user) },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "${user.first_name} ${user.second_name}",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "Grupa dziekańska: ${user.dean_group}",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
