package com.example.cusotmauthtest.screens

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.cusotmauthtest.retrofit.BASE_URL
import com.example.cusotmauthtest.retrofit.getToken
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.json.JSONObject

data class ChatUiState(
    val senderMessage : String = "",
    val receivedMessageList : List<String> = listOf<String>(),
    val connectionMessage : String = "",
)

class ChatViewModel (val context:Context) : ViewModel() {
    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState = _uiState.asStateFlow()

    private lateinit var mSocket: Socket

    fun updateSenderMessage(newMessage : String){
        _uiState.update { it.copy(senderMessage = newMessage) }
    }

    fun connect() {
        try {
            val options = IO.Options()
            options.reconnection = true
            options.forceNew = true
            options.auth = mapOf("token" to getToken(context))

            mSocket = IO.socket(BASE_URL, options)
            mSocket.connect()

            mSocket.on(Socket.EVENT_CONNECT) {
                Log.d("Socket","Connected to server")
                _uiState.update { it.copy(connectionMessage = "Connected to server") }
            }

            mSocket.on("chat message", onMessageReceived)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun sendMessage(message: String , toUserName : String ) {
        mSocket.emit("chat message",
            JSONObject().apply {
                put("toUserName", toUserName)
                put("message", message)
            })
    }

    private val onMessageReceived = Emitter.Listener { args ->
        val data = args[0] as JSONObject
        Log.d("Socket", "Message from ${data.getString("from")}: ${data.getString("message")}")
        _uiState.update { it.copy(receivedMessageList = it.receivedMessageList + data.getString("message") ) }
    }

    fun disconnect() {
        mSocket.disconnect()
        mSocket.off()
    }

    init {
        connect()
    }
}