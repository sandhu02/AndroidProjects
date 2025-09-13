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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.cusotmauthtest.AuthTestScreens
import com.example.cusotmauthtest.AuthTestViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    navController: NavController,
    viewModel: RegisterViewModel = viewModel(factory = AuthTestViewModelFactory(LocalContext.current))
) {
    val registerUiState by viewModel.registerUiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {Text("Register")},
            )
        },
        bottomBar = {},
    ) {innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(value = registerUiState.username , onValueChange = {viewModel.updateUsername(it)} , placeholder = {Text("Username")})
            Spacer(modifier = Modifier.height(20.dp))
            TextField(value = registerUiState.name , onValueChange = {viewModel.updateName(it)} , placeholder = {Text("Name")})
            Spacer(modifier = Modifier.height(20.dp))
            TextField(value = registerUiState.password , onValueChange = {viewModel.updatePassword(it)} , placeholder = {Text("Password")})
            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    viewModel.register()
                },
                modifier = Modifier.fillMaxWidth()
            ) {Text("Register") }
            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    navController.navigate(AuthTestScreens.SignInScreen.name)
                },
                modifier = Modifier.fillMaxWidth()
            ) {Text("Back To SignIn") }

            when (val res = registerUiState.response) {
                is Loading -> {
                    CircularProgressIndicator()
                }
                is Success -> {
                    Text(res.message)
                }
                is Failure -> {
                    Text(res.message, color = Color.Red)
                }
                null -> {
                    Text("Enter Username and password to Register")
                }
            }
        }
    }
}