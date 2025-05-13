package com.example.edtech.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PersonPin
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.AddComment
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.SupervisorAccount
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.edtech.EdTechAppScreens
import com.example.edtech.EdTechViewModelFactory
import com.example.edtech.FirebaseAuth.User

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun ChatsTopBar(
//    viewModel: TeacherDashboardViewModel,
    navController: NavController
) {
    TopAppBar(
        title = {
            Text(text = "Teacher Chats", fontWeight = FontWeight.Bold)
        },
        colors = TopAppBarDefaults.topAppBarColors(),
        actions = {
            IconButton(onClick = { /* TODO: Open search */ }) {
                Icon(Icons.Default.Search, contentDescription = "Search")
            }
        }
    )
}
@Composable
fun ChatsBottomBar(
    navController: NavController ,
    teacherChatUiState: TeacherChatUiState,
    viewModel: TeacherChatViewModel
) {
    var showMenu by remember { mutableStateOf(false) }
    val menuItems = teacherChatUiState.allUsers

    Column(modifier = Modifier.fillMaxWidth()) {
        NavigationBar(modifier = Modifier.fillMaxWidth()) {
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = if (!showMenu) Icons.Filled.AddComment else Icons.Filled.ArrowDropDown,
                        contentDescription = "Add",
                        modifier = Modifier.scale(1.5f)
                    )
                },
                label = { Text("New Chat") },
                selected = false,
                onClick = { showMenu = !showMenu }
            )
        }

        // Custom dropdown implementation
        if (showMenu) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    menuItems.forEach { item ->
                        if (!viewModel.isThisUserCurrentUser(item)) {
                            MenuCard(
                                user = item , navController = navController , viewModel = viewModel,
                            )
                        }

                        if (item != menuItems.last()) {
                            HorizontalDivider(
                                thickness = 1.dp,
                                color = MaterialTheme.colorScheme.outlineVariant
                            )
                        }
                    }
                }
            }
        }
    }

}

@Composable
fun TeacherChatsScreen(
    viewModel : TeacherChatViewModel = viewModel (factory = EdTechViewModelFactory(LocalContext.current)),
    navController: NavController,
    modifier: Modifier = Modifier,
) {
    val teacherChatUiState by viewModel.teacherChatUiState.collectAsState()

    Scaffold(
        topBar = { ChatsTopBar(navController) },
        bottomBar = { ChatsBottomBar(navController , teacherChatUiState , viewModel) }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .padding(10.dp),

            verticalArrangement = Arrangement.spacedBy(8.dp )
        ) {
            items(teacherChatUiState.allUsers) { item ->
                if (!viewModel.isThisUserCurrentUser(item)) {
                    ChatCard(user = item , navController = navController , viewModel = viewModel)
                }
            }
        }
    }
}


@Composable
fun ChatCard (
    viewModel: TeacherChatViewModel,
    navController: NavController,
    user: User
) {
    Card (
        shape = RoundedCornerShape(50.dp),
        colors = CardDefaults.cardColors(Color.Transparent),
        modifier = Modifier.clickable(
            onClick = {
                viewModel.setSelectedUser(user = user)
                navController.navigate("${EdTechAppScreens.ChatScreen.name}/${user.email}")
            }
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (user.role == "student") {
                Icon(
                    imageVector = Icons.Default.School,
                    contentDescription = "Student",
                    modifier = Modifier.scale(1.5f)
                )
            } else {
                Icon(
                    imageVector = Icons.Default.SupervisorAccount,
                    contentDescription = "Teacher",
                    modifier = Modifier.scale(1.5f)
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(text = user.name, style = MaterialTheme.typography.bodyLarge)
                Text(
                    text = user.email,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Text(
                text = user.role,
                style = MaterialTheme.typography.bodyLarge ,
                fontWeight = FontWeight.Bold,

            )
        }
    }
    Spacer(modifier = Modifier.height(10.dp))
    HorizontalDivider(thickness = 1.dp)
}

@Composable
fun MenuCard(
    user: User ,
    navController: NavController,
    viewModel: TeacherChatViewModel
) {
    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 4.dp)
            .clickable {
                viewModel.setSelectedUser(user = user)
                navController.navigate("${EdTechAppScreens.ChatScreen.name}/${user.email}")
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (user.role == "student") {
                Icon(
                    imageVector = Icons.Default.School,
                    contentDescription = "Student",
                    modifier = Modifier.scale(1.5f)
                )
            } else {
                Icon(
                    imageVector = Icons.Default.SupervisorAccount,
                    contentDescription = "Teacher",
                    modifier = Modifier.scale(1.5f)
                )
            }
            Column {
                Text(text = user.name, style = MaterialTheme.typography.bodyLarge)
                Text(
                    text = user.email,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}