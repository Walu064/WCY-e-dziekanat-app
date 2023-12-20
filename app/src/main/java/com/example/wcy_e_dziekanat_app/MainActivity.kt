package com.example.wcy_e_dziekanat_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.wcy_e_dziekanat_app.loginApiService.LoginApiService
import com.example.wcy_e_dziekanat_app.models.UserLogin
import com.example.wcy_e_dziekanat_app.ui.theme.WCYedziekanatappTheme
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WCYedziekanatappTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    LoginScreen()
                }
            }
        }
    }
}

@Composable
fun LoginScreen() {
    var albumNumber by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val retrofit = Retrofit.Builder()
        .baseUrl("http://10.0.2.2:8000/") // Użyj 10.0.2.2 dla emulatora Androida, aby wskazać na localhost Twojego komputera
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService = retrofit.create(LoginApiService::class.java)

    var showDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Zalogowano pomyślnie") },
            confirmButton = {
                Button(onClick = { showDialog = false }) {
                    Text("OK")
                }
            }
        )
    }

    errorMessage?.let {
        Text(it, color = Color.Red)
    }

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
            performLogin(apiService ,albumNumber, password){ success, error ->
                if (success) {
                    showDialog = true
                    errorMessage = null
                } else {
                    errorMessage = error
                }
            }
        }) {
            Text("Zaloguj się!")
        }
    }
}

private fun performLogin(
    apiService: LoginApiService,
    albumNumber: String,
    password: String,
    onResult: (Boolean, String?) -> Unit
) {
    val call = apiService.loginUser(UserLogin(albumNumber, password))
    call.enqueue(object : retrofit2.Callback<ResponseBody> {
        override fun onResponse(call: Call<ResponseBody>, response: retrofit2.Response<ResponseBody>) {
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

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    WCYedziekanatappTheme {
        LoginScreen()
    }
}
