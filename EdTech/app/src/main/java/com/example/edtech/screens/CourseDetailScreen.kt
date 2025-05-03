package com.example.edtech.screens


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseDetailScreen(
    courseId: String,
    navController: NavController
) {
    // Mock data based on courseId
    val courseTitle = when (courseId.toIntOrNull()) {
        0 -> "Introduction to Python Programming"
        1 -> "Machine Learning Fundamentals"
        2 -> "UI/UX Design Principles"
        3 -> "Data Structures & Algorithms"
        4 -> "Web Development with React"
        else -> "Course $courseId"
    }

    val instructor = when (courseId.toIntOrNull()) {
        0 -> "Dr. Sarah Johnson"
        1 -> "Prof. Michael Chen"
        2 -> "Emma Wilson"
        3 -> "James Rodriguez"
        4 -> "Alex Turner"
        else -> "Instructor"
    }

    val description = "This comprehensive course is designed to give you a solid foundation in ${courseTitle.lowercase()}. You'll learn through hands-on projects and real-world examples."

    var isEnrolled by remember { mutableStateOf(courseId.toIntOrNull() in 0..2) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Course Details") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { /* TODO: Bookmark/Save course */ }) {
                        Icon(Icons.Default.BookmarkBorder, contentDescription = "Save")
                    }
                    IconButton(onClick = { /* TODO: Share course */ }) {
                        Icon(Icons.Default.Share, contentDescription = "Share")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Course Header
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .background(MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Image(
                        painter = painterResource(id = android.R.drawable.sym_def_app_icon),
                        contentDescription = courseTitle,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0x88000000))
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.Bottom
                    ) {
                        Text(
                            text = courseTitle,
                            style = MaterialTheme.typography.headlineSmall,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "By $instructor",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White
                        )
                    }
                }
            }

            // Course Info
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        InfoItem(
                            icon = Icons.Default.Star,
                            text = "4.8",
                            subtext = "Rating"
                        )

                        InfoItem(
                            icon = Icons.Default.People,
                            text = "${(1000..5000).random()}",
                            subtext = "Students"
                        )

                        InfoItem(
                            icon = Icons.Default.AccessTime,
                            text = "${(4..12).random()} weeks",
                            subtext = "Duration"
                        )

                        InfoItem(
                            icon = Icons.Default.PlayLesson,
                            text = "${(10..30).random()}",
                            subtext = "Lessons"
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    if (isEnrolled) {
                        Button(
                            onClick = { navController.navigate("lesson/$courseId/1") },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Continue Learning")
                        }
                    } else {
                        Button(
                            onClick = { isEnrolled = true },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Enroll Now")
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "About This Course",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodyLarge
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "What You'll Learn",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            // What You'll Learn
            items(5) { index ->
                LearningObjectiveItem(
                    text = when (index) {
                        0 -> "Master the fundamentals of ${courseTitle.lowercase()}"
                        1 -> "Build real-world projects to apply your knowledge"
                        2 -> "Understand key concepts and best practices"
                        3 -> "Learn industry-standard tools and frameworks"
                        else -> "Gain skills that directly apply to your career goals"
                    }
                )
            }

            // Curriculum Section
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Course Curriculum",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            // Curriculum Items
            items(8) { index ->
                CurriculumItem(
                    moduleNumber = index + 1,
                    title = when (index) {
                        0 -> "Getting Started"
                        1 -> "Core Concepts"
                        2 -> "Intermediate Techniques"
                        3 -> "Advanced Topics"
                        4 -> "Practical Applications"
                        5 -> "Project Development"
                        6 -> "Best Practices"
                        else -> "Final Project"
                    },
                    duration = "${(20..60).random()} min",
                    isUnlocked = index < 3 || isEnrolled,
                    onClick = {
                        if (isEnrolled || index < 2) {
                            navController.navigate("lesson/$courseId/${index + 1}")
                        }
                    }
                )
            }

            // Instructor Section
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Your Instructor",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = android.R.drawable.ic_menu_gallery),
                            contentDescription = instructor,
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )

                        Spacer(modifier = Modifier.width(16.dp))

                        Column {
                            Text(
                                text = instructor,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )

                            Text(
                                text = "Expert in ${courseTitle.split(" ").take(3).joinToString(" ")}",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "$instructor is a seasoned professional with over ${(5..15).random()} years of experience in the field. They are known for their practical teaching approach and ability to explain complex concepts clearly.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            // Reviews Section
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Student Reviews",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            // Review Items
            items(3) { index ->
                ReviewItem(
                    name = when (index) {
                        0 -> "Maria Garcia"
                        1 -> "David Kim"
                        else -> "Jessica Thompson"
                    },
                    rating = (4..5).random(),
                    review = when (index) {
                        0 -> "This course exceeded my expectations. The instructor explains everything clearly and the projects helped solidify my understanding."
                        1 -> "Very practical approach to learning. I was able to apply these skills immediately in my job."
                        else -> "Well-structured content with a good balance of theory and practice. Highly recommended for beginners."
                    }
                )
            }

            // Related Courses Section
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Related Courses",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        for (i in 0..4) {
                            if (i.toString() != courseId) {
                                RelatedCourseItem(
                                    title = when (i) {
                                        0 -> "Introduction to Python Programming"
                                        1 -> "Machine Learning Fundamentals"
                                        2 -> "UI/UX Design Principles"
                                        3 -> "Data Structures & Algorithms"
                                        else -> "Web Development with React"
                                    },
                                    instructor = when (i) {
                                        0 -> "Dr. Sarah Johnson"
                                        1 -> "Prof. Michael Chen"
                                        2 -> "Emma Wilson"
                                        3 -> "James Rodriguez"
                                        else -> "Alex Turner"
                                    },
                                    onClick = { navController.navigate("course/$i") }
                                )
                            }
                        }
                    }
                }
            }

            // Bottom Spacer
            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}

