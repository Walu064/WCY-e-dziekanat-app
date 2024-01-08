package com.example.wcy_e_dziekanat_app.dashboardActivity.dashboardView.topAppBarMenuFragments.myProfileFragment.myProfileFragmentViewModel

import androidx.lifecycle.ViewModel
import com.example.wcy_e_dziekanat_app.apiService.ApiService
import com.example.wcy_e_dziekanat_app.dashboardActivity.dashboardModel.UserOut
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyProfileFragmentViewModel(private val apiService: ApiService) : ViewModel() {
    private val _userProfile = MutableStateFlow<UserOut?>(null)
    val userProfile: StateFlow<UserOut?> = _userProfile

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun fetchUserProfile(albumNumber: String) {
        apiService.getUserByAlbumNumber(albumNumber).enqueue(object : Callback<UserOut> {
            override fun onResponse(call: Call<UserOut>, response: Response<UserOut>) {
                if (response.isSuccessful) {
                    _userProfile.value = response.body()
                    _error.value = null
                } else {
                    _error.value = "Error: ${response.code()} ${response.message()}"
                }
            }

            override fun onFailure(call: Call<UserOut>, t: Throwable) {
                _error.value = "Failure: ${t.message}"
            }
        })
    }

    fun updateUser(user: UserOut) {
        apiService.updateUser(user.album_number, user).enqueue(object : Callback<UserOut> {
            override fun onResponse(call: Call<UserOut>, response: Response<UserOut>) {
                if (response.isSuccessful) {
                    _userProfile.value = response.body()
                    _error.value = null
                } else {
                    _error.value = "Error: ${response.code()} ${response.message()}"
                }
            }

            override fun onFailure(call: Call<UserOut>, t: Throwable) {
                _error.value = "Failure: ${t.message}"
            }
        })
    }
}

