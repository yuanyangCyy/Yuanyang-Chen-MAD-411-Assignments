package com.example.habit_tracker_yuanyang_chen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.habit_tracker_yuanyang_chen.ui.theme.HabitTrackerYuanyangChenTheme

// 1. Define a data class to store habit name and completion status
data class Habit(
    val name: String,
    val isCompleted: MutableState<Boolean> = mutableStateOf(false)
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HabitTrackerYuanyangChenTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    HabitTrackerApp(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

// 2. Composable functions moved outside the class (Separation of composables)
@Composable
fun HabitTrackerApp(modifier: Modifier = Modifier) {
    // habitList now stores Habit objects to track the "Completed" state
    val habitList = remember { mutableStateListOf<Habit>() }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        HabitHeader()
        // Next: HabitInputSection() will go here
    }
}

@Composable
fun HabitHeader() {
    Text(
        text = "Student Habit Tracker",
        style = MaterialTheme.typography.headlineMedium,
        modifier = Modifier.padding(bottom = 16.dp)
    )
}