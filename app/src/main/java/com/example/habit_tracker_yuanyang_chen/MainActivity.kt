package com.example.habit_tracker_yuanyang_chen

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.foundation.layout.height
import com.example.habit_tracker_yuanyang_chen.ui.theme.HabitTrackerYuanyangChenTheme

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
    var textState by rememberSaveable { mutableStateOf("") }

    // Align items horizontally
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
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
fun HabitItem(habit: Habit, onDelete: () -> Unit, onViewDetails: () -> Unit) {
    // Row makes text and button sit side-by-side
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        // Display habit name with conditional color
        // if complete change color
        Text(
            text = habit.name,
            modifier = Modifier.weight(1f),
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

        // View Details Button
        Button(onClick = { onViewDetails() }) {
            Text("Details")
        }

        Spacer(modifier = Modifier.width(4.dp))

        // add Delete Button
        Button(onClick = { onDelete() }) {
            Text("Delete")
        }
    }
}





@Composable
fun HabitList(
    habits: List<Habit>,
    onDeleteHabit: (Habit) -> Unit,
    onViewDetails: (Habit) -> Unit // Added onViewDetails parameter
) {
    // lazyColumn efficiently renders only visible items
    LazyColumn {
        items(habits) { habit ->
            // call the HabitItem we created earlier
            HabitItem(
                habit = habit,
                onDelete = { onDeleteHabit(habit) },
                onViewDetails = { onViewDetails(habit) } // Pass the habit to the detail button
            )
        }
    }
}



@Composable
fun HabitDetailScreen(habitName: String, isCompleted: Boolean, navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Habit Name: $habitName", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Status: ${if (isCompleted) "Completed" else "Not Completed"}")
        Spacer(modifier = Modifier.height(32.dp))

        Button(onClick = { navController.popBackStack() }) {
            Text("Back")
        }
    }
}


@Composable
fun MainScreen(
    habitList: MutableList<Habit>,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val context = androidx.compose.ui.platform.LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        HabitHeader()

        Button(
            onClick = {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com"))
                context.startActivity(intent)
            },
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            Text("Open Youtube")
        }

        HabitInputSection(onHabitAdded = { newHabitName ->
            habitList.add(Habit(id = nextId++, name = newHabitName))
        })

        HabitList(
            habits = habitList,
            onDeleteHabit = { habit -> habitList.remove(habit) },
            onViewDetails = { habit ->
                // Navigate to detail route with habit name and completion status
                navController.navigate("detail/${habit.name}/${habit.isCompleted.value}")
            }
        )
    }
}

@Composable
fun HabitTrackerApp(modifier: Modifier = Modifier) {

    val habitList = remember { mutableStateListOf<Habit>() }
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "main") {


        composable("main") {
            MainScreen(habitList = habitList, navController = navController, modifier = modifier)
        }


        composable("detail/{habitName}/{status}") { backStackEntry ->

            val habitName = backStackEntry.arguments?.getString("habitName") ?: "Unknown"
            val statusString = backStackEntry.arguments?.getString("status") ?: "false"

            HabitDetailScreen(
                habitName = habitName,
                isCompleted = statusString.toBoolean(),
                navController = navController
            )
        }
    }
}