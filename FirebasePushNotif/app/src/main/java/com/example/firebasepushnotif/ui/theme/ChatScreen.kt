package com.example.firebasepushnotif.ui.theme

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.nio.file.WatchEvent

@Composable
fun ChatScreen(
    messageText : String,
    onMessageChange : (String) -> Unit,
    onMessageSend: () -> Unit,
    onMessageBroadcast : () -> Unit
) {
    Column (
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = messageText,
            onValueChange = onMessageChange ,
            placeholder = { Text("Enter a message") },
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        Row (
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            IconButton(
                onClick = {onMessageSend}
            ) {
                Icon(imageVector = Icons.Default.Send , contentDescription = "Send")
            }
            Spacer(modifier = Modifier.width(16.dp))

            IconButton(
                onClick = {onMessageBroadcast}
            ) {
                Icon(imageVector = Icons.Default.Share , contentDescription = "broadcast")
            }
        }
    }
}