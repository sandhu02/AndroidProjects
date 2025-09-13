package com.example.cusotmauthtest.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.cusotmauthtest.AuthTestViewModelFactory


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    navController: NavController,
    viewModel: ChatViewModel = viewModel(factory = AuthTestViewModelFactory(context = LocalContext.current)),
    sharedVideoViewModel: SharedVideoViewModel
) {
    val recipientUser by sharedVideoViewModel.chatRecipientUser.collectAsState()

    DisposableEffect(Unit) {
        onDispose {
            viewModel.disconnect()
        }
    }

    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {Text("Chat - ${recipientUser?.name}")},
            )
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
                    .navigationBarsPadding()
                    .imePadding(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    modifier = Modifier.weight(1f),
                    value = uiState.senderMessage,
                    onValueChange = { viewModel.updateSenderMessage(it) },
                    placeholder = { Text("Type a message") }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = {
                    viewModel.sendMessage(uiState.senderMessage , toUserName = recipientUser?.username ?: "null")
                    viewModel.updateSenderMessage("")
                }) {
                    Text("Send")
                }
            }
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            Row(horizontalArrangement = Arrangement.Center , modifier = Modifier.fillMaxWidth()) {
                Text(uiState.connectionMessage , color = Color.Green)
            }
            // Messages list
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(12.dp),

            ) {
                items(uiState.receivedMessageList) { message ->
                    Text(
                        text = message,
                        modifier = Modifier
                            .padding(4.dp)
                    )
                }
            }
        }
    }
}