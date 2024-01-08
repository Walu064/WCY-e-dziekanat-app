package com.example.wcy_e_dziekanat_app.dashboardActivity.dashboardView.topAppBarMenuFragments.searchsearchLecturerFragment.searchLecturerFragmentVMF

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.wcy_e_dziekanat_app.apiService.ApiService
import com.example.wcy_e_dziekanat_app.dashboardActivity.dashboardView.topAppBarMenuFragments.searchsearchLecturerFragment.searchLecturerFragmentVM.SearchLecturerFragmentVM

@Suppress("UNCHECKED_CAST")
class SearchLecturerFragmentVMF(private val apiService: ApiService) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchLecturerFragmentVM::class.java)) {
            return SearchLecturerFragmentVM(apiService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}