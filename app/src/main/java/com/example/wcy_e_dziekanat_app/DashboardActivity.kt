package com.example.wcy_e_dziekanat_app
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.wcy_e_dziekanat_app.ui.theme.WCYedziekanatappTheme

class DashboardActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WCYedziekanatappTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TopPanel(
                        logoPainter = painterResource(id = R.drawable.wcy_old_logo),
                        title = "e-Dziekanat WCY"
                    )

                }
            }
        }
    }

    @Composable
    fun TopPanel(logoPainter: Painter, title: String) {
        var isMenuExpanded by remember { mutableStateOf(false) }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Logo
            Image(
                painter = logoPainter,
                contentDescription = "Logo Wydziału",
                modifier = Modifier.size(50.dp)
            )

            Text(text = title, style = MaterialTheme.typography.headlineMedium)

            Box {
                IconButton(onClick = { isMenuExpanded = true }) {
                    Icon(Icons.Default.Menu, contentDescription = "Menu")
                }

                DropdownMenu(
                    expanded = isMenuExpanded,
                    onDismissRequest = { isMenuExpanded = false }
                ) {
                    DropdownMenuItem(text = { /*TODO: Wyświetl pełny plan zajęć*/ }, onClick = { /*TODO: Obsługa do planu zajęć */ })
                    DropdownMenuItem(text = { /*TODO: Wyświetl grupę dziekańską*/ }, onClick = { /*TODO: Obsługa do planu*/ })
                    DropdownMenuItem(text = { /*TODO: Wyszukaj...*/ }, onClick = { /*TODO: Dorobić obsługę  */ })
                }
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun LoginActivityPreview() {
        WCYedziekanatappTheme {
            TopPanel(logoPainter = painterResource(id = R.drawable.wcy_old_logo),
                title = "e-Dziekanat WCY")
        }
    }
}