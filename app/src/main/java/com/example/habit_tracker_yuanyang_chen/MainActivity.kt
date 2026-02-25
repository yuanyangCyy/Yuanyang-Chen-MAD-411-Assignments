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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Button
import androidx.compose.foundation.layout.fillMaxWidth

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
fun HabitHeader() {
    Text(
        text = "Student Habit Tracker",
        style = MaterialTheme.typography.headlineMedium,
        modifier = Modifier.padding(bottom = 16.dp)
    )
}

@Composable
fun HabitInputSection(onHabitAdded: (String) -> Unit) {
    var textState by remember { mutableStateOf("") }
    Column(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = textState,
            onValueChange = { textState = it },
            label = { Text("Enter a new habit") },
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = {
                if (textState.isNotBlank()) {
                    onHabitAdded(textState)
                    textState = ""
                }
            },
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            Text("Add Habit")
        }
    }
}


@Composable
fun HabitItem(habit: Habit) {
    // Row makes text and button sit side-by-side
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Display habit name with conditional strike-through
        Text(
            text = habit.name,
            style = if (habit.isCompleted.value) {
                MaterialTheme.typography.bodyLarge.copy(
                    textDecoration = androidx.compose.ui.text.style.TextDecoration.LineThrough
                )
            } else {
                MaterialTheme.typography.bodyLarge
            }
        )

        // Button to toggle the completion state
        Button(onClick = {
            habit.isCompleted.value = !habit.isCompleted.value
        }) {
            Text("Completed")
        }
    }
}
@Composable
fun HabitList(habits: List<Habit>) {
    // LazyColumn efficiently renders only visible items
    LazyColumn {
        items(habits) { habit ->
            // Calls the HabitItem we created earlier
            HabitItem(habit = habit)
        }
    }
}

@Composable
fun HabitTrackerApp(modifier: Modifier = Modifier) {
    // This list tracks all added habits and updates the UI automatically
    val habitList = remember { mutableStateListOf<Habit>() }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // 1. Header
        HabitHeader()

        // 2. Input area: Adds a new Habit object to the list
        HabitInputSection(onHabitAdded = { newHabitName ->
            habitList.add(Habit(name = newHabitName))
        })

        // 3. List area: Displays all habits in the list
        HabitList(habits = habitList)
    }
}