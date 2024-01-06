package com.example.wcy_e_dziekanat_app.dashboardActivity.dashboardView.topAppBarMenuFragments.myProfileFragment.myProfileFragmentViewModel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.wcy_e_dziekanat_app.apiService.ApiService
import com.example.wcy_e_dziekanat_app.dashboardActivity.dashboardModel.UserOut
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyProfileFragmentViewModel(private val apiService: ApiService) : ViewModel() {
    var userProfile = mutableStateOf<UserOut?>(null)
    var error = mutableStateOf<String?>(null)

    fun fetchUserProfile(albumNumber: String) {
        apiService.getUserByAlbumNumber(albumNumber).enqueue(object : Callback<UserOut> {
            override fun onResponse(call: Call<UserOut>, response: Response<UserOut>) {
                if (response.isSuccessful) {
                    userProfile.value = response.body()
                    error.value = null
                } else {
                    error.value = "Error: ${response.code()} ${response.message()}"
                }
            }

            override fun onFailure(call: Call<UserOut>, t: Throwable) {
                error.value = "Failure: ${t.message}"
            }
        })
    }
}
