package com.example.wcy_e_dziekanat_app.dashboardActivity.dashboardView

import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController


@Composable
fun TopAppBar(
    title: String,
    logo: Painter,
    isExpanded: MutableState<Boolean>,
    navController: NavController,
    activity: ComponentActivity?
) {
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
                    DropdownMenuItem(trailingIcon = { Icons.Default.Person}, text = { Text("Mój profil") }, onClick = {
                        (!isExpanded.value).also { isExpanded.value = it }
                        navController.navigate("myProfileFragment") })
                    DropdownMenuItem(trailingIcon = { Icons.Default.DateRange}, text = { Text("Wyświetl plan zajęć") }, onClick = {isExpanded.value = !isExpanded.value
                        navController.navigate("fullScheduleFragment") })
                    DropdownMenuItem(trailingIcon = { Icons.Default.List}, text = { Text("Wyświetl skład grupy") }, onClick = {isExpanded.value = !isExpanded.value
                        navController.navigate("deanGroupFragment") })
                    DropdownMenuItem(trailingIcon = { Icons.Default.List}, text = { Text("Wyszukaj studenta") }, onClick = {isExpanded.value = !isExpanded.value
                        navController.navigate("searchStudentFragment") })
                    DropdownMenuItem(trailingIcon = { Icons.Default.List}, text = { Text("Wyszukaj wykładowcę") }, onClick = {isExpanded.value = !isExpanded.value
                        navController.navigate("searchLecturerFragment") })
                    DropdownMenuItem(trailingIcon = { Icons.Default.List}, text = { Text("Wyszukaj przedmiot") }, onClick = {isExpanded.value = !isExpanded.value
                        navController.navigate("searchCourseFragment") })
                    DropdownMenuItem(trailingIcon = { Icons.Default.List}, text = { Text("Wyświetl plan studiów") }, onClick = {isExpanded.value = !isExpanded.value
                        navController.navigate("displayStudiesPlanFragment") })
                    DropdownMenuItem(trailingIcon = { Icons.Default.List}, text = { Text("Informacje o dziekanacie") }, onClick = {isExpanded.value = !isExpanded.value
                        navController.navigate("displayDeanOfficeInfoFragment") })
                    DropdownMenuItem(trailingIcon = { Icons.Default.ExitToApp}, text = { Text("Wyloguj") }, onClick = { activity?.finish() })
                }
            }
        }
    }
}
