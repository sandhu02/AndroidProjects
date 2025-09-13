package com.example.cusotmauthtest.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.cusotmauthtest.videoPlayer.VideoPlayer


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewVideoScreen(
    navController: NavController,
    sharedVideoViewModel: SharedVideoViewModel
){
    val selectedVideo by sharedVideoViewModel.selectedVideo.collectAsState()
    val videoUrl = selectedVideo?.url
    Scaffold(
        topBar = { TopAppBar(title = {Text(selectedVideo?.description ?: "")}) }
    ) { innerPadding ->
        VideoPlayer(
            videoUrl = videoUrl ?: "",
            modifier = Modifier.fillMaxSize().padding(innerPadding)
        )
    }
}