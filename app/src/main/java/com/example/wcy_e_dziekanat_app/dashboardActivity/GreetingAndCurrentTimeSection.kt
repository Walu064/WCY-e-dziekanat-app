package com.example.wcy_e_dziekanat_app.dashboardActivity

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


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