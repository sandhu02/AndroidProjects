package com.example.roomtest

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.roomtest.room.User
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: MainViewModel = MainViewModel(LocalContext.current)
) {
    val uiState by viewModel.uiState.collectAsState()
    val pullRefreshState = rememberPullToRefreshState()
    var refreshing by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(title = {Text("Users")})
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = true,
                    onClick = { viewModel.showDialog() },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Add ,
                            contentDescription = "Create User"
                        )
                    },
                    label = { Text("Create User") }
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding) ,
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (uiState.isLoading){
                Column(
                    modifier = Modifier.fillMaxSize() ,
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    CircularProgressIndicator()
                }
            }
            else{
                PullToRefreshBox(
                    state = pullRefreshState,
                    isRefreshing = refreshing,
                    onRefresh = {
                        refreshing = true
                        viewModel.getUsers()
                        refreshing = false
                    },
                    modifier = Modifier.fillMaxSize()
                ){
                    LazyColumn() {
                        items(uiState.userList) { user ->
                            UserCard(user,viewModel)
                        }
                    }
                }
            }
        }

        if (uiState.showingDialog) {
            AlertDialog(
                onDismissRequest = { viewModel.closeDialog() },
                title = { Text("Create User") },
                text = {
                    Column {
                        TextField(
                            value = uiState.name,
                            onValueChange = { viewModel.updateNameField(it) },
                            placeholder = { Text("Name") }
                        )
                        Spacer(Modifier.height(8.dp))
                        TextField(
                            value = uiState.username,
                            onValueChange = { viewModel.updateUsernameField(it) },
                            placeholder = { Text("Username") }
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            viewModel.addUser()
                            viewModel.closeDialog()
                            viewModel.updateNameField("")
                            viewModel.updateUsernameField("")
                        }
                    ) {
                        Text("Create")
                    }
                },
                dismissButton = {
                    Button(onClick = { viewModel.closeDialog() }) {
                        Text("Cancel")
                    }
                },
            )
        }
    }
}

@Composable
fun UserCard(
    user: User,
    viewModel: MainViewModel
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        onClick = {

        }
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row (
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = user.username)
                Icon(
                    imageVector = Icons.Default.Delete ,
                    contentDescription = "Delete User",
                    modifier = Modifier.clickable(
                        onClick = {
                            viewModel.deleteUser(user = user)
                        }
                    )
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = user.name)
        }
    }
}