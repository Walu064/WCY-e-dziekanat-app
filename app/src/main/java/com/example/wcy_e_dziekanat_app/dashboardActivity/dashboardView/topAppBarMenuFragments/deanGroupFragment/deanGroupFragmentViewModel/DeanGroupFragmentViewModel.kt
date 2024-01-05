package com.example.wcy_e_dziekanat_app.dashboardActivity.dashboardView.topAppBarMenuFragments.deanGroupFragment.deanGroupFragmentViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wcy_e_dziekanat_app.apiService.ApiService
import com.example.wcy_e_dziekanat_app.dashboardActivity.dashboardModel.UserOut
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException

class DeanGroupFragmentViewModel(private val apiService: ApiService) : ViewModel() {

    private val _students = MutableStateFlow<List<UserOut>>(emptyList())
    val students: StateFlow<List<UserOut>> = _students

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun fetchStudents(deanGroup: String) {
        viewModelScope.launch {
            try {
                val response = apiService.getUsersByDeanGroup(deanGroup)
                if (response.isSuccessful) {
                    _students.value = response.body() ?: emptyList()
                    _errorMessage.value = null
                } else {
                    _errorMessage.value = "Błąd: Nie udało się pobrać danych studentów."
                }
            } catch (e: Exception) {
                if (e is HttpException && e.code() == 404) {
                    _errorMessage.value = "Nie znaleziono studentów w tej grupie dziekańskiej."
                } else {
                    _errorMessage.value = "Błąd połączenia: ${e.localizedMessage}"
                }
            }
        }
    }
}
