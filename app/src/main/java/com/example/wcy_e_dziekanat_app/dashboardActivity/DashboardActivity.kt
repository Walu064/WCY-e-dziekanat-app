package com.example.wcy_e_dziekanat_app.dashboardActivity

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.wcy_e_dziekanat_app.backendIntegrationModules.apiService.ApiService
import com.example.wcy_e_dziekanat_app.backendIntegrationModules.models.Course
import com.example.wcy_e_dziekanat_app.backendIntegrationModules.models.FullCourseInfo
import com.example.wcy_e_dziekanat_app.backendIntegrationModules.models.Schedule
import com.example.wcy_e_dziekanat_app.backendIntegrationModules.models.UserOut
import com.example.wcy_e_dziekanat_app.dashboardActivity.topAppBarMenuFragments.deanGroupFragment.DeanGroupFragment
import com.example.wcy_e_dziekanat_app.dashboardActivity.topAppBarMenuFragments.fullScheduleFragment.FullScheduleFragment
import com.example.wcy_e_dziekanat_app.dashboardActivity.topAppBarMenuFragments.myProfileFragment.MyProfileFragment
import com.example.wcy_e_dziekanat_app.ui.theme.WCYedziekanatappTheme
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DashboardActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val loggedUserAlbumNumber = intent.getStringExtra("loggedUserAlbumNumber")
        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)

        setContent {
            val firstName = remember { mutableStateOf("") }
            val deanGroup = remember { mutableStateOf("") }
            val (showDialog, setShowDialog) = remember { mutableStateOf(false) }
            val (selectedCourse, setSelectedCourse) = remember { mutableStateOf<FullCourseInfo?>(null) }
            val todaySchedules = remember { mutableStateOf<List<FullCourseInfo>>(listOf()) }
            val isExpanded = remember {mutableStateOf(false)}

            loggedUserAlbumNumber?.let { albumNum ->
                apiService.getUserByAlbumNumber(albumNum).enqueue(object : Callback<UserOut> {
                    override fun onResponse(call: Call<UserOut>, response: Response<UserOut>) {
                        if (response.isSuccessful) {
                            val userOut = response.body()
                            userOut?.let {
                                firstName.value = it.first_name
                                deanGroup.value = it.dean_group
                                fetchTodaySchedules(apiService, deanGroup.value, todaySchedules)
                            }
                        } else {
                            Log.e("API Error", "Błąd odpowiedzi: ${response.errorBody()}")
                        }
                    }

                    override fun onFailure(call: Call<UserOut>, t: Throwable) {
                        t.printStackTrace()
                    }
                })
            }

            WCYedziekanatappTheme {
                val navController = rememberNavController()

                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {

                    NavHost(navController = navController, startDestination = "dashboardActivity") {
                        composable("dashboardActivity") {
                            DashboardScreen(
                                studentName = firstName.value,
                                deanGroupName = deanGroup.value,
                                setSelectedCourse = setSelectedCourse,
                                setShowDialog = setShowDialog,
                                todaySchedules = todaySchedules,
                                isExpanded = isExpanded,
                                navController = navController,
                                activity = this@DashboardActivity
                            )
                            if (showDialog) {
                                CourseDetailsDialog(fullCourseInfo = selectedCourse) {
                                    setShowDialog(false)
                                }
                            }
                        }
                        composable("myProfileFragment") {
                            MyProfileFragment(navController = navController)
                        }
                        composable("fullScheduleFragment") {
                            FullScheduleFragment(navController = navController)
                        }
                        composable("deanGroupFragment") {
                            DeanGroupFragment(navController = navController)
                        }
                    }
                }
            }
        }
    }

    private fun fetchTodaySchedules(apiService: ApiService, deanGroup: String, todaySchedules: MutableState<List<FullCourseInfo>>) {
        apiService.getTodaySchedulesByDeanGroup(deanGroup).enqueue(object : Callback<List<Schedule>> {
            override fun onResponse(call: Call<List<Schedule>>, response: Response<List<Schedule>>) {
                if (response.isSuccessful) {
                    val schedules = response.body() ?: listOf()
                    todaySchedules.value = schedules.map { schedule ->
                        FullCourseInfo(schedule = schedule)
                    }
                    todaySchedules.value.forEach { fullCourseInfo ->
                        fetchCourseDetails(apiService, fullCourseInfo, todaySchedules)
                    }
                } else {
                    Log.e("API Error", "Błąd odpowiedzi: ${response.errorBody()}")
                }
            }

            override fun onFailure(call: Call<List<Schedule>>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    private fun fetchCourseDetails(apiService: ApiService, fullCourseInfo: FullCourseInfo, todaySchedules: MutableState<List<FullCourseInfo>>) {
        apiService.getCourseDetails(fullCourseInfo.schedule.course_id).enqueue(object : Callback<Course> {
            override fun onResponse(call: Call<Course>, response: Response<Course>) {
                if (response.isSuccessful) {
                    val courseDetails = response.body()
                    todaySchedules.value = todaySchedules.value.map {
                        if (it.schedule.course_id == fullCourseInfo.schedule.course_id) {
                            it.copy(courseDetails = courseDetails)
                        } else it
                    }
                }
            }

            override fun onFailure(call: Call<Course>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }
}