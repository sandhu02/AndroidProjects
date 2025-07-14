package com.example.edtech.screens.teacherScreens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.edtech.EdTechViewModelFactory
import com.example.edtech.model.Course

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeacherCoursesScreen(
    navController : NavController,
    viewModel: TeacherCoursesViewModel = viewModel(factory = EdTechViewModelFactory(LocalContext.current))
) {
    val teacherCourseUiState by viewModel.uiState.collectAsState()

    Scaffold (
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "My Courses", style = MaterialTheme.typography.displayLarge,
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {

                }
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.AddCircleOutline , contentDescription = "Add") },
                    label = { Text("Add Course") },
                    selected = true,
                    onClick = {  }
                )
            }
        },

    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding),
        ) {
            if (teacherCourseUiState.isLoading){
                CircularProgressIndicator()
            }
            else {
                LazyRow {
                    items(teacherCourseUiState.courses) { TeacherCourseCard(it) }
                }
            }
        }
    }
}

@Composable
fun TeacherCourseCard(course : Course ){
    Card(
        modifier = Modifier
            .padding(12.dp)
            .fillMaxHeight()
            .width(380.dp),
        shape = RoundedCornerShape(25.dp),
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = course.title , style = MaterialTheme.typography.displayMedium
            )
            Text(
                text = course.author , style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = course.description , style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "${course.rating} / 10" , style = MaterialTheme.typography.displayMedium
            )
        }
    }
}
