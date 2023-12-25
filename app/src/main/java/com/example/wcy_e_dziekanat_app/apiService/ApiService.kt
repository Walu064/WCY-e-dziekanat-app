package com.example.wcy_e_dziekanat_app.loginApiService
import com.example.wcy_e_dziekanat_app.models.UserLogin
import com.example.wcy_e_dziekanat_app.models.UserOut
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.POST
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface ApiService {
    @POST("/login/")
    @Headers("Content-Type: application/json")
    fun loginUser(@Body userData: UserLogin): Call<ResponseBody>

    @GET("user/{album_number}")
    fun getUserByAlbumNumber(@Path("album_number") albumNumber: String): Call<UserOut>
}
