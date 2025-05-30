package com.example.edtech.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.PersonOutline
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.edtech.EdTechViewModelFactory
import com.example.edtech.firebase.ChatMessage

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun ChatTopBar(
    navController: NavController,
    teacherChatUiState: TeacherChatUiState
) {
    TopAppBar(
        title = {
            Row (
                verticalAlignment = Alignment.CenterVertically ,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Default.ArrowBackIosNew, contentDescription = "go back")
                }
                Text(
                    text = if (teacherChatUiState.selectedUser !=null) teacherChatUiState.selectedUser.name else "Null" ,
                    fontWeight = FontWeight.Bold ,
                    fontSize = 20.sp
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(),
        actions = {
            IconButton(onClick = {  }) {
                Icon(Icons.Default.PersonOutline, contentDescription = "Profile pic")
            }
            IconButton(onClick = {  }) {
                Icon(Icons.Default.Call, contentDescription = "Call")
            }
        }
    )
}

@Composable
fun ChatScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    userEmail: String?,
    viewModel : TeacherChatViewModel = viewModel (),
) {
    val teacherChatUiState by viewModel.teacherChatUiState.collectAsState()
    val chatUiState by viewModel.chatUitate.collectAsState()

    LaunchedEffect(userEmail, teacherChatUiState.allUsers) {
        if (userEmail != null && teacherChatUiState.selectedUser == null) {
            teacherChatUiState.allUsers.firstOrNull { it.email == userEmail }?.let { user ->
                viewModel.setSelectedUser(user)
                viewModel.loadMessages()
            }
        }
    }

    Scaffold(
        topBar = { ChatTopBar(navController , teacherChatUiState) },
        bottomBar = {
            NavigationBar {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = chatUiState.messageField,
                        onValueChange = { viewModel.updateMessageField(it) },
                        modifier = Modifier.weight(1f),
                        placeholder = { Text("Type a message") },
                        shape = RoundedCornerShape(50.dp)
                    )
                    IconButton(onClick = {
                        viewModel.sendMessage()
                        viewModel.updateMessageField("")
                    }) {
                        Icon(
                            imageVector = Icons.Default.Send,
                            contentDescription = "Send",
                            modifier = Modifier.scale(1.5f)
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        if (chatUiState.isLoading) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding).padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                CircularProgressIndicator()
            }
        }
        else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding).padding(16.dp),
                verticalArrangement = Arrangement.Top
            ) {
                LazyColumn () {
                    items(chatUiState.chatMessages) { message ->
                        ChatBubble(message , viewModel , chatUiState)
                    }
                }
            }
        }
    }
}

@Composable
fun ChatBubble(message: ChatMessage, viewModel: TeacherChatViewModel , chatUiState: ChatUiState) {
    val bubbleColor = if (message.senderId == chatUiState.senderId) Color(0xFFDCF8C6) else Color(0xFFFFFFFF)
    val alignment = if (message.senderId == chatUiState.senderId) Alignment.End else Alignment.Start

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalArrangement = if (message.senderId == chatUiState.senderId) Arrangement.End else Arrangement.Start
    ) {
        Column(
            modifier = Modifier
                .background(bubbleColor, shape = RoundedCornerShape(8.dp))
                .padding(8.dp)
        ) {
            Text(text = message.text)
            Text(
                text = viewModel.formatTimeFromMillis(message.timestamp),
                color = Color.Gray,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.align(Alignment.End)
            )
        }
    }
}
