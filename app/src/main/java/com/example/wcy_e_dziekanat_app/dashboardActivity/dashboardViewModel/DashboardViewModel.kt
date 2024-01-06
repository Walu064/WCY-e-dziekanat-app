package com.example.wcy_e_dziekanat_app.dashboardActivity.dashboardViewModel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.wcy_e_dziekanat_app.apiService.ApiService
import com.example.wcy_e_dziekanat_app.dashboardActivity.dashboardModel.Course
import com.example.wcy_e_dziekanat_app.dashboardActivity.dashboardModel.FullCourseInfo
import com.example.wcy_e_dziekanat_app.dashboardActivity.dashboardModel.Lecturer
import com.example.wcy_e_dziekanat_app.dashboardActivity.dashboardModel.Schedule
import com.example.wcy_e_dziekanat_app.dashboardActivity.dashboardModel.UserOut
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
    val errorMessage = mutableStateOf("")

    fun getUserData(albumNumber: String) {
        apiService.getUserByAlbumNumber(albumNumber).enqueue(object : Callback<UserOut> {
            override fun onResponse(call: Call<UserOut>, response: Response<UserOut>) {
                if (response.isSuccessful) {
                    val user = response.body()
                    firstName.value = user?.first_name ?: ""
                    deanGroup.value = user?.dean_group ?: ""
                    fetchTodaySchedules()
                } else {
                    errorMessage.value = "Error: ${response.code()} ${response.message()}"
                }
            }

            override fun onFailure(call: Call<UserOut>, t: Throwable) {
                errorMessage.value = "Failure: ${t.message}"
            }
        })
    }

    private fun fetchTodaySchedules() {
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
                } else {
                    errorMessage.value = "Error: ${response.code()} ${response.message()}"
                }
            }

            override fun onFailure(call: Call<List<Schedule>>, t: Throwable) {
                errorMessage.value = "Failure: ${t.message}"
            }
        })
    }

    private fun fetchCourseDetails(fullCourseInfo: FullCourseInfo) {
        apiService.getCourseDetails(fullCourseInfo.schedule.course_id).enqueue(object : Callback<Course> {
            override fun onResponse(call: Call<Course>, response: Response<Course>) {
                if (response.isSuccessful) {
                    val courseDetails = response.body()
                    if (courseDetails != null && courseDetails.lecturer != 0) {
                        fetchLecturerDetails(fullCourseInfo, courseDetails)
                    }
                } else {
                    errorMessage.value = "Error: ${response.code()} ${response.message()}"
                }
            }

            override fun onFailure(call: Call<Course>, t: Throwable) {
                errorMessage.value = "Failure: ${t.message}"
            }
        })
    }

    private fun fetchLecturerDetails(fullCourseInfo: FullCourseInfo, course: Course) {
        apiService.getLecturerInfo(course.lecturer).enqueue(object : Callback<Lecturer> {
            override fun onResponse(call: Call<Lecturer>, response: Response<Lecturer>) {
                if (response.isSuccessful) {
                    val lecturerDetails = response.body()
                    val updatedCourses = todaySchedules.value.map { info ->
                        if (info.schedule.course_id == course.id) {
                            FullCourseInfo(info.schedule, course, lecturerDetails)
                        } else {
                            info
                        }
                    }
                    todaySchedules.value = updatedCourses
                } else {
                    errorMessage.value = "Error: ${response.code()} ${response.message()}"
                }
            }

            override fun onFailure(call: Call<Lecturer>, t: Throwable) {
                errorMessage.value = "Failure: ${t.message}"
            }
        })
    }
}
