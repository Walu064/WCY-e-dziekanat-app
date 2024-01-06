package com.example.wcy_e_dziekanat_app.dashboardActivity.dashboardView.topAppBarMenuFragments.fullScheduleFragment.fullScheduleFragmentViewModel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.wcy_e_dziekanat_app.apiService.ApiService
import com.example.wcy_e_dziekanat_app.dashboardActivity.dashboardModel.Course
import com.example.wcy_e_dziekanat_app.dashboardActivity.dashboardModel.FullCourseInfo
import com.example.wcy_e_dziekanat_app.dashboardActivity.dashboardModel.Lecturer
import com.example.wcy_e_dziekanat_app.dashboardActivity.dashboardModel.Schedule
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.time.YearMonth

class FullScheduleFragmentViewModel(private val apiService: ApiService) : ViewModel() {
    val specificDaySchedules = mutableStateOf<List<FullCourseInfo>>(listOf())
    val selectedCourse = mutableStateOf<FullCourseInfo?>(null)
    val showDialog = mutableStateOf(false)
    val currentMonthYear = mutableStateOf(YearMonth.now())
    private val selectedDate = mutableStateOf<LocalDate?>(null)
    val errorMessage = mutableStateOf("")

    fun getSchedulesForSpecificDay(deanGroup: String, date: String) {
        apiService.getSchedulesByDeanGroupAndDate(deanGroup, date).enqueue(object : Callback<List<Schedule>> {
            override fun onResponse(call: Call<List<Schedule>>, response: Response<List<Schedule>>) {
                if (response.isSuccessful) {
                    val schedules = response.body() ?: listOf()
                    specificDaySchedules.value = schedules.map { schedule ->
                        FullCourseInfo(schedule = schedule)
                    }
                    specificDaySchedules.value.forEach { fullCourseInfo ->
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
                    val updatedCourses = specificDaySchedules.value.map { info ->
                        if (info.schedule.course_id == course.id) {
                            FullCourseInfo(info.schedule, course, lecturerDetails)
                        } else {
                            info
                        }
                    }
                    specificDaySchedules.value = updatedCourses
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
