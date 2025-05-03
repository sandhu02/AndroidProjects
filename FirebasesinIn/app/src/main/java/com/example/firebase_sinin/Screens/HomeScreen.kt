package com.example.firebase_sinin.Screens

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.firebase_sinin.ui.theme.FirebasesinInTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import com.example.firebase_sinin.SignInViewModelFactory


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    context: Context = LocalContext.current,
    viewModel: HomeScreenViewModel = viewModel(
        factory = SignInViewModelFactory(context)
    ),
    onSignOutClicked : () -> Unit = {}
) {
    val signoutUiState by viewModel.signoutState.collectAsState()
    val promptState by viewModel.uiState.collectAsState()

    LaunchedEffect(signoutUiState.isSignOutSuccessful) {
        if (signoutUiState.isSignOutSuccessful) {
            onSignOutClicked()
            viewModel.resetSignOutSuccess()
        }
    }

    Column (
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center
    ){
        Text(
            text = "Welcome to Home Screen",
            style = MaterialTheme.typography.displayMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = promptState.responseField ,
            onValueChange = {} ,
            shape = RoundedCornerShape(25.dp),
            modifier = Modifier.height(500.dp).fillMaxWidth(),
            placeholder = {
                if (promptState.isLoading) {
                    Text("Generating response...")
                }
                else {
                    Text("Response from AI will show here")
                }
          },
            readOnly = true,
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row (
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            OutlinedTextField(
                value = promptState.promptField,
                onValueChange = {viewModel.updatePromptField(it)},
                shape = RoundedCornerShape(25.dp),
                placeholder = { Text("Write Something...") }
            )
            Spacer(modifier = Modifier.width(26.dp))
            Icon(
                imageVector = Icons.Default.Send ,
                contentDescription = "send",
                modifier = Modifier.scale(2f)
                    .clickable {
                        viewModel.generateAiResponse()
                        viewModel.updatePromptField("")
                    }
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row (
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(
                onClick = {
                    viewModel.shareAiText(context = context )
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Share ,
                    contentDescription = "share",
                    modifier = Modifier.scale(1.5f)
                )
            }
            Button(
                shape = RoundedCornerShape(25.dp),
                onClick = {
                    viewModel.SignOut()
                },
            ) {
                if (signoutUiState.isLoading) {
                    CircularProgressIndicator(color = Color.White)
                } else {
                    Text(text = "Sign-Out")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun homePreview() {
    FirebasesinInTheme {
        HomeScreen()
    }
}