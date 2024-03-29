package com.example.wcy_e_dziekanat_app.apiService
import com.example.wcy_e_dziekanat_app.dashboardActivity.dashboardModel.Course
import com.example.wcy_e_dziekanat_app.dashboardActivity.dashboardModel.Lecturer
import com.example.wcy_e_dziekanat_app.dashboardActivity.dashboardModel.Schedule
import com.example.wcy_e_dziekanat_app.dashboardActivity.dashboardModel.UserOut
import com.example.wcy_e_dziekanat_app.loginActivity.loginModel.LoginModel
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @POST("/login/")
    @Headers("Content-Type: application/json")
    fun loginUser(@Body userData: LoginModel): Call<ResponseBody>

    @GET("user/{album_number}")
    fun getUserByAlbumNumber(@Path("album_number") albumNumber: String): Call<UserOut>

    @GET("/today_schedules/by_group/{dean_group}")
    fun getTodaySchedulesByDeanGroup(@Path("dean_group") deanGroup: String): Call<List<Schedule>>

    @GET("/get_course/{course_id}")
    fun getCourseDetails(@Path("course_id") courseId: Int): Call<Course>

    @GET("/schedules/by_group/{dean_group}/on_date/")
    fun getSchedulesByDeanGroupAndDate(@Path("dean_group") deanGroup: String, @Query("date_str") date: String): Call<List<Schedule>>

    @GET("/users/by_group/{dean_group}")
    fun getUsersByDeanGroup(@Path("dean_group") deanGroup: String): Call<List<UserOut>>

    @GET("/get_lecturer_by_id/{lecturer_id}")
    fun getLecturerInfo(@Path("lecturer_id") lecturerId: Int): Call<Lecturer>

    @GET("/get_users/")
    fun getAllUsers(): Call<List<UserOut>>

    @GET("/get_lecturers/")
    fun getAllLecturers(): Call<List<Lecturer>>

    @GET("/get_courses/")
    fun getAllCourses(): Call<List<Course>>

    @PUT("/user/{album_number}")
    fun updateUser(@Path("album_number") albumNumber: String, @Body telephone: String): Call<UserOut>
}
