package com.example.wcy_e_dziekanat_app.loginActivity.loginViewModel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.wcy_e_dziekanat_app.apiService.ApiService
import com.example.wcy_e_dziekanat_app.loginActivity.loginModel.LoginModel
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(private val apiService: ApiService) : ViewModel() {
    var albumNumber = mutableStateOf("")
    var password = mutableStateOf("")
    var loginSuccess = mutableStateOf(false)
    var loginError = mutableStateOf<String?>(null)

    fun performLogin() {
        val call = apiService.loginUser(LoginModel(albumNumber.value, password.value))
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    loginSuccess.value = true
                    loginError.value = null
                } else {
                    loginSuccess.value = false
                    loginError.value = "Kod błędu: ${response.code()}"
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                loginSuccess.value = false
                loginError.value = "Błąd sieci: ${t.message}"
            }
        })
    }
}
