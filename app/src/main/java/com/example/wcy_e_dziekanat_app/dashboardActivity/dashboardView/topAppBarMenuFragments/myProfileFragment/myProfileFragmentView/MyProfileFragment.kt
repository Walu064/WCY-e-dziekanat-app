package com.example.wcy_e_dziekanat_app.dashboardActivity.dashboardView.topAppBarMenuFragments.myProfileFragment.myProfileFragmentView

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.wcy_e_dziekanat_app.dashboardActivity.dashboardModel.UserOut
import com.example.wcy_e_dziekanat_app.dashboardActivity.dashboardView.topAppBarMenuFragments.myProfileFragment.myProfileFragmentViewModel.MyProfileFragmentViewModel

@Composable
fun MyProfileFragment(viewModel: MyProfileFragmentViewModel, navController: NavController, albumNumber: String) {
    LaunchedEffect(albumNumber) {
        viewModel.fetchUserProfile(albumNumber)
    }

    val userState by viewModel.userProfile.collectAsState()
    val user = userState
    val error by viewModel.error.collectAsState()
    var isEditing by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .border(2.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(10.dp))
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Profil Użytkownika",
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            if (user != null) {
                UserInfoRow("Imię:", user.first_name ?: "Brak danych")
                UserInfoRow("Nazwisko:", user.second_name ?: "Brak danych")
                UserInfoRow("Numer albumu:", user.album_number ?: "Brak danych")
                UserInfoRow("Numer telefonu:", user.telephone ?: "Brak danych")
                UserInfoRow("Adres e-mail:", user.email_address ?: "Brak danych")
            } else if (error != null) {
                Text("Błąd: $error", style = MaterialTheme.typography.bodyLarge)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Divider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f))

            Button(onClick = { isEditing = true },
                modifier = Modifier.align(Alignment.CenterHorizontally)) {
                Text("Edytuj")
            }

            if (isEditing && user != null) {
                UserEditDialog(user) { newPhoneNumber ->
                    viewModel.updateUser(user.album_number, newPhoneNumber)
                    isEditing = false
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
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
fun UserInfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun UserEditDialog(user: UserOut, onUpdate: (String) -> Unit) {
    var newPhoneNumber by remember { mutableStateOf(user.telephone) }

    AlertDialog(
        onDismissRequest = {},
        title = { Text("Edycja numeru telefonu") },
        text = {
            Column {
                TextField(
                    value = newPhoneNumber,
                    onValueChange = { newPhoneNumber = it },
                    label = { Text("Nowy numer telefonu") }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onUpdate(newPhoneNumber) }
            ) {
                Text("Aktualizuj")
            }
        },
        dismissButton = {
            Button(onClick = {}) {
                Text("Anuluj")
            }
        }
    )
}
