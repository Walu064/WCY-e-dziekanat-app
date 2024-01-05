package com.example.wcy_e_dziekanat_app.dashboardActivity.dashboardView.topAppBarMenuFragments.myProfileFragment.myProfileFragmentViewModel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wcy_e_dziekanat_app.apiService.ApiService
import com.example.wcy_e_dziekanat_app.dashboardActivity.dashboardModel.UserOut
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyProfileFragmentViewModel(private val apiService: ApiService) : ViewModel() {
    var userProfile = mutableStateOf<UserOut?>(null)
        private set

    var error = mutableStateOf<String?>(null)
        private set

    fun fetchUserProfile(albumNumber: String) {
        viewModelScope.launch {
            apiService.getUserByAlbumNumber(albumNumber).enqueue(object : Callback<UserOut> {
                override fun onResponse(call: Call<UserOut>, response: Response<UserOut>) {
                    if (response.isSuccessful) {
                        userProfile.value = response.body()
                        error.value = null
                    } else {
                        error.value = "Błąd: ${response.code()} ${response.message()}"
                    }
                }

                override fun onFailure(call: Call<UserOut>, t: Throwable) {
                    error.value = t.message
                }
            })
        }
    }
}