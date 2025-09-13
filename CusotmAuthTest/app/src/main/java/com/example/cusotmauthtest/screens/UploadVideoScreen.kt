package com.example.cusotmauthtest.screens

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.cusotmauthtest.AuthTestViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UploadVideScreen(
    viewModel : UploadVideoViewModel = viewModel(factory = AuthTestViewModelFactory(LocalContext.current)),
    navController: NavController,
    context : Context = LocalContext.current
) {
    val uiState by viewModel.uiState.collectAsState()

    val videoLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            if (uri != null) {
                viewModel.updateUri(uri)
            }
        }
    )

    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text("Upload Video", style = MaterialTheme.typography.displayLarge)
            })
        }
    ) { innerPadding ->
        if (uiState.showingDialog) {
            when (val res = uiState.response) {
                is UploadVideoResponse.Success -> {
                    AlertDialog(
                        onDismissRequest = { viewModel.closeDialog() },
                        title = { Text("Success") },
                        text = { Text(res.message) },
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
                is UploadVideoResponse.Failure -> {
                    AlertDialog(
                        onDismissRequest = { viewModel.closeDialog() },
                        title = { Text("Failure") },
                        text = { Text(res.error) },
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
                is UploadVideoResponse.Loading -> {
                    AlertDialog(
                        onDismissRequest = { },
                        text = {
                            Row (modifier= Modifier.fillMaxWidth(),horizontalArrangement = Arrangement.Center){
                                CircularProgressIndicator()
                            }
                        },
                        confirmButton = {}
                    )
                }
                else -> {}
            }
        }
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(innerPadding)
                .padding(12.dp)
                .fillMaxSize()
        ) {

            OutlinedTextField(
                value = uiState.title,
                onValueChange = { viewModel.updateTitle(it) },
                placeholder = { Text("Video Title") },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = uiState.description ,
                onValueChange = { viewModel.updateDescription(it) },
                placeholder = { Text("Video Description") },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .weight(2f)
                    .fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            Card(
                modifier = Modifier
                    .weight(2f)
                    .fillMaxWidth(),
                onClick = { videoLauncher.launch("video/*") }
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    if (uiState.videoUri == null) {
                        Icon(
                            imageVector = Icons.Outlined.Add,
                            contentDescription = "Add",
                            modifier = Modifier
                                .scale(3f)
                                .padding(20.dp)
                        )
                        Spacer(modifier = Modifier.height(30.dp))
                        Text(text = "Add Video to Upload")
                    }
                    else {
                        Row(modifier = Modifier.fillMaxWidth() , horizontalArrangement = Arrangement.Center){
                            Text(text = uiState.videoUri.toString())
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = {
                    viewModel.uploadVideo(context)
                    viewModel.resetFields()
                },
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(text = "Upload Video")
            }
        }
    }
}