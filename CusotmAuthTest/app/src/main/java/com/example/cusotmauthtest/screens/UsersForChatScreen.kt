package com.example.cusotmauthtest.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.cusotmauthtest.AuthTestScreens
import com.example.cusotmauthtest.AuthTestViewModelFactory
import com.example.cusotmauthtest.retrofit.User

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsersForChatScreen(
    navController: NavController,
    viewModel: UsersForChatViewModel = viewModel(factory = AuthTestViewModelFactory(context = LocalContext.current)),
    sharedViewModel : SharedVideoViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    Scaffold(
        topBar = {
            TopAppBar(
                title = {Text(text = "Users") },
            )
        },
    ){ innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            when (val response = uiState.usersResponse) {
                is UsersResponse.Failure -> {
                    Text(
                        text = response.error,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(16.dp)
                    )
                }
                is UsersResponse.Success -> {
                    val users = response.usersList
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = if (users.isEmpty()) {
                            Arrangement.Center
                        } else {
                            Arrangement.Top
                        }
                    ){
                        items(users) { user ->
                            UserCard(
                                navController = navController,
                                user = user,
                                sharedVideoViewModel = sharedViewModel
                            )
                        }
                    }
                }
                is UsersResponse.Loading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                null -> {}
            }
        }
    }
}

@Composable
fun UserCard(
    navController: NavController ,
    sharedVideoViewModel: SharedVideoViewModel,
    user: User
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        onClick = {
            sharedVideoViewModel.setChatUser(user = user)
            navController.navigate(AuthTestScreens.ChatScreen.name)
        }
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(text = user.username)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = user.name)
        }
    }
}
