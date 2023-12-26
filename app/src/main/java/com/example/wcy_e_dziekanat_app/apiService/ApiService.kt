package com.example.wcy_e_dziekanat_app.apiService
import com.example.wcy_e_dziekanat_app.models.Course
import com.example.wcy_e_dziekanat_app.models.Schedule
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

    @GET("/today_schedules/by_group/{dean_group}")
    fun getTodaySchedulesByDeanGroup(@Path("dean_group") deanGroup: String): Call<List<Schedule>>

    @GET("/get_course/{course_id}")
    fun getCourseDetails(@Path("course_id") courseId: Int): Call<Course>
}
