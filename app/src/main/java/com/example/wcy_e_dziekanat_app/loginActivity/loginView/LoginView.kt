package com.example.wcy_e_dziekanat_app.loginActivity.loginView

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.wcy_e_dziekanat_app.R
import com.example.wcy_e_dziekanat_app.loginActivity.loginViewModel.LoginViewModel

@Composable
fun LoginView(viewModel: LoginViewModel, startDashboardActivity: (String) -> Unit) {
    val loginSuccess by viewModel.loginSuccess
    val loginError by viewModel.loginError

    LaunchedEffect(loginSuccess) {
        if (loginSuccess) {
            startDashboardActivity(viewModel.albumNumber.value)
        }
    }

    var errorMessage by remember { mutableStateOf<String?>(null) }
    errorMessage = loginError

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo",
            modifier = Modifier
                .height(250.dp)
                .width(250.dp)
                .padding(bottom = 16.dp)
        )

        TextField(
            value = viewModel.albumNumber.value,
            onValueChange = { viewModel.albumNumber.value = it },
            label = { Text("Numer albumu studenta") }
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = viewModel.password.value,
            onValueChange = { viewModel.password.value = it },
            label = { Text("Hasło") },
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { viewModel.performLogin() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text("Zaloguj się")
        }

        errorMessage?.let {
            Text(it, color = Color.Red)
        }
    }
}
