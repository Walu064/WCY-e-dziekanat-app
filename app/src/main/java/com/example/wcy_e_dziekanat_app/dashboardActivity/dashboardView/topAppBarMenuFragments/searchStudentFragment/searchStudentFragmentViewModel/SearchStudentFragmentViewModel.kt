package com.example.wcy_e_dziekanat_app.dashboardActivity.dashboardView.topAppBarMenuFragments.searchStudentFragment.searchStudentFragmentViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wcy_e_dziekanat_app.apiService.ApiService
import com.example.wcy_e_dziekanat_app.dashboardActivity.dashboardModel.UserOut
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchStudentFragmentViewModel(private val apiService: ApiService) : ViewModel() {
    private val _users = MutableStateFlow<List<UserOut>>(emptyList())
    private val _filteredUsers = MutableStateFlow<List<UserOut>>(emptyList())
    val filteredUsers: StateFlow<List<UserOut>> = _filteredUsers

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    init {
        fetchAllUsers()
    }

    private fun fetchAllUsers() {
        viewModelScope.launch {
            apiService.getAllUsers().enqueue(object : Callback<List<UserOut>> {
                override fun onResponse(call: Call<List<UserOut>>, response: Response<List<UserOut>>) {
                    if (response.isSuccessful) {
                        val usersList = response.body() ?: emptyList()
                        _users.value = usersList
                        _filteredUsers.value = usersList
                    } else {
                        _errorMessage.value = "Błąd: ${response.code()}. ${response.message()}"
                    }
                }

                override fun onFailure(call: Call<List<UserOut>>, t: Throwable) {
                    _errorMessage.value = "Błąd połączenia: ${t.message}"
                }
            })
        }
    }

    private var hasSearched = false

    fun searchStudents(query: String) {
        hasSearched = true
        if (query.isEmpty() && !hasSearched) {
            _filteredUsers.value = emptyList()
        } else {
            _filteredUsers.value = _users.value.filter {
                it.first_name.contains(query, ignoreCase = true) ||
                        it.second_name.contains(query, ignoreCase = true)
            }
        }
    }
}