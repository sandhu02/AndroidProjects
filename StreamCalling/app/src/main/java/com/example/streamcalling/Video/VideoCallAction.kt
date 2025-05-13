package com.example.streamcalling.Video

sealed interface VideoCallAction {
    data object OnDisconnectClick : VideoCallAction
    data object JoinCallClick : VideoCallAction
}