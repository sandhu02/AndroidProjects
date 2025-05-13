package com.example.streamcalling.Video

import io.getstream.video.android.core.Call

data class VideoCallState(
    val call : Call ,
    val callState: CallState? = null ,
    val error : String? = null
)

enum class CallState {
    JOINING ,
    ACTIVE ,
    ENDED
}