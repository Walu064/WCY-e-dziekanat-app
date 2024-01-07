package com.example.wcy_e_dziekanat_app.dashboardActivity.dashboardView.topAppBarMenuFragments.searchFragments.searchsearchLecturerFragment.searchLecturerFragmentVM

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wcy_e_dziekanat_app.apiService.ApiService
import com.example.wcy_e_dziekanat_app.dashboardActivity.dashboardModel.Lecturer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchLecturerFragmentVM(private val apiService: ApiService) : ViewModel() {
    private val _lecturers = MutableStateFlow<List<Lecturer>>(emptyList())
    val lecturers: StateFlow<List<Lecturer>> = _lecturers

    private val _filteredLecturers = MutableStateFlow<List<Lecturer>>(emptyList())
    val filteredLecturers: StateFlow<List<Lecturer>> = _filteredLecturers

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    init {
        fetchAllLecturers()
    }

    private fun fetchAllLecturers() {
        viewModelScope.launch {
            apiService.getAllLecturers().enqueue(object : Callback<List<Lecturer>> {
                override fun onResponse(call: Call<List<Lecturer>>, response: Response<List<Lecturer>>) {
                    if (response.isSuccessful) {
                        _lecturers.value = response.body() ?: emptyList()
                        _filteredLecturers.value = _lecturers.value
                    } else {
                        _errorMessage.value = "Błąd: ${response.code()}. ${response.message()}"
                    }
                }

                override fun onFailure(call: Call<List<Lecturer>>, t: Throwable) {
                    _errorMessage.value = "Błąd połączenia: ${t.message}"
                }
            })
        }
    }

    fun searchLecturers(query: String) {
        _filteredLecturers.value = if (query.isEmpty()) {
            _lecturers.value
        } else {
            _lecturers.value.filter {
                it.first_name.contains(query, ignoreCase = true) ||
                        it.last_name.contains(query, ignoreCase = true) ||
                        it.office.contains(query, ignoreCase = true)
            }
        }
    }
}
