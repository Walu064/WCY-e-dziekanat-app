package com.example.wcy_e_dziekanat_app.loginActivity

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
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
import com.example.wcy_e_dziekanat_app.backendIntegrationModules.apiService.ApiService
import com.example.wcy_e_dziekanat_app.backendIntegrationModules.models.UserLogin
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun LoginScreen(apiService: ApiService, startDashboardActivity: (String) -> Unit) {
    var albumNumber by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

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

        errorMessage?.let {
            Text(it, color = Color.Red)
            Spacer(modifier = Modifier.height(8.dp))
        }

        TextField(
            value = albumNumber,
            onValueChange = { albumNumber = it },
            label = { Text("Numer albumu studenta") }
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Hasło") },
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            performLogin(apiService, albumNumber, password) { success, error ->
                if (success) {
                    errorMessage = null
                    startDashboardActivity(albumNumber)
                } else {
                    errorMessage = error ?: "Nieznany błąd"
                }
            }
        }) {
            Text("Zaloguj się!")
        }
    }
}

private fun performLogin(
    apiService: ApiService,
    albumNumber: String,
    password: String,
    onResult: (Boolean, String?) -> Unit
) {
    val call = apiService.loginUser(UserLogin(albumNumber, password))
    call.enqueue(object : Callback<ResponseBody> {
        override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
            if (response.isSuccessful) {
                onResult(true, null)
            } else {
                onResult(false, "Kod błędu: ${response.code()}")
            }
        }

        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
            onResult(false, "Błąd sieci: ${t.message}")
        }
    })
}
