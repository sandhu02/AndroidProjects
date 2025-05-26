package com.example.rtcaudio

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CallScreen(webRTCManager: WebRTCManager) {
    var callId by remember { mutableStateOf("") }
    var isInCall by remember { mutableStateOf(false) }
    var callState by remember { mutableStateOf("Idle") }

    // Observe call state
    LaunchedEffect(Unit) {
        webRTCManager.callState.collect { state ->
            callState = state
            isInCall = state == "Connected" || state == "Calling" || state == "Ringing"
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Audio Call App",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        Text(
            text = "Status: $callState",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (!isInCall) {
            OutlinedTextField(
                value = callId,
                onValueChange = { callId = it },
                label = { Text("Enter Call ID") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = {
                        if (callId.isNotBlank()) {
                            webRTCManager.startCall(callId)
                        }
                    },
                    enabled = callId.isNotBlank()
                ) {
                    Text("Start Call")
                }

                Button(
                    onClick = {
                        if (callId.isNotBlank()) {
                            webRTCManager.answerCall(callId)
                        }
                    },
                    enabled = callId.isNotBlank()
                ) {
                    Text("Answer Call")
                }
            }

            Button(
                onClick = { webRTCManager.listenForIncomingCalls() },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text("Listen for Incoming Calls")
            }
        } else {
            Button(
                onClick = { webRTCManager.endCall() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text("End Call")
            }
        }
    }
}

