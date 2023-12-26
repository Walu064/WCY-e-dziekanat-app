package com.example.wcy_e_dziekanat_app

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.wcy_e_dziekanat_app.apiService.ApiService
import com.example.wcy_e_dziekanat_app.models.Course
import com.example.wcy_e_dziekanat_app.models.FullCourseInfo
import com.example.wcy_e_dziekanat_app.models.Schedule
import com.example.wcy_e_dziekanat_app.models.UserOut
import com.example.wcy_e_dziekanat_app.ui.theme.WCYedziekanatappTheme
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DashboardActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val loggedUserAlbumNumber = intent.getStringExtra("loggedUserAlbumNumber")
        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)

        setContent {
            val firstName = remember { mutableStateOf("") }
            val deanGroup = remember { mutableStateOf("") }
            val (showDialog, setShowDialog) = remember { mutableStateOf(false) }
            val (selectedCourse, setSelectedCourse) = remember { mutableStateOf<FullCourseInfo?>(null) }
            val todaySchedules = remember { mutableStateOf<List<FullCourseInfo>>(listOf()) }

            loggedUserAlbumNumber?.let { albumNum ->
                apiService.getUserByAlbumNumber(albumNum).enqueue(object : Callback<UserOut> {
                    override fun onResponse(call: Call<UserOut>, response: Response<UserOut>) {
                        if (response.isSuccessful) {
                            val userOut = response.body()
                            userOut?.let {
                                firstName.value = it.first_name
                                deanGroup.value = it.dean_group
                                fetchTodaySchedules(apiService, deanGroup.value, todaySchedules)
                            }
                        } else {
                            Log.e("API Error", "Błąd odpowiedzi: ${response.errorBody()}")
                        }
                    }

                    override fun onFailure(call: Call<UserOut>, t: Throwable) {
                        t.printStackTrace()
                    }
                })
            }

            WCYedziekanatappTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    DashboardScreen(
                        studentName = firstName.value,
                        deanGroupName = deanGroup.value,
                        setSelectedCourse = setSelectedCourse,
                        setShowDialog = setShowDialog,
                        todaySchedules = todaySchedules
                    )

                    if (showDialog) {
                        CourseDetailsDialog(fullCourseInfo = selectedCourse) {
                            setShowDialog(false)
                        }
                    }
                }
            }
        }
    }

    private fun fetchTodaySchedules(apiService: ApiService, deanGroup: String, todaySchedules: MutableState<List<FullCourseInfo>>) {
        apiService.getTodaySchedulesByDeanGroup(deanGroup).enqueue(object : Callback<List<Schedule>> {
            override fun onResponse(call: Call<List<Schedule>>, response: Response<List<Schedule>>) {
                if (response.isSuccessful) {
                    val schedules = response.body() ?: listOf()
                    todaySchedules.value = schedules.map { schedule ->
                        FullCourseInfo(schedule = schedule)
                    }
                    todaySchedules.value.forEach { fullCourseInfo ->
                        fetchCourseDetails(apiService, fullCourseInfo, todaySchedules)
                    }
                } else {
                    Log.e("API Error", "Błąd odpowiedzi: ${response.errorBody()}")
                }
            }

            override fun onFailure(call: Call<List<Schedule>>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    private fun fetchCourseDetails(apiService: ApiService, fullCourseInfo: FullCourseInfo, todaySchedules: MutableState<List<FullCourseInfo>>) {
        apiService.getCourseDetails(fullCourseInfo.schedule.course_id).enqueue(object : Callback<Course> {
            override fun onResponse(call: Call<Course>, response: Response<Course>) {
                if (response.isSuccessful) {
                    val courseDetails = response.body()
                    todaySchedules.value = todaySchedules.value.map {
                        if (it.schedule.course_id == fullCourseInfo.schedule.course_id) {
                            it.copy(courseDetails = courseDetails)
                        } else it
                    }
                }
            }

            override fun onFailure(call: Call<Course>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }
}
@Composable
fun DashboardScreen(
    studentName: String,
    deanGroupName: String,
    setSelectedCourse: (FullCourseInfo?) -> Unit,
    setShowDialog: (Boolean) -> Unit,
    todaySchedules: MutableState<List<FullCourseInfo>>
){

    val onCourseClicked = { courseInfo: FullCourseInfo ->
        setSelectedCourse(courseInfo)
        setShowDialog(true)
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopAppBar(
            title = "e-Dziekanat WCY",
            logo = painterResource(id = R.drawable.wcy_old_logo),
            onMenuClicked = { /* Obsługa kliknięcia menu */ }
        )
        Spacer(modifier = Modifier.height(32.dp))
        GreetingAndCurrentTimeSection(studentName = studentName)
        Spacer(modifier = Modifier.height(32.dp))
        InformationAboutGroupSection(deanGroupName = deanGroupName)
        CoursesScheduleTable(coursesList = todaySchedules.value, onCourseClicked)
    }
}

@Composable
fun TopAppBar(title: String, logo: Painter, onMenuClicked: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                painter = logo,
                contentDescription = "Logo Wydziału",
                modifier = Modifier.size(50.dp)
            )
            Text(text = title, style = MaterialTheme.typography.headlineMedium)
            IconButton(onClick = onMenuClicked) {
                Icon(Icons.Default.Menu, contentDescription = "Menu")
            }
        }
    }
}

@Composable
fun GreetingAndCurrentTimeSection(studentName: String) {
    val currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("EEEE - dd - MM - yyyy - HH:mm"))
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Witaj, $studentName!", style = MaterialTheme.typography.headlineMedium)
        Text(text = currentTime, style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
fun InformationAboutGroupSection(deanGroupName : String){
    Text(
        text = "Studiujesz w grupie: $deanGroupName. Plan zajęć na dziś:",
        style = MaterialTheme.typography.bodySmall
    )
}
val fixedScheduleTimes = listOf("08:00", "09:50", "11:40", "13:15", "15:45", "17:20", "19:30")

@Composable
fun CoursesScheduleTable(coursesList: List<FullCourseInfo>, onCourseClicked: (FullCourseInfo) -> Unit) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        fixedScheduleTimes.forEach { time ->
            val courseAtTime = coursesList.find { extractStartTime(it.schedule.date_time) == time }
            if (courseAtTime != null) {
                ScheduleRow(courseAtTime, onCourseClicked)
            } else {
                val currentDate = LocalDate.now()
                val currentDateTime = currentDate.toString()+"T"+time+"00"
                ScheduleRow(FullCourseInfo(Schedule(date_time = currentDateTime, classroom = "Blok wolny"))) {}
            }
        }
    }
}

@Composable
fun ScheduleRow(fullCourseInfo: FullCourseInfo, onCourseClicked: (FullCourseInfo) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onCourseClicked(fullCourseInfo) },
        elevation =  CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = extractStartTime(fullCourseInfo.schedule.date_time),
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight= FontWeight.Bold)
            )
            Text(
                text = fullCourseInfo.schedule.classroom,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight= FontWeight.Bold)
            )
            fullCourseInfo.courseDetails?.let {
                Text(
                    text = abbreviateCourseName(it.name),
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight= FontWeight.Bold)
                )
                Text(
                    text = it.lecturer,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight= FontWeight.Bold)
                )
            }
        }
    }
}

