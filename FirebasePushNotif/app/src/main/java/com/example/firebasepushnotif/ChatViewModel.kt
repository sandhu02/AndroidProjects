package com.example.firebasepushnotif

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firebasepushnotif.network.FcmAPi
import com.example.firebasepushnotif.network.NotificationBody
import com.example.firebasepushnotif.network.SendMessageDTO
import com.google.firebase.Firebase
import com.google.firebase.messaging.messaging
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.IOException

const val BASE_URL = "http://192.168.0.150:8080/"  //TODO:change for physical device

class ChatViewModel : ViewModel() {
    var state by mutableStateOf(ChatState())
        private set

    private val api: FcmAPi = Retrofit
        .Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()
        .create(FcmAPi::class.java)

    fun onRemoteTokenChange(newToken  : String) {
        state = state.copy(remoteToken = newToken)
    }

    fun onSubmitRemoteToken() {
        state = state.copy(
            isEnteringToken = false
        )
    }

    fun onMessageChange(message : String) {
        state = state.copy(
            messageText = message
        )
    }

    fun sendMessage(isBroadcast : Boolean) {
        viewModelScope.launch {
            val messageDTO = SendMessageDTO(
                to = if (isBroadcast) null else state.remoteToken,
                notification = NotificationBody(
                    title = "New message!" ,
                    body = state.messageText
                )
            )
            try {
                if (isBroadcast) {
                    api.broadcast(messageDTO)
                }
                else {
                    api.sendMessage(messageDTO)
                }
                state = state.copy(messageText = "")
            }
            catch (e : HttpException) {
                e.printStackTrace()
            }
            catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    init {
        viewModelScope.launch {
            Firebase.messaging.subscribeToTopic("chat").await()
        }
    }
}