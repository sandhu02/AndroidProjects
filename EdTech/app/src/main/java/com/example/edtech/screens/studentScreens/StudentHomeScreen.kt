package com.example.edtech.screens.studentScreens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import android.util.Log

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentHomeScreen(navController: NavController) {
    // Add debugging to track lifecycle
    Log.d("StudentHomeScreen", "Composition started")

    // Track the current tab with remember to preserve state across recompositions
    var currentTab by remember { mutableStateOf("courses") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = when (currentTab) {
                            "courses" -> "Explore Courses"
                            "my_courses" -> "My Learning"
                            "flashcards" -> "My Flashcards"
                            "discussions" -> "Discussions"
                            "profile" -> "My Profile"
                            else -> "EdTech Platform"
                        },
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    IconButton(onClick = { /* TODO: Open search */ }) {
                        Icon(Icons.Default.Search, contentDescription = "Search")
                    }
                    IconButton(onClick = { /* TODO: Open notifications */ }) {
                        Icon(Icons.Default.Notifications, contentDescription = "Notifications")
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { Icon(if (currentTab == "courses") Icons.Filled.Explore else Icons.Outlined.Explore, contentDescription = "Explore") },
                    label = { Text("Explore") },
                    selected = currentTab == "courses",
                    onClick = { currentTab = "courses" }
                )
                NavigationBarItem(
                    icon = { Icon(if (currentTab == "my_courses") Icons.Filled.MenuBook else Icons.Outlined.MenuBook, contentDescription = "My Learning") },
                    label = { Text("My Learning") },
                    selected = currentTab == "my_courses",
                    onClick = { currentTab = "my_courses" }
                )
                NavigationBarItem(
                    icon = { Icon(if (currentTab == "flashcards") Icons.Filled.Layers else Icons.Outlined.Layers, contentDescription = "Flashcards") },
                    label = { Text("Flashcards") },
                    selected = currentTab == "flashcards",
                    onClick = { currentTab = "flashcards" }
                )
                NavigationBarItem(
                    icon = { Icon(if (currentTab == "discussions") Icons.Filled.Forum else Icons.Outlined.Forum, contentDescription = "Discussions") },
                    label = { Text("Discussions") },
                    selected = currentTab == "discussions",
                    onClick = { currentTab = "discussions" }
                )
                NavigationBarItem(
                    icon = { Icon(if (currentTab == "profile") Icons.Filled.Person else Icons.Outlined.Person, contentDescription = "Profile") },
                    label = { Text("Profile") },
                    selected = currentTab == "profile",
                    onClick = { currentTab = "profile" }
                )
            }
        }
    ) { paddingValues ->
        // Content based on current tab with debugging measurements
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            // Use remember for content to avoid recreating on every recomposition
            val content by remember(currentTab) {
                mutableStateOf(
                    when (currentTab) {
                        "courses" -> { @Composable { PlaceholderScreen("Explore Courses") } }
                        "my_courses" -> { { PlaceholderScreen("My Learning") } }
                        "flashcards" -> { { FlashcardsScreen(navController) } }
                        "discussions" -> { { DiscussionsScreen(navController) } }
                        "profile" -> { { ProfileScreen(navController) } }
                        else -> { { PlaceholderScreen("Unknown Tab") } }
                    }
                )
            }

            // Execute the content lambda
            content()
        }
    }

    // Debug end of composition
    Log.d("StudentHomeScreen", "Composition completed")
}

// Use this placeholder screen while you debug the CoursesScreen and MyCoursesScreen
@Composable
fun PlaceholderScreen(name: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "$name",
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Content coming soon",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Composable
fun FlashcardsScreen(navController: NavController) {
    // Placeholder for FlashcardsScreen
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Flashcards Screen - Coming Soon")
    }
}

@Composable
fun DiscussionsScreen(navController: NavController) {
    // Placeholder for DiscussionsScreen
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Discussions Screen - Coming Soon")
    }
}

@Composable
fun ProfileScreen(navController: NavController) {
    // Placeholder for ProfileScreen
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Profile Screen - Coming Soon")
    }
}