@Composable
fun InfoItem(
    icon: ImageVector,
    text: String,
    subtext: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = subtext,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun LearningObjectiveItem(text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            imageVector = Icons.Default.Check,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(top = 2.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
fun CurriculumItem(
    moduleNumber: Int,
    title: String,
    duration: String,
    isUnlocked: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (isUnlocked)
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
    else
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f)

    val textColor = if (isUnlocked)
        MaterialTheme.colorScheme.onSurface
    else
        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clickable(enabled = isUnlocked, onClick = onClick),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(
                        if (isUnlocked) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = moduleNumber.toString(),
                    color = Color.White,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Module $moduleNumber: $title",
                    style = MaterialTheme.typography.titleMedium,
                    color = textColor,
                    fontWeight = FontWeight.Medium
                )

                Text(
                    text = duration,
                    style = MaterialTheme.typography.bodySmall,
                    color = textColor.copy(alpha = 0.7f)
                )
            }

            Icon(
                imageVector = if (isUnlocked) Icons.Default.PlayCircle else Icons.Default.Lock,
                contentDescription = if (isUnlocked) "Play" else "Locked",
                tint = if (isUnlocked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )
        }
    }
}

@Composable
fun ReviewItem(
    name: String,
    rating: Int,
    review: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = android.R.drawable.ic_menu_gallery),
                    contentDescription = name,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = name,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Medium
                    )

                    Row {
                        repeat(5) { index ->
                            Icon(
                                imageVector = if (index < rating) Icons.Default.Star else Icons.Default.StarBorder,
                                contentDescription = null,
                                tint = if (index < rating) Color(0xFFFFC107) else Color.Gray,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }

                Text(
                    text = "${(1..12).random()} days ago",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = review,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun RelatedCourseItem(
    title: String,
    instructor: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(200.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(MaterialTheme.colorScheme.primaryContainer)
            ) {
                Image(
                    painter = painterResource(id = android.R.drawable.sym_def_app_icon),
                    contentDescription = title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Medium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = instructor,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = Color(0xFFFFC107),
                        modifier = Modifier.size(16.dp)
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = "${(4..5).random().toString().take(3)}",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Medium
                    )

                    Text(
                        text = " (${(100..999).random()})",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}