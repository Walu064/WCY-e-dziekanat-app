package com.example.wcy_e_dziekanat_app.dashboardActivity.dashboardViewModel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wcy_e_dziekanat_app.apiService.ApiService
import com.example.wcy_e_dziekanat_app.dashboardActivity.dashboardModel.Course
import com.example.wcy_e_dziekanat_app.dashboardActivity.dashboardModel.FullCourseInfo
import com.example.wcy_e_dziekanat_app.dashboardActivity.dashboardModel.Schedule
import com.example.wcy_e_dziekanat_app.dashboardActivity.dashboardModel.UserOut
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DashboardViewModel(private val apiService: ApiService) : ViewModel() {
    val firstName = mutableStateOf("")
    val deanGroup = mutableStateOf("")
    val todaySchedules = mutableStateOf<List<FullCourseInfo>>(listOf())
    val showDialog = mutableStateOf(false)
    val selectedCourse = mutableStateOf<FullCourseInfo?>(null)
    val isExpanded = mutableStateOf(false)

    fun getUserData(albumNumber: String) {
        viewModelScope.launch {
            apiService.getUserByAlbumNumber(albumNumber).enqueue(object : Callback<UserOut> {
                override fun onResponse(call: Call<UserOut>, response: Response<UserOut>) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            firstName.value = it.first_name
                            deanGroup.value = it.dean_group
                            fetchTodaySchedules()
                        }
                    }
                }

                override fun onFailure(call: Call<UserOut>, t: Throwable) {
                    t.printStackTrace()
                }
            })
        }
    }

    private fun fetchTodaySchedules() {
        viewModelScope.launch {
            apiService.getTodaySchedulesByDeanGroup(deanGroup.value).enqueue(object : Callback<List<Schedule>> {
                override fun onResponse(call: Call<List<Schedule>>, response: Response<List<Schedule>>) {
                    if (response.isSuccessful) {
                        val schedules = response.body() ?: listOf()
                        todaySchedules.value = schedules.map { schedule ->
                            FullCourseInfo(schedule = schedule)
                        }
                        todaySchedules.value.forEach { fullCourseInfo ->
                            fetchCourseDetails(fullCourseInfo)
                        }
                    }
                }

                override fun onFailure(call: Call<List<Schedule>>, t: Throwable) {
                    t.printStackTrace()
                }
            })
        }
    }

    private fun fetchCourseDetails(fullCourseInfo: FullCourseInfo) {
        viewModelScope.launch {
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

    fun getSchedulesForSpecificDay(deanGroup: String, date: String) {
        viewModelScope.launch {
            apiService.getSchedulesByDeanGroupAndDate(deanGroup, date).enqueue(object : Callback<List<Schedule>> {
                override fun onResponse(call: Call<List<Schedule>>, response: Response<List<Schedule>>) {
                    if (response.isSuccessful) {
                        val schedules = response.body() ?: listOf()
                        // Możesz tu przetwarzać dane jak uważasz za stosowne
                    }
                }

                override fun onFailure(call: Call<List<Schedule>>, t: Throwable) {
                    t.printStackTrace()
                }
            })
        }
    }
}
