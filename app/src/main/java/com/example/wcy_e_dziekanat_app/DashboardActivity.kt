package com.example.wcy_e_dziekanat_app
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.wcy_e_dziekanat_app.models.CourseDisplay
import com.example.wcy_e_dziekanat_app.models.exampleCourses
import com.example.wcy_e_dziekanat_app.ui.theme.WCYedziekanatappTheme
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DashboardActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WCYedziekanatappTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    DashboardScreen()
                }
            }
        }
    }
}

@Composable
fun DashboardScreen() {
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
        GreetingAndCurrentTimeSection(studentName = "Jan")
        Spacer(modifier = Modifier.height(32.dp))
        InformationAboutGroupSection(deanGroupName = "WCY21KW37")
        CoursesScheduleTable(coursesList = exampleCourses)
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

@Composable
fun CoursesScheduleTable(coursesList: List<CourseDisplay>) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        coursesList.forEach { course ->
            ScheduleRow(course)
        }
    }
}

@Composable
fun ScheduleRow(course: CourseDisplay) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation =  CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = course.block.toString(), style = MaterialTheme.typography.bodyLarge)
            Text(text = course.courseName, style = MaterialTheme.typography.bodyLarge)
            Text(text = course.lecturer, style = MaterialTheme.typography.bodyLarge)
            Text(text = course.classroom, style = MaterialTheme.typography.bodyLarge)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DashboardActivityPreview() {
    WCYedziekanatappTheme {
        DashboardScreen()
    }
}
