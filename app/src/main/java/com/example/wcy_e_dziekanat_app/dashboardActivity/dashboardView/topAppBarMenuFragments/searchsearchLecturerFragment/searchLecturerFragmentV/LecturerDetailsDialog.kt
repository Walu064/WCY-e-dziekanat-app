package com.example.wcy_e_dziekanat_app.dashboardActivity.dashboardView.topAppBarMenuFragments.searchsearchLecturerFragment.searchLecturerFragmentV

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.wcy_e_dziekanat_app.dashboardActivity.dashboardModel.Lecturer

@Composable
fun LecturerDetailsDialog(lecturer: Lecturer?, onDismiss: () -> Unit) {
    if (lecturer != null) {
        Dialog(onDismissRequest = onDismiss) {
            Surface(
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colorScheme.surface
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        "Profil Wykładowcy",
                        style = MaterialTheme.typography.headlineSmall,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                    )

                    DetailRow("Imię: ", lecturer.first_name ?: "Brak danych")
                    DetailRow("Nazwisko: ", lecturer.last_name ?: "Brak danych")
                    DetailRow("Gabinet: ", lecturer.office ?: "Brak danych")
                    DetailRow("Numer telefonu: ", lecturer.telephone ?: "Brak danych")
                    DetailRow("Adres e-mail: ", lecturer.email_address ?: "Brak danych")

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = onDismiss,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Zamknij")
                    }
                }
            }
        }
    }
}

@Composable
fun DetailRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(label, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
        Spacer(Modifier.width(4.dp))
        Text(value, modifier = Modifier.weight(1f))
    }
}
