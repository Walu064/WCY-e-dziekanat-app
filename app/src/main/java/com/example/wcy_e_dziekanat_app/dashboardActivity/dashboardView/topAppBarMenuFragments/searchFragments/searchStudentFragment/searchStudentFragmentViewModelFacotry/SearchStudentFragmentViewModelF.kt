package com.example.wcy_e_dziekanat_app.dashboardActivity.dashboardView.topAppBarMenuFragments.searchFragments.searchStudentFragment.searchStudentFragmentViewModelFacotry

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.wcy_e_dziekanat_app.apiService.ApiService
import com.example.wcy_e_dziekanat_app.dashboardActivity.dashboardView.topAppBarMenuFragments.searchFragments.searchStudentFragment.searchStudentFragmentViewModel.SearchStudentFragmentViewModel

@Suppress("UNCHECKED_CAST")
class SearchStudentFragmentViewModelF(private val apiService: ApiService) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchStudentFragmentViewModel::class.java)) {
            return SearchStudentFragmentViewModel(apiService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}