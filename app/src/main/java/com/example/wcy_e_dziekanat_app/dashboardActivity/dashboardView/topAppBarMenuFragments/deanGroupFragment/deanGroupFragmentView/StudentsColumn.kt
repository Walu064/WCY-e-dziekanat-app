package com.example.wcy_e_dziekanat_app.dashboardActivity.dashboardView.topAppBarMenuFragments.deanGroupFragment.deanGroupFragmentView

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.wcy_e_dziekanat_app.dashboardActivity.dashboardModel.UserOut

@Composable
fun StudentsColumn(students: List<UserOut>) {
    LazyColumn(
        modifier = Modifier.padding(16.dp)
    ) {
        items(students) { student ->
            StudentRow(student)
        }
    }
}