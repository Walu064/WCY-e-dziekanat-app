package com.example.wcy_e_dziekanat_app.dashboardActivity.dashboardView.topAppBarMenuFragments.fullScheduleFragment.fullScheduleFragmentViewModel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wcy_e_dziekanat_app.apiService.ApiService
import com.example.wcy_e_dziekanat_app.dashboardActivity.dashboardModel.Course
import com.example.wcy_e_dziekanat_app.dashboardActivity.dashboardModel.FullCourseInfo
import com.example.wcy_e_dziekanat_app.dashboardActivity.dashboardModel.Schedule
import kotlinx.coroutines.launch
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

    fun getSchedulesForSpecificDay(deanGroup: String, date: String) {
        viewModelScope.launch {
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
                        specificDaySchedules.value = specificDaySchedules.value.map {
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

    fun onDateSelected(newDate: LocalDate, deanGroup: String) {
        selectedDate.value = newDate
        getSchedulesForSpecificDay(deanGroup, newDate.toString())
    }
}
