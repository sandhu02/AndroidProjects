package com.example.rtcaudio

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp

@Composable
fun MainScreen(modifier: Modifier) {

    val viewModel = MainScreenViewModel()
    val uiState by viewModel.uiState.collectAsState()

    Surface (modifier = modifier.fillMaxSize()) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                value = uiState.callerIdTextField,
                onValueChange = { viewModel.updateCallerIdTextField(it) },
                placeholder = { Text("Enter caller ID") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Phone,
                        contentDescription = "Phone icon"
                    )
                },
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                shape = CircleShape ,
                modifier = Modifier.size(80.dp).padding(8.dp),
                onClick = {}
            ) {
                Icon(
                    imageVector = Icons.Default.Phone ,
                    modifier = Modifier.scale(2f) ,
                    contentDescription = "Phone"
                )
            }
        }
    }
}
