package com.example.evently

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.evently.core.ui.EventlyTheme
import com.example.evently.features.crudevent.presentation.AddEditEventScreen
import com.example.evently.features.home.presentation.HomeScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            EventlyTheme {
                var showAddEvent by remember {
                    mutableStateOf(false)
                }

                if (showAddEvent) {
                    AddEditEventScreen(
                        onBackClick = {
                            showAddEvent = false
                        }
                    )
                } else {
                    HomeScreen(
                        onAddEventClick = {
                            showAddEvent = true
                        }
                    )
                }
            }
        }
    }
}

