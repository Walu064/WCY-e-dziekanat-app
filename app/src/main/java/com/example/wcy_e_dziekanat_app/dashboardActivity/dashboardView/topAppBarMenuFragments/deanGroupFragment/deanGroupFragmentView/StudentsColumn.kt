package com.example.wcy_e_dziekanat_app.dashboardActivity.dashboardView.topAppBarMenuFragments.deanGroupFragment.deanGroupFragmentView

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.wcy_e_dziekanat_app.dashboardActivity.dashboardModel.UserOut
import com.example.wcy_e_dziekanat_app.dashboardActivity.dashboardView.topAppBarMenuFragments.searchStudentFragment.searchStudentFragmentView.UserDetailsDialog

@Composable
fun StudentsColumn(students: List<UserOut>) {
    var showDialog by remember { mutableStateOf(false) }
    var selectedStudent by remember { mutableStateOf<UserOut?>(null) }

    LazyColumn(
        modifier = Modifier.padding(16.dp)
    ) {
        items(students) { student ->
            StudentRow(student) {
                selectedStudent = student
                showDialog = true
            }
        }
    }

    if (showDialog && selectedStudent != null) {
        UserDetailsDialog(user = selectedStudent, onDismiss = {
            showDialog = false
            selectedStudent = null
        })
    }
}