package com.example.wcy_e_dziekanat_app.dashboardActivity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.wcy_e_dziekanat_app.apiService.ApiService
import com.example.wcy_e_dziekanat_app.dashboardActivity.dashboardView.DashboardScreen
import com.example.wcy_e_dziekanat_app.dashboardActivity.dashboardView.topAppBarMenuFragments.deanGroupFragment.DeanGroupFragment
import com.example.wcy_e_dziekanat_app.dashboardActivity.dashboardView.topAppBarMenuFragments.fullScheduleFragment.fullScheduleFragmentView.FullScheduleFragment
import com.example.wcy_e_dziekanat_app.dashboardActivity.dashboardView.topAppBarMenuFragments.fullScheduleFragment.fullScheduleFragmentViewModel.FullScheduleFragmentViewModel
import com.example.wcy_e_dziekanat_app.dashboardActivity.dashboardView.topAppBarMenuFragments.fullScheduleFragment.fullScheduleFragmentViewModelFactory.FullScheduleFragmentViewModelFactory
import com.example.wcy_e_dziekanat_app.dashboardActivity.dashboardView.topAppBarMenuFragments.myProfileFragment.MyProfileFragment
import com.example.wcy_e_dziekanat_app.dashboardActivity.dashboardViewModel.DashboardViewModel
import com.example.wcy_e_dziekanat_app.dashboardActivity.dashboardViewModelFactory.DashboardViewModelFactory
import com.example.wcy_e_dziekanat_app.ui.theme.WCYedziekanatappTheme
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DashboardActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val apiService = retrofit.create(ApiService::class.java)

        val dashboardActivityViewModelFactory = DashboardViewModelFactory(apiService)
        val dashboardActivityViewModel = ViewModelProvider(this, dashboardActivityViewModelFactory)[DashboardViewModel::class.java]

        val fullScheduleFragmentViewModelFactory = FullScheduleFragmentViewModelFactory(apiService)
        val fullScheduleFragmentViewModel = ViewModelProvider(this, fullScheduleFragmentViewModelFactory)[FullScheduleFragmentViewModel::class.java]

        setContent {
            val navController = rememberNavController()
            WCYedziekanatappTheme {
                    NavHost(navController = navController, startDestination = "dashboardActivityView") {
                        composable("dashboardActivityView") {
                            Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                                DashboardScreen(viewModel = dashboardActivityViewModel, navController = navController)
                            }
                        }
                        composable("myProfileFragment") {
                            MyProfileFragment(navController = navController)
                        }
                        composable("fullScheduleFragment") {
                            FullScheduleFragment(viewModel = fullScheduleFragmentViewModel, navController = navController, deanGroup = dashboardActivityViewModel.deanGroup.value)
                        }
                        composable("deanGroupFragment") {
                            DeanGroupFragment(navController = navController)
                        }
                    }
                }
            }
        intent.getStringExtra("loggedUserAlbumNumber")?.let { albumNumber ->
            dashboardActivityViewModel.getUserData(albumNumber)
        }
    }
}
