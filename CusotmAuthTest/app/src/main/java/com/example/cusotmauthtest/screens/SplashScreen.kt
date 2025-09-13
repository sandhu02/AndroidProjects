package com.example.cusotmauthtest.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.cusotmauthtest.AuthTestScreens
import com.example.cusotmauthtest.AuthTestViewModelFactory
import com.example.cusotmauthtest.R

@Composable
fun SplashScreen(
    navController: NavController ,
    viewModel: SplashViewModel = viewModel (factory = AuthTestViewModelFactory(LocalContext.current))
) {
    val uiState by viewModel.uiState.collectAsState()

    when (uiState.response) {
        is SplashResponse.Failure -> {
            navController.navigate(AuthTestScreens.SignInScreen.name){
                popUpTo(AuthTestScreens.SplashScreen.name){inclusive = true}
            }
        }
        is SplashResponse.Loading -> {
            Column (
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = "Logo",
                    modifier = Modifier.size(120.dp)
                )
            }
        }
        is SplashResponse.Success -> {
            navController.navigate(AuthTestScreens.HomeScreen.name){
                popUpTo(AuthTestScreens.SplashScreen.name){inclusive = true}
            }
        }
        null -> {}
    }
}