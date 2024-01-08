package com.example.wcy_e_dziekanat_app.dashboardActivity.dashboardView.topAppBarMenuFragments.displayStudyPlanFragment.displayStudyPlanViewModelFactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.wcy_e_dziekanat_app.apiService.ApiService
import com.example.wcy_e_dziekanat_app.dashboardActivity.dashboardView.topAppBarMenuFragments.displayStudyPlanFragment.displayStudyPlanFragmentViewModel.DisplayStudyPlanFragmentViewModel

@Suppress("UNCHECKED_CAST")
class DisplayStudyPlanFragmentViewModelFactory(private val apiService: ApiService) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DisplayStudyPlanFragmentViewModel::class.java)) {
            return DisplayStudyPlanFragmentViewModel(apiService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}