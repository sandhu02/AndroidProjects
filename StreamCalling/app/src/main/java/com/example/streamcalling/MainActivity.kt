package com.example.streamcalling

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.streamcalling.Video.CallState
import com.example.streamcalling.Video.VideoCallScreen
import com.example.streamcalling.Video.VideoCallViewModel
import com.example.streamcalling.connect.ConnectScreen
import com.example.streamcalling.connect.ConnectViewModel
import com.example.streamcalling.ui.theme.StreamCallingTheme
import io.getstream.video.android.compose.theme.VideoTheme
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StreamCallingTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val navController = rememberNavController()
                    NavHost(
                        navController ,
                        startDestination = ConnectRoute ,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable<ConnectRoute> {
                            val viewModel = koinViewModel<ConnectViewModel>()
                            val state = viewModel.state

                            LaunchedEffect(state.isConnected) {
                                if (state.isConnected) {
                                    navController.navigate(VideoCallRoute){
                                        popUpTo(ConnectRoute){inclusive = true}
                                    }
                                }
                            }

                            ConnectScreen(
                                state = state, onAction = viewModel::onAction,
                            )
                        }
                        composable<VideoCallRoute> {
                            val viewModel = koinViewModel<VideoCallViewModel>()
                            val state = viewModel.state

                            LaunchedEffect(state.callState) {
                                if (state.callState == CallState.ENDED) {
                                    navController.navigate(ConnectRoute) {
                                        popUpTo(VideoCallRoute){ inclusive = true }
                                    }
                                }
                            }
                            VideoTheme {
                                VideoCallScreen(state , onAction = viewModel::onAction)
                            }
                        }
                    }
                }
            }
        }
    }
}


@Serializable
data object ConnectRoute

@Serializable
data object VideoCallRoute