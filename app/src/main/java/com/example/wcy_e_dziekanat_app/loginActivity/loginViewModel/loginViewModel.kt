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
    var loginSuccess: Boolean = false

    fun performLogin(onResult: (Boolean, String?) -> Unit) {
        val call = apiService.loginUser(LoginModel(albumNumber.value, password.value))
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    loginSuccess = true
                    onResult(true, null)
                } else {
                    loginSuccess = false
                    onResult(false, "Kod błędu: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                loginSuccess = false
                onResult(false, "Błąd sieci: ${t.message}")
            }
        })
    }
}
