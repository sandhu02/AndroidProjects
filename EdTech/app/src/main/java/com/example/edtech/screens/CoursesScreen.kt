package com.example.edtech.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoursesScreen(navController: NavController) {
    var selectedFilter by remember { mutableStateOf("All") }
    val filters = listOf("All", "Programming", "Data Science", "Design", "Marketing", "Business", "Language")

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Find Your Next Course",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = "",
                onValueChange = { },
                placeholder = { Text("Search courses...") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = "Search")
                },
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filters) { filter ->
                    FilterChip(
                        selected = selectedFilter == filter,
                        onClick = { selectedFilter = filter },
                        label = { Text(filter) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Popular Courses",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))
        }

        items(5) { index ->
            CourseCard(
                courseId = "$index",
                title = when (index) {
                    0 -> "Introduction to Python Programming"
                    1 -> "Machine Learning Fundamentals"
                    2 -> "UI/UX Design Principles"
                    3 -> "Data Structures & Algorithms"
                    else -> "Web Development with React"
                },
                instructor = when (index) {
                    0 -> "Dr. Sarah Johnson"
                    1 -> "Prof. Michael Chen"
                    2 -> "Emma Wilson"
                    3 -> "James Rodriguez"
                    else -> "Alex Turner"
                },
                rating = 4.5f + (index.toFloat() / 10),
                duration = "${index + 6} weeks",
                imageResourceId = if (index % 2 == 0) android.R.drawable.sym_def_app_icon else android.R.drawable.ic_menu_compass,
                navController = navController
            )
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Trending This Week",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))
        }

        items(3) { index ->
            CourseCard(
                courseId = "${index + 5}",
                title = when (index) {
                    0 -> "Artificial Intelligence Ethics"
                    1 -> "Blockchain Fundamentals"
                    else -> "Digital Marketing Strategies"
                },
                instructor = when (index) {
                    0 -> "Dr. Emily Watson"
                    1 -> "Robert Miller"
                    else -> "Sophia Garcia"
                },
                rating = 4.7f + (index.toFloat() / 10),
                duration = "${index + 4} weeks",
                imageResourceId = if (index % 2 == 0) android.R.drawable.ic_menu_gallery else android.R.drawable.ic_menu_today,
                navController = navController
            )
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "New Releases",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))
        }

        items(2) { index ->
            CourseCard(
                courseId = "${index + 8}",
                title = when (index) {
                    0 -> "Cloud Computing with AWS"
                    else -> "Mobile App Development with Flutter"
                },
                instructor = when (index) {
                    0 -> "Jennifer Brooks"
                    else -> "David Kim"
                },
                rating = 4.8f + (index.toFloat() / 10),
                duration = "${index + 5} weeks",
                imageResourceId = if (index % 2 == 0) android.R.drawable.ic_menu_report_image else android.R.drawable.ic_menu_camera,
                navController = navController
            )
        }
    }
}

@Composable
fun CourseCard(
    courseId: String,
    title: String,
    instructor: String,
    rating: Float,
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

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Rating",
                        tint = Color(0xFFFFA000),
                        modifier = Modifier.size(16.dp)
                    )

                    Text(
                        text = rating.toString(),
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 4.dp)
                    )

                    Text(
                        text = " • $duration",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}