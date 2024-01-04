package com.example.wcy_e_dziekanat_app

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
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
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Locale

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
            val isExpanded = remember {mutableStateOf(false)}

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
                val navController = rememberNavController()

                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {

                    NavHost(navController = navController, startDestination = "dashboardActivity") {
                        composable("dashboardActivity") {
                            DashboardScreen(
                                studentName = firstName.value,
                                deanGroupName = deanGroup.value,
                                setSelectedCourse = setSelectedCourse,
                                setShowDialog = setShowDialog,
                                todaySchedules = todaySchedules,
                                isExpanded = isExpanded,
                                navController = navController,
                                activity = this@DashboardActivity
                            )
                            if (showDialog) {
                                CourseDetailsDialog(fullCourseInfo = selectedCourse) {
                                    setShowDialog(false)
                                }
                            }
                        }
                        composable("myProfileFragment") {
                            MyProfileFragment(navController = navController)
                        }
                        composable("fullScheduleFragment") {
                            FullScheduleFragment(navController = navController)
                        }
                        composable("deanGroupFragment") {
                            DeanGroupFragment(navController = navController)
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
    todaySchedules: MutableState<List<FullCourseInfo>>,
    isExpanded: MutableState<Boolean>,
    navController: NavController,
    activity: ComponentActivity
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
            isExpanded = isExpanded,
            navController = navController,
            activity = activity
        )
        Spacer(modifier = Modifier.height(32.dp))
        GreetingAndCurrentTimeSection(studentName = studentName)
        Spacer(modifier = Modifier.height(32.dp))
        InformationAboutGroupSection(deanGroupName = deanGroupName)
        CoursesScheduleTable(coursesList = todaySchedules.value, onCourseClicked)

    }
}

@Composable
fun TopAppBar(
    title: String,
    logo: Painter,
    isExpanded: MutableState<Boolean>,
    navController: NavController,
    activity: ComponentActivity) {
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
            IconButton(onClick = { isExpanded.value = true }) {
                Icon(Icons.Default.Menu, contentDescription = "Menu")
                DropdownMenu(expanded = isExpanded.value, onDismissRequest = { isExpanded.value = false }) {
                    DropdownMenuItem(trailingIcon = {Icons.Default.Person}, text = { Text("Mój profil") }, onClick = {
                        (!isExpanded.value).also { isExpanded.value = it }
                        navController.navigate("myProfileFragment") })
                    DropdownMenuItem(trailingIcon = {Icons.Default.DateRange}, text = { Text("Wyświetl plan zajęć") }, onClick = {isExpanded.value = !isExpanded.value
                        navController.navigate("fullScheduleFragment") })
                    DropdownMenuItem(trailingIcon = {Icons.Default.List}, text = { Text("Wyświetl skład grupy") }, onClick = {isExpanded.value = !isExpanded.value
                        navController.navigate("deanGroupFragment") })
                    DropdownMenuItem(trailingIcon = {Icons.Default.ExitToApp}, text = { Text("Wyloguj") }, onClick = { activity.finish()})
                }
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

@Composable
fun MyProfileFragment(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Profil Użytkownika", style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(16.dp))
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { navController.popBackStack() }) {
            Text("Powrót do ekranu głównego.")
        }
    }
}
@Composable
fun FullScheduleFragment(navController: NavController) {
    val selectedDate = remember { mutableStateOf<LocalDate?>(null) }
    val currentMonthYear = remember { mutableStateOf(YearMonth.now()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CalendarHeader(currentMonthYear)
        CalendarView(currentMonthYear, selectedDate)

        Spacer(modifier = Modifier.height(16.dp))

        selectedDate.value?.let {
            Text("Wybrana data: ${it.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))}")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { navController.popBackStack() }) {
            Text("Powrót")
        }
    }
}

@Composable
fun CalendarHeader(currentMonthYear: MutableState<YearMonth>) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = {
            currentMonthYear.value = currentMonthYear.value.minusMonths(1)
        }) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Poprzedni miesiąc")
        }
        Text(
            text = "${currentMonthYear.value.month.name.lowercase(Locale.ROOT)
                .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }} ${currentMonthYear.value.year}",
            style = MaterialTheme.typography.headlineSmall
        )
        IconButton(onClick = {
            currentMonthYear.value = currentMonthYear.value.plusMonths(1)
        }) {
            Icon(Icons.Default.ArrowForward, contentDescription = "Następny miesiąc")
        }
    }
}

@Composable
fun CalendarView(currentMonthYear: MutableState<YearMonth>, selectedDate: MutableState<LocalDate?>) {
    val currentMonth = currentMonthYear.value
    val daysInMonth = currentMonth.lengthOfMonth()
    val firstOfMonth = currentMonth.atDay(1)
    val daysOffset = firstOfMonth.dayOfWeek.value - 1
    val days = (1..daysInMonth).map { day ->
        currentMonth.atDay(day)
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(7),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(daysOffset) {
            Spacer(modifier = Modifier.size(40.dp))
        }
        items(days) { day ->
            DayView(day = day, selectedDate = selectedDate)
        }
    }
}

@Composable
fun DayView(day: LocalDate, selectedDate: MutableState<LocalDate?>) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .background(if (selectedDate.value == day) Color.Gray else Color.Transparent)
            .clickable {
                selectedDate.value = day
            },
        contentAlignment = Alignment.Center
    ) {
        Text(text = day.dayOfMonth.toString())
    }
}


@Composable
fun DeanGroupFragment(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Grupa Dziekańska", style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(16.dp))

        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { navController.popBackStack() }) {
            Text("Powrót")
        }
    }
}
