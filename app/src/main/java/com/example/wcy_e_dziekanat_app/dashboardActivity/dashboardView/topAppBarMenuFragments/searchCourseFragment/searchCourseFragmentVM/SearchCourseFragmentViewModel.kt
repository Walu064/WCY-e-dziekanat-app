package com.example.wcy_e_dziekanat_app.dashboardActivity.dashboardView.topAppBarMenuFragments.searchCourseFragment.searchCourseFragmentVM

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wcy_e_dziekanat_app.apiService.ApiService
import com.example.wcy_e_dziekanat_app.dashboardActivity.dashboardModel.Course
import com.example.wcy_e_dziekanat_app.dashboardActivity.dashboardModel.Lecturer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchCourseFragmentViewModel(private val apiService: ApiService) : ViewModel() {
    private val _courses = MutableStateFlow<List<Course>>(emptyList())
    val courses: StateFlow<List<Course>> = _courses

    private val _filteredCourses = MutableStateFlow<List<Course>>(emptyList())
    val filteredCourses: StateFlow<List<Course>> = _filteredCourses

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _lecturers = MutableStateFlow<Map<Int, Lecturer>>(emptyMap())

    init {
        fetchAllCourses()
        fetchAllLecturers()
    }

    private fun fetchAllLecturers() {
        viewModelScope.launch {
            apiService.getAllLecturers().enqueue(object : Callback<List<Lecturer>> {
                override fun onResponse(call: Call<List<Lecturer>>, response: Response<List<Lecturer>>) {
                    if (response.isSuccessful) {
                        val lecturersList = response.body() ?: emptyList()
                        _lecturers.value = lecturersList.associateBy { it.id }
                    } else {
                        //TODO: Obsługa błędu
                    }
                }

                override fun onFailure(call: Call<List<Lecturer>>, t: Throwable) {
                    //TODO: Obsługa wyjątku
                }
            })
        }
    }

    private fun fetchAllCourses() {
        viewModelScope.launch {
            apiService.getAllCourses().enqueue(object : Callback<List<Course>> {
                override fun onResponse(call: Call<List<Course>>, response: Response<List<Course>>) {
                    if (response.isSuccessful) {
                        _courses.value = response.body()?.map { course ->
                            val lecturerName = _lecturers.value[course.lecturer]?.let { lecturer ->
                                "${lecturer.first_name} ${lecturer.last_name}"
                            } ?: "Nieznany prowadzący"

                            course.copy(lecturerName = lecturerName)
                        } ?: emptyList()
                        _filteredCourses.value = _courses.value
                    } else {
                        _errorMessage.value = "Błąd: ${response.code()}. ${response.message()}"
                    }
                }

                override fun onFailure(call: Call<List<Course>>, t: Throwable) {
                    _errorMessage.value = "Błąd połączenia: ${t.message}"
                }
            })
        }
    }

    fun searchCourses(query: String) {
        _filteredCourses.value = if (query.isEmpty()) {
            _courses.value
        } else {
            _courses.value.filter {
                it.name.contains(query, ignoreCase = true) ||
                        it.type.contains(query, ignoreCase = true)
            }
        }
    }
}