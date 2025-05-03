package com.example.edtech.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import com.example.edtech.EdTechAppScreens
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.edtech.EdTechViewModelFactory


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeachertopBar(
    viewModel: TeacherDashboardViewModel,
    navController: NavController
) {
    TopAppBar(
        title = {
            Text(text = "Dashboard", fontWeight = FontWeight.Bold)
        },
        colors = TopAppBarDefaults.topAppBarColors(),
        actions = {
            IconButton(onClick = { /* TODO: Open search */ }) {
                Icon(Icons.Default.Search, contentDescription = "Search")
            }
            IconButton(onClick = { /* TODO: Open notifications */ }) {
                Icon(Icons.Default.Notifications, contentDescription = "Notifications")
            }
            IconButton(onClick = {
                viewModel.logout()
            }) {
                Icon(Icons.Default.PowerSettingsNew, contentDescription = "Sign Out")
            }
        }
    )
}

@Composable
fun teacherBottomBar(navController: NavController) {
    NavigationBar {
        NavigationBarItem(
            icon = { Icon(Icons.Filled.ChatBubble , contentDescription = "Chats") },
            label = { Text("Chats") },
            selected = true,
            onClick = { navController.navigate(EdTechAppScreens.TeacherChats.name) }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.AddCircleOutline , contentDescription = "Add") },
            label = { Text("Add Course") },
            selected = true,
            onClick = {  }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.AccountCircle , contentDescription = "Profile") },
            label = { Text("Profile") },
            selected = true,
            onClick = {  }
        )
    }
}

@Composable
fun TeacherDashboard(
    viewModel : TeacherDashboardViewModel = viewModel (
        factory = EdTechViewModelFactory (LocalContext.current)
    ),
    navController: NavController
) {
    val signOutUiState by viewModel.signOutUiState.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.navigateToSignIn.collect {
            navController.navigate(EdTechAppScreens.SignIn.name){
                popUpTo(EdTechAppScreens.TeacherDashboard.name){inclusive = true}
            }
        }
    }

    Scaffold (
        topBar = { TeachertopBar(viewModel , navController) },

        bottomBar = { teacherBottomBar(navController) }
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.fillMaxSize().padding(innerPadding),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Teacher Dashboard")
            }
            if (signOutUiState.isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.4f))
                        .clickable(enabled = true, onClick = {}), // block interaction
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }

    }
}