fun abbreviateCourseName(courseName: String): String {
    return courseName.split(" ")
        .filter { it.isNotEmpty() }
        .joinToString("") { it[0].uppercase() }
}

fun extractStartTime(dateTime: String): String {
    val parts = dateTime.split("T")
    return if (parts.size > 1) {
        parts[1].substring(0, 5)
    } else {
        ""
    }
}

@Composable
fun CourseDetailsDialog(fullCourseInfo: FullCourseInfo?, onDismiss: () -> Unit) {
    if (fullCourseInfo != null) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text("Szczegóły zajęć") },
            text = {
                Column {
                    Text("Czas zajęć: ${fullCourseInfo.schedule.date_time}")
                    Text("Sala: ${fullCourseInfo.schedule.classroom}")
                    fullCourseInfo.courseDetails?.let {
                        Text("Nazwa przedmiotu: ${it.name}")
                        Text("Prowadzący: ${it.lecturer}")
                        Text("Typ: ${it.type}")
                    }
                }
            },
            confirmButton = {
                Button(onClick = onDismiss) {
                    Text("Zamknij")
                }
            }
        )
    }
}

//@Preview(showBackground = true)
//@Composable
//fun DashboardActivityPreview() {
//    WCYedziekanatappTheme {
//        DashboardScreen(studentName = "Jan", deanGroupName = "WCY19IW1S0" )
//    }
//}