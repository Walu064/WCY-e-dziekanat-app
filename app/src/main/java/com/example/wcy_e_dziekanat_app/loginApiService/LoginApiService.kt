package com.example.wcy_e_dziekanat_app.loginApiService
import com.example.wcy_e_dziekanat_app.models.UserLogin
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.POST
import retrofit2.http.Body
import retrofit2.http.Headers

interface LoginApiService {
    @POST("/login/")
    @Headers("Content-Type: application/json")
    fun loginUser(@Body userData: UserLogin): Call<ResponseBody>
}
