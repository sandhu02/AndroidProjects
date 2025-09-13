package com.example.cusotmauthtest.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.cusotmauthtest.AuthTestScreens
import com.example.cusotmauthtest.AuthTestViewModelFactory
import com.example.cusotmauthtest.retrofit.Video
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.NavigationBar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeScreenViewModel = viewModel(factory = AuthTestViewModelFactory(LocalContext.current)),
    sharedViewModel : SharedVideoViewModel
){
    val homeUiState by viewModel.homeUiState.collectAsState()
    val context = LocalContext.current
    Scaffold(
        topBar = {
            TopAppBar(
                title = {Text(text = "Home Screen") },
                actions = {
                    IconButton(onClick = {
                        viewModel.logout()
                    }) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "Search"
                        )
                    }
                }
            )
        },
        bottomBar = {
            BottomAppBar(modifier = Modifier.navigationBarsPadding()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(onClick = { navController.navigate(AuthTestScreens.UploadVideoScreen.name) }) {
                        Text("Upload Video")
                    }
                    Button(onClick = {navController.navigate(AuthTestScreens.ChatUsersScreen.name) }) {
                        Text("Chat")
                    }
                }
            }
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            when (val response = homeUiState.videosResponse) {
                is VideosResponse.Success -> {
                    val videos = response.videoList
                    LazyColumn(
                        modifier = Modifier.weight(1f), // Takes all available space
                        verticalArrangement = if (videos.isEmpty()) {
                            Arrangement.Center
                        } else {
                            Arrangement.Top
                        }
                    ) {
                        items(videos) { video ->
                            VideoCard(
                                navController = navController,
                                video = video,
                                sharedVideoViewModel = sharedViewModel,
                                viewModel = viewModel
                            )
                        }
                    }
                }

                is VideosResponse.Failure -> {
                    Text(
                        text = response.error,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(16.dp)
                    )
                }

                is VideosResponse.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize().weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                null -> {}
            }

            when (val response = homeUiState.logoutResponse) {
                is LogoutResponse.Failure -> {
                    Toast.makeText(context, response.message, Toast.LENGTH_LONG).show()
                }
                is LogoutResponse.Success -> {
                    Toast.makeText(context, response.message, Toast.LENGTH_LONG).show()
                    navController.navigate(AuthTestScreens.SignInScreen.name)
                }
                is LogoutResponse.Loading -> {
                    Toast.makeText(context, "Logging Out...", Toast.LENGTH_SHORT).show()
                }
                null -> {}
            }

            when (val response = homeUiState.deleteResponse) {
                is DeleteResponse.Failure -> {
                    AlertDialog(
                        onDismissRequest = { viewModel.closeDialog() },
                        title = { Text("Failure") },
                        text = { Text(response.message) },
                        confirmButton = {
                            Button(
                                onClick = {
                                    viewModel.closeDialog()
                                }
                            ) {
                                Text("Ok")
                            }
                        }
                    )
                }
                is DeleteResponse.Loading -> {
                    AlertDialog(
                        onDismissRequest = {  },
                        text = {
                            Row (modifier= Modifier.fillMaxWidth(),horizontalArrangement = Arrangement.Center){
                                CircularProgressIndicator()
                            }
                        },
                        confirmButton = {}
                    )
                }
                is DeleteResponse.Success -> {
                    AlertDialog(
                        onDismissRequest = { viewModel.closeDialog() },
                        title = { Text("Success") },
                        text = { Text(response.message) },
                        confirmButton = {
                            Button(
                                onClick = {
                                    viewModel.closeDialog()
                                }
                            ) {
                                Text("Ok")
                            }
                        }
                    )
                }
                null -> {}
            }
        }
    }
}


@Composable
fun VideoCard(
    navController: NavController ,
    sharedVideoViewModel: SharedVideoViewModel,
    viewModel: HomeScreenViewModel ,
    video: Video
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        onClick = {
            sharedVideoViewModel.setVideo(video = video)
            navController.navigate(AuthTestScreens.ViewVideoScreen.name)
        }
    ) {
        Column(
            modifier = Modifier.padding(12.dp).fillMaxWidth()
        ) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = video.title)
                IconButton(onClick = {
                    viewModel.deleteVideo(video)
                }) {
                    Icon(imageVector = Icons.Default.Delete , contentDescription = "delete")
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = video.description)
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = video.url)
        }

    }
}
