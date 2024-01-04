package com.example.wcy_e_dziekanat_app.loginActivity

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import com.example.wcy_e_dziekanat_app.backendIntegrationModules.apiService.ApiService
import com.example.wcy_e_dziekanat_app.dashboardActivity.DashboardActivity
import com.example.wcy_e_dziekanat_app.ui.theme.WCYedziekanatappTheme
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val apiService = retrofit.create(ApiService::class.java)

        val viewModelFactory = LoginViewModelFactory(apiService)
        val viewModel = ViewModelProvider(this, viewModelFactory)[LoginViewModel::class.java]

        setContent {
            WCYedziekanatappTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    LoginView(viewModel, ::startDashboardActivity)
                }
            }
        }
    }

    private fun startDashboardActivity(albumNumber: String) {
        val intent = Intent(this, DashboardActivity::class.java)
        intent.putExtra("loggedUserAlbumNumber", albumNumber)
        startActivity(intent)
    }
}
