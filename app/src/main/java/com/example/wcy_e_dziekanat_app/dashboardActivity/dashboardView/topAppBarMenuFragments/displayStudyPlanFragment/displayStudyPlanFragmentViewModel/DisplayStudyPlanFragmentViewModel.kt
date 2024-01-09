package com.example.wcy_e_dziekanat_app.dashboardActivity.dashboardView.topAppBarMenuFragments.displayStudyPlanFragment.displayStudyPlanFragmentViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wcy_e_dziekanat_app.apiService.ApiService
import com.example.wcy_e_dziekanat_app.dashboardActivity.dashboardModel.Course
import com.example.wcy_e_dziekanat_app.dashboardActivity.dashboardModel.Lecturer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DisplayStudyPlanFragmentViewModel(private val apiService: ApiService) : ViewModel() {
    private val _studyPlan = MutableStateFlow<Map<String, List<Course>>>(emptyMap())
    val studyPlan: StateFlow<Map<String, List<Course>>> = _studyPlan

    init {
        loadLecturersAndCourses()
    }

    private fun loadLecturersAndCourses() {
        viewModelScope.launch(Dispatchers.IO) {
            apiService.getAllLecturers().enqueue(object : Callback<List<Lecturer>> {
                override fun onResponse(
                    call: Call<List<Lecturer>>,
                    response: Response<List<Lecturer>>
                ) {
                    if (response.isSuccessful) {
                        val lecturers = response.body()?.associateBy { it.id } ?: emptyMap()
                        loadCourses(lecturers)
                    } else {
                        //TODO: Obsłuż błąd odpowiedzi dla prowadzących
                    }
                }

                override fun onFailure(call: Call<List<Lecturer>>, t: Throwable) {
                    //TODO: Obsłuż wyjątek dla zapytania o prowadzących
                }
            })
        }
    }

    private fun loadCourses(lecturers: Map<Int, Lecturer>) {
        apiService.getAllCourses().enqueue(object : Callback<List<Course>> {
            override fun onResponse(
                call: Call<List<Course>>,
                response: Response<List<Course>>
            ) {
                if (response.isSuccessful) {
                    val courses = response.body()?.map { course ->
                        course.copy(lecturerName = lecturers[course.lecturer]?.let {
                            "${it.first_name} ${it.last_name}"
                        })
                    } ?: emptyList()
                    _studyPlan.value = courses.groupBy { it.semester }
                        .toSortedMap(compareBy { romanNumeralToInt(it) })
                } else {
                    //TODO: Obsłuż błąd odpowiedzi dla kursów
                }
            }

            override fun onFailure(call: Call<List<Course>>, t: Throwable) {
                //TODO: Obsłuż wyjątek dla zapytania o kursy
            }
        })
    }

    fun romanNumeralToInt(numeral: String): Int {
        val romanNumerals = mapOf(
            "I" to 1, "II" to 2, "III" to 3,
            "IV" to 4, "V" to 5, "VI" to 6,
            "VII" to 7, "VIII" to 8, "IX" to 9, "X" to 10
        )

        return romanNumerals[numeral] ?: 0
    }
}