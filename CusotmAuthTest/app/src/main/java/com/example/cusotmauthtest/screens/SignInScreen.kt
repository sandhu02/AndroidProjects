package com.example.cusotmauthtest.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.cusotmauthtest.AuthTestScreens
import com.example.cusotmauthtest.AuthTestViewModelFactory
import com.google.firebase.Firebase
import com.google.firebase.messaging.messaging
import kotlinx.coroutines.tasks.await

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignInScreen(
    navController: NavController,
    viewModel: SignInViewModel = viewModel(factory = AuthTestViewModelFactory(context = LocalContext.current))
) {
    val signInUiState by viewModel.signInUiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {Text("Sign In")},
            )
        },
        bottomBar = {},
    ) {innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(12.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                value = signInUiState.username ,
                onValueChange = {viewModel.updateUsername(it)} ,
                placeholder = {Text("Username")},
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(20.dp))
            TextField(
                value = signInUiState.password ,
                onValueChange = {viewModel.updatePassword(it)} ,
                placeholder = {Text("Password")},
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    viewModel.login()
                },
                modifier = Modifier.fillMaxWidth()
            ) {Text("Sign In") }
            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    navController.navigate(AuthTestScreens.RegisterScreen.name)
                },
                modifier = Modifier.fillMaxWidth()
            ) {Text("Register") }

            Spacer(modifier = Modifier.height(20.dp))

            when (val res = signInUiState.response) {
                is Loading -> {
                    CircularProgressIndicator()
                }
                is Success -> {
                    Text(res.message)
                    navController.navigate(AuthTestScreens.HomeScreen.name){
                        popUpTo(AuthTestScreens.SignInScreen.name){inclusive = true}
                    }
                }
                is Failure -> {
                    Text(res.message, color = Color.Red)
                }
                null -> {
                    Text("Enter Username and password to Signin")
                }
            }

            Button(onClick = {
                viewModel.getFirebaseToken()
            }) {Text("Send Push Notification") }

            TextField(value = signInUiState.localToken?:"" , onValueChange = {/*viewModel.updateToken(it)*/})
        }


    }
}