package com.example.wcy_e_dziekanat_app.dashboardActivity.dashboardView.topAppBarMenuFragments.searchFragments.searchCourseFragment.searchCourseFragmentVM

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wcy_e_dziekanat_app.apiService.ApiService
import com.example.wcy_e_dziekanat_app.dashboardActivity.dashboardModel.Course
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

    init {
        fetchAllCourses()
    }

    private fun fetchAllCourses() {
        viewModelScope.launch {
            apiService.getAllCourses().enqueue(object : Callback<List<Course>> {
                override fun onResponse(call: Call<List<Course>>, response: Response<List<Course>>) {
                    if (response.isSuccessful) {
                        _courses.value = response.body() ?: emptyList()
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