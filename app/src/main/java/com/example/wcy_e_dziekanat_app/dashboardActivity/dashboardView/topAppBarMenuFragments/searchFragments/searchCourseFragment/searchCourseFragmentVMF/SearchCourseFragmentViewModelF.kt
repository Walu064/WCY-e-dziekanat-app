package com.example.wcy_e_dziekanat_app.dashboardActivity.dashboardView.topAppBarMenuFragments.searchFragments.searchCourseFragment.searchCourseFragmentVMF

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.wcy_e_dziekanat_app.apiService.ApiService
import com.example.wcy_e_dziekanat_app.dashboardActivity.dashboardView.topAppBarMenuFragments.searchFragments.searchCourseFragment.searchCourseFragmentVM.SearchCourseFragmentViewModel

@Suppress("UNCHECKED_CAST")
class SearchCourseFragmentViewModelF(private val apiService: ApiService) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchCourseFragmentViewModel::class.java)) {
            return SearchCourseFragmentViewModel(apiService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}