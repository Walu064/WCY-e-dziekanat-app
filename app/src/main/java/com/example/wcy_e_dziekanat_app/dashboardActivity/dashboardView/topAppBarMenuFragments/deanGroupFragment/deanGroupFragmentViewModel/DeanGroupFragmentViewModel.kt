package com.example.wcy_e_dziekanat_app.dashboardActivity.dashboardView.topAppBarMenuFragments.deanGroupFragment.deanGroupFragmentViewModel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.wcy_e_dziekanat_app.apiService.ApiService
import com.example.wcy_e_dziekanat_app.dashboardActivity.dashboardModel.UserOut
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DeanGroupFragmentViewModel(private val apiService: ApiService) : ViewModel() {

    var students = mutableStateOf<List<UserOut>>(emptyList())
    var errorMessage = mutableStateOf<String?>(null)

    fun fetchStudents(deanGroup: String) {
        apiService.getUsersByDeanGroup(deanGroup).enqueue(object : Callback<List<UserOut>> {
            override fun onResponse(call: Call<List<UserOut>>, response: Response<List<UserOut>>) {
                if (response.isSuccessful) {
                    students.value = response.body() ?: emptyList()
                    errorMessage.value = null
                } else {
                    errorMessage.value = "Error: ${response.code()} ${response.message()}"
                }
            }

            override fun onFailure(call: Call<List<UserOut>>, t: Throwable) {
                errorMessage.value = "Failure: ${t.message}"
            }
        })
    }
}
