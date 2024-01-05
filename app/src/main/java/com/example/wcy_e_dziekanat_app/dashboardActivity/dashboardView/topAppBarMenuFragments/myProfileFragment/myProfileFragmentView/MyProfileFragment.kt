package com.example.wcy_e_dziekanat_app.dashboardActivity.dashboardView.topAppBarMenuFragments.myProfileFragment.myProfileFragmentView

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.wcy_e_dziekanat_app.dashboardActivity.dashboardView.topAppBarMenuFragments.myProfileFragment.myProfileFragmentViewModel.MyProfileFragmentViewModel

@Composable
fun MyProfileFragment(viewModel: MyProfileFragmentViewModel, navController: NavController,  albumNumber: String) {

    LaunchedEffect(albumNumber) {
        viewModel.fetchUserProfile(albumNumber)
    }

    val user = viewModel.userProfile.value
    val error = viewModel.error.value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Profil Użytkownika", style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(16.dp))

        if (user != null) {
            Text("Imię: ${user.first_name}", style = MaterialTheme.typography.bodyLarge)
            Text("Nazwisko: ${user.second_name}", style = MaterialTheme.typography.bodyLarge)
            Text("Numer albumu: ${user.album_number}", style = MaterialTheme.typography.bodyLarge)
        } else if (error != null) {
            Text("Błąd: $error", style = MaterialTheme.typography.bodyLarge)
        }

        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = { navController.popBackStack() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Powrót")
        }
    }
}