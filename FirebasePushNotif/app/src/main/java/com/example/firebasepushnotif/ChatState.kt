package com.example.firebasepushnotif

data class ChatState (
    val isEnteringToken : Boolean = true,
    val remoteToken:String = "",
    val messageText: String = ""
)