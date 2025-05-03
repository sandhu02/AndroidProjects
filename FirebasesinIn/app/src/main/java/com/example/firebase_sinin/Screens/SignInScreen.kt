package com.example.firebase_sinin.Screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.firebase_sinin.SignInViewModelFactory
import com.example.firebase_sinin.ui.theme.FirebasesinInTheme

@Composable
fun SignInSc`reen(
    onSignInClicked : () -> Unit = {} ,
    viewModel: SignInViewModel = viewModel(
        factory = SignInViewModelFactory(LocalContext.current)
    )
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.isSignInSuccessful) {
        if (uiState.isSignInSuccessful) {
            onSignInClicked()
            viewModel.resetSignInSuccess()
        }
    }

    Column (
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = "Sign-in" ,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.displayMedium
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = uiState.email,
            onValueChange = {viewModel.updateEmail(it)},
            shape = RoundedCornerShape(25.dp),
            modifier = Modifier.fillMaxWidth(),
            placeholder = {Text("E-mail")},
            leadingIcon = {
                Icon(imageVector = Icons.Rounded.Email , contentDescription = "Email")
            }

        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = uiState.password,
            onValueChange = { viewModel.updatePassword(it) },
            shape = RoundedCornerShape(25.dp),
            modifier = Modifier.fillMaxWidth(),
            placeholder = {Text("Password")},
            leadingIcon = {
                Icon(imageVector = Icons.Rounded.Lock , contentDescription = "password")
            },
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            shape = RoundedCornerShape(25.dp),
            onClick = {
                viewModel.SignIn()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(color = Color.White)
            } else {
                Text(text = "Sign-In")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Button(
            shape = RoundedCornerShape(25.dp),
            onClick = {
                viewModel.register()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Register")
        }
        if (uiState.error != null) {
            Text(text = uiState.error!! , color = Color.Red)
        }
    }

}


@Preview(showBackground = true)
@Composable
fun SignPreview() {
    FirebasesinInTheme {
//        SignInScreen()
    }
}