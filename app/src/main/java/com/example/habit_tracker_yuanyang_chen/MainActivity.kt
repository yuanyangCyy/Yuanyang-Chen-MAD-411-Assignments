package com.example.habit_tracker_yuanyang_chen
import android.os.Bundle
import android.widget.Toast
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
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import android.util.Log
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items

import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.ui.Alignment

// define a data class to store habit name and completion status
data class Habit(
    val id: Int,
    val name: String,
    val isCompleted: MutableState<Boolean> = mutableStateOf(false)
)

var nextId = 0

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 1. Add Log to onCreate
        Log.d("LifecycleLog", "onCreate called")

        enableEdgeToEdge()
        setContent {
            HabitTrackerYuanyangChenTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    HabitTrackerApp(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }

    // 2. Override and log the remaining 5 lifecycle methods


override fun onStart() {
    super.onStart()
    Log.d("LifecycleLog", "onStart called")
}

override fun onResume() {
    super.onResume()
    Log.d("LifecycleLog", "onResume called")
}

override fun onPause() {
    super.onPause()
    Log.d("LifecycleLog", "onPause called")
}

override fun onStop() {
    super.onStop()
    Log.d("LifecycleLog", "onStop called")
}

override fun onDestroy() {
    super.onDestroy()
    Log.d("LifecycleLog", "onDestroy called")
}
}






// composable functions moved outside the class
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


    // context needed for Toast
    val context = androidx.compose.ui.platform.LocalContext.current


    // State for text input
    var textState by remember { mutableStateOf("") }

    // Align items horizontally
    Row(
        modifier = Modifier.fillMaxWidth().padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {



        // text field with label
        OutlinedTextField(
            value = textState,
            onValueChange = { textState = it },
            label = { Text("Enter a new habit") },
            modifier = Modifier.weight(1f) // Takes up remaining space
        )

        // Space between input and button
        Spacer(modifier = Modifier.width(8.dp))

        //  FAB  button
        FloatingActionButton(
            onClick = {
                if (textState.isNotBlank()) {
                    onHabitAdded(textState)
                   Toast.makeText(context,
                        "Habit Added！",
                        Toast.LENGTH_LONG)
                        .show()
                    textState = "" // Clear input after adding
                }
            }
        ) {
            //  icon  the FAB
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add"
            )
        }
    }
}



@Composable
fun HabitItem(habit: Habit,  onDelete: ( ) -> Unit) {  //add onDele
    // Row makes text and button sit side-by-side
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
    ) {




        // Display habit name with conditional
        // Display habit name with conditional color
        Text(
            text = habit.name,
            modifier = Modifier.weight(1f),
           // if complete change color
            color = if (habit.isCompleted.value) Color.Red else Color.Unspecified,
            style = MaterialTheme.typography.bodyLarge
        )





        // Button to toggle the completion state
        Button(onClick = {
            habit.isCompleted.value = !habit.isCompleted.value
        }) {
            Text("Completed")
        }


        Spacer(modifier = Modifier.width(4.dp))


        // add  Delete Button
        Button(onClick = { onDelete() }) {
            Text("Delete")
        }
    }
}






@Composable
fun HabitList(habits: List<Habit>, onDeleteHabit: (Habit) -> Unit) {
    // lazyColumn efficiently renders only visible items
    LazyColumn {
        items(habits) { habit ->
            // call the HabitItem we created earlier
            HabitItem(
                habit = habit,
                onDelete = { onDeleteHabit(habit) })
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

        // adds a new Habit object to the list
        HabitInputSection(onHabitAdded = { newHabitName ->
            habitList.add(Habit(id = nextId++,name = newHabitName))
        })

        // displays all habits in the list
        HabitList(habits = habitList,
            onDeleteHabit = { habit -> habitList.remove(habit) })  // add delect action
    }
}