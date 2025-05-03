package com.example.edtech.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun MyCoursesScreen(navController: NavController) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("In Progress", "Completed", "Saved")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "My Learning Journey",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = { Text(title) }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (selectedTab) {
            0 -> InProgressCoursesList(navController)
            1 -> CompletedCoursesList(navController)
            2 -> SavedCoursesList(navController)
        }
    }
}

@Composable
fun InProgressCoursesList(navController: NavController) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(3) { index ->
            EnrolledCourseCard(
                courseId = "$index",
                title = when (index) {
                    0 -> "Introduction to Python Programming"
                    1 -> "Data Structures & Algorithms"
                    else -> "UI/UX Design Principles"
                },
                progress = when (index) {
                    0 -> 0.75f
                    1 -> 0.45f
                    else -> 0.30f
                },
                imageResourceId = when (index) {
                    0 -> android.R.drawable.sym_def_app_icon
                    1 -> android.R.drawable.ic_menu_compass
                    else -> android.R.drawable.ic_menu_gallery
                },
                lastLesson = when (index) {
                    0 -> "Functions and Classes"
                    1 -> "Binary Trees"
                    else -> "User Research Methods"
                },
                navController = navController
            )
        }
    }
}

@Composable
fun CompletedCoursesList(navController: NavController) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(2) { index ->
            EnrolledCourseCard(
                courseId = "${index + 3}",
                title = when (index) {
                    0 -> "Web Development Fundamentals"
                    else -> "Digital Marketing Basics"
                },
                progress = 1.0f,
                imageResourceId = when (index) {
                    0 -> android.R.drawable.ic_menu_report_image
                    else -> android.R.drawable.ic_menu_camera
                },
                lastLesson = "Course Completed",
                navController = navController
            )
        }
    }
}

@Composable
fun SavedCoursesList(navController: NavController) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(2) { index ->
            SavedCourseCard(
                courseId = "${index + 5}",
                title = when (index) {
                    0 -> "Artificial Intelligence Ethics"
                    else -> "Blockchain Fundamentals"
                },
                instructor = when (index) {
                    0 -> "Dr. Emily Watson"
                    else -> "Robert Miller"
                },
                duration = when (index) {
                    0 -> "6 weeks"
                    else -> "8 weeks"
                },
                imageResourceId = when (index) {
                    0 -> android.R.drawable.ic_menu_today
                    else -> android.R.drawable.sym_def_app_icon
                },
                navController = navController
            )
        }
    }
}

@Composable
fun EnrolledCourseCard(
    courseId: String,
    title: String,
    progress: Float,
    imageResourceId: Int,
    lastLesson: String,
    navController: NavController
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { navController.navigate("course_detail/$courseId") }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.LightGray)
            ) {
                Image(
                    painter = painterResource(id = imageResourceId),
                    contentDescription = title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                LinearProgressIndicator(
                    progress = progress,
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (progress < 1.0f) {
                        Icon(
                            imageVector = Icons.Default.AccessTime,
                            contentDescription = "Last Lesson",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(16.dp)
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Completed",
                            tint = Color(0xFF4CAF50),
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    Text(
                        text = if (progress < 1.0f) "Last: $lastLesson" else lastLesson,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }

                Text(
                    text = "${(progress * 100).toInt()}% Complete",
                    style = MaterialTheme.typography.bodySmall,
                    color = if (progress < 1.0f) MaterialTheme.colorScheme.primary else Color(0xFF4CAF50)
                )
            }
        }
    }
}

@Composable
fun SavedCourseCard(
    courseId: String,
    title: String,
    instructor: String,
    duration: String,
    imageResourceId: Int,
    navController: NavController
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { navController.navigate("course_detail/$courseId") }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.LightGray)
            ) {
                Image(
                    painter = painterResource(id = imageResourceId),
                    contentDescription = title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = "By $instructor",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = { /* Enroll in course */ },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Enroll")
                }
            }
        }
    }
}