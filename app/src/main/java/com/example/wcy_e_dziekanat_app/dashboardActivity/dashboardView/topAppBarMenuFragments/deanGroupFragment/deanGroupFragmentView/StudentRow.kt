package com.example.wcy_e_dziekanat_app.dashboardActivity.dashboardView.topAppBarMenuFragments.deanGroupFragment.deanGroupFragmentView

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.wcy_e_dziekanat_app.dashboardActivity.dashboardModel.UserOut

@Composable
fun StudentRow(student: UserOut, onStudentClick: (UserOut) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onStudentClick(student) },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "${student.first_name} ${student.second_name}",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "Grupa dziekańska: ${student.dean_group}",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
