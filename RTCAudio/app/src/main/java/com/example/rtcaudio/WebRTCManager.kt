package com.example.rtcaudio

import android.content.Context
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.webrtc.*

data class UiState(
    val isCalling: Boolean = false
)


class WebRTCManager(private val context: Context) {

    private val tag = "WebRTCManager"
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    // WebRTC Components
    private var peerConnectionFactory: PeerConnectionFactory? = null
    private var peerConnection: PeerConnection? = null
    private var audioTrack: AudioTrack? = null
    private var audioSource: AudioSource? = null

    // Firebase signaling
    private lateinit var signaling: FirebaseSignaling

    // Call state
    private val _callState = MutableStateFlow("Idle")
    val callState: StateFlow<String> = _callState

    // Current call ID
    private var currentCallId: String? = null
    private var isInitiator = false

    fun initialize() {
        Log.d(tag, "Initializing WebRTC")

        // Initialize PeerConnectionFactory
        val options = PeerConnectionFactory.InitializationOptions.builder(context)
            .createInitializationOptions()
        PeerConnectionFactory.initialize(options)

        val encoderFactory = DefaultVideoEncoderFactory(
            EglBase.create().eglBaseContext,
            true,
            true
        )
        val decoderFactory = DefaultVideoDecoderFactory(EglBase.create().eglBaseContext)

        peerConnectionFactory = PeerConnectionFactory.builder()
            .setVideoEncoderFactory(encoderFactory)
            .setVideoDecoderFactory(decoderFactory)
            .createPeerConnectionFactory()

        // Initialize Firebase signaling
        signaling = FirebaseSignaling(
            onOfferReceived = { callId, offer -> handleRemoteOffer(callId, offer) },
            onAnswerReceived = { answer -> handleRemoteAnswer(answer) },
            onIceCandidateReceived = { candidate -> handleRemoteIceCandidate(candidate) },
            onCallStateChanged = { state -> updateCallState(state) }
        )

        Log.d(tag, "WebRTC initialized successfully")
    }

    fun startCall(callId: String) {
        Log.d(tag, "Starting call with ID: $callId")
        currentCallId = callId
        isInitiator = true
        updateCallState("Calling")

        createPeerConnection()
        createOffer(callId)
    }

    fun answerCall(callId: String) {
        Log.d(tag, "Answering call with ID: $callId")
        currentCallId = callId
        isInitiator = false
        updateCallState("Connecting")

        // Check if there's already an offer waiting
        signaling.checkForOffer(callId) { offer ->
            if (offer != null) {
                createPeerConnection()
                handleRemoteOffer(callId, offer)
            }
        }
    }

    fun listenForIncomingCalls() {
        Log.d(tag, "Listening for incoming calls")
        signaling.listenForIncomingCalls()
        updateCallState("Listening")
    }

    fun endCall() {
        Log.d(tag, "Ending call")
        currentCallId?.let { callId ->
            signaling.endCall(callId)
        }
        cleanup()
        updateCallState("Idle")
    }

    private fun createPeerConnection() {
        Log.d(tag, "Creating peer connection")

        val rtcConfig = PeerConnection.RTCConfiguration(
            listOf(
                PeerConnection.IceServer.builder("stun:stun.l.google.com:19302").createIceServer()
            )
        )

        peerConnection = peerConnectionFactory?.createPeerConnection(
            rtcConfig,
            object : PeerConnection.Observer {
                override fun onSignalingChange(state: PeerConnection.SignalingState?) {
                    Log.d(tag, "Signaling state: $state")
                }

                override fun onIceConnectionChange(state: PeerConnection.IceConnectionState?) {
                    Log.d(tag, "ICE connection state: $state")
                    when (state) {
                        PeerConnection.IceConnectionState.CONNECTED -> {
                            updateCallState("Connected")
                        }
                        PeerConnection.IceConnectionState.DISCONNECTED -> {
                            updateCallState("Disconnected")
                        }
                        PeerConnection.IceConnectionState.FAILED -> {
                            updateCallState("Failed")
                            endCall()
                        }
                        else -> {}
                    }
                }

                override fun onIceConnectionReceivingChange(receiving: Boolean) {
                    Log.d(tag, "ICE connection receiving: $receiving")
                }

                override fun onIceGatheringChange(state: PeerConnection.IceGatheringState?) {
                    Log.d(tag, "ICE gathering state: $state")
                }

                override fun onIceCandidate(candidate: IceCandidate?) {
                    Log.d(tag, "ICE candidate generated")
                    candidate?.let {
                        currentCallId?.let { callId ->
                            signaling.sendIceCandidate(callId, it)
                        }
                    }
                }

                override fun onIceCandidatesRemoved(candidates: Array<out IceCandidate>?) {
                    Log.d(tag, "ICE candidates removed")
                }

                override fun onAddStream(stream: MediaStream?) {
                    Log.d(tag, "Stream added")
                }

                override fun onRemoveStream(stream: MediaStream?) {
                    Log.d(tag, "Stream removed")
                }

                override fun onDataChannel(channel: DataChannel?) {
                    Log.d(tag, "Data channel created")
                }

                override fun onRenegotiationNeeded() {
                    Log.d(tag, "Renegotiation needed")
                }

                override fun onAddTrack(receiver: RtpReceiver?, streams: Array<out MediaStream>?) {
                    Log.d(tag, "Track added")
                }
            }
        )

        // Add audio track
        createAudioTrack()
    }

    private fun createAudioTrack() {
        Log.d(tag, "Creating audio track")

        // Create audio constraints
        val audioConstraints = MediaConstraints().apply {
            mandatory.add(MediaConstraints.KeyValuePair("googEchoCancellation", "true"))
            mandatory.add(MediaConstraints.KeyValuePair("googNoiseSuppression", "true"))
            mandatory.add(MediaConstraints.KeyValuePair("googHighpassFilter", "true"))
        }

        // Create audio source and track
        audioSource = peerConnectionFactory?.createAudioSource(audioConstraints)
        audioTrack = peerConnectionFactory?.createAudioTrack("audio_track", audioSource)

        // Add track to peer connection
        audioTrack?.let { track ->
            peerConnection?.addTrack(track, listOf("stream"))
        }
    }

    private fun createOffer(callId: String) {
        Log.d(tag, "Creating offer")

        val constraints = MediaConstraints().apply {
            mandatory.add(MediaConstraints.KeyValuePair("OfferToReceiveAudio", "true"))
            mandatory.add(MediaConstraints.KeyValuePair("OfferToReceiveVideo", "false"))
        }

        peerConnection?.createOffer(object : SdpObserver {
            override fun onCreateSuccess(sessionDescription: SessionDescription?) {
                Log.d(tag, "Offer created successfully")
                sessionDescription?.let { offer ->
                    peerConnection?.setLocalDescription(object : SdpObserver {
                        override fun onCreateSuccess(p0: SessionDescription?) {}
                        override fun onSetSuccess() {
                            Log.d(tag, "Local description set successfully")
                            signaling.sendOffer(callId, offer)
                        }
                        override fun onCreateFailure(error: String?) {
                            Log.e(tag, "Failed to set local description: $error")
                        }
                        override fun onSetFailure(error: String?) {
                            Log.e(tag, "Failed to set local description: $error")
                        }
                    }, offer)
                }
            }

            override fun onSetSuccess() {}
            override fun onCreateFailure(error: String?) {
                Log.e(tag, "Failed to create offer: $error")
            }
            override fun onSetFailure(error: String?) {
                Log.e(tag, "Failed to set offer: $error")
            }
        }, constraints)
    }

    private fun createAnswer(callId: String) {
        Log.d(tag, "Creating answer")

        val constraints = MediaConstraints().apply {
            mandatory.add(MediaConstraints.KeyValuePair("OfferToReceiveAudio", "true"))
            mandatory.add(MediaConstraints.KeyValuePair("OfferToReceiveVideo", "false"))
        }

        peerConnection?.createAnswer(object : SdpObserver {
            override fun onCreateSuccess(sessionDescription: SessionDescription?) {
                Log.d(tag, "Answer created successfully")
                sessionDescription?.let { answer ->
                    peerConnection?.setLocalDescription(object : SdpObserver {
                        override fun onCreateSuccess(p0: SessionDescription?) {}
                        override fun onSetSuccess() {
                            Log.d(tag, "Local description set successfully")
                            signaling.sendAnswer(callId, answer)
                        }
                        override fun onCreateFailure(error: String?) {
                            Log.e(tag, "Failed to set local description: $error")
                        }
                        override fun onSetFailure(error: String?) {
                            Log.e(tag, "Failed to set local description: $error")
                        }
                    }, answer)
                }
            }

            override fun onSetSuccess() {}
            override fun onCreateFailure(error: String?) {
                Log.e(tag, "Failed to create answer: $error")
            }
            override fun onSetFailure(error: String?) {
                Log.e(tag, "Failed to set answer: $error")
            }
        }, constraints)
    }

    private fun handleRemoteOffer(callId: String, offer: SessionDescription) {
        Log.d(tag, "Handling remote offer")
        currentCallId = callId
        updateCallState("Ringing")

        if (peerConnection == null) {
            createPeerConnection()
        }

        peerConnection?.setRemoteDescription(object : SdpObserver {
            override fun onCreateSuccess(p0: SessionDescription?) {}
            override fun onSetSuccess() {
                Log.d(tag, "Remote description set successfully")
                createAnswer(callId)
            }
            override fun onCreateFailure(error: String?) {
                Log.e(tag, "Failed to set remote description: $error")
            }
            override fun onSetFailure(error: String?) {
                Log.e(tag, "Failed to set remote description: $error")
            }
        }, offer)
    }

    private fun handleRemoteAnswer(answer: SessionDescription) {
        Log.d(tag, "Handling remote answer")

        peerConnection?.setRemoteDescription(object : SdpObserver {
            override fun onCreateSuccess(p0: SessionDescription?) {}
            override fun onSetSuccess() {
                Log.d(tag, "Remote answer set successfully")
                updateCallState("Connecting")
            }
            override fun onCreateFailure(error: String?) {
                Log.e(tag, "Failed to set remote answer: $error")
            }
            override fun onSetFailure(error: String?) {
                Log.e(tag, "Failed to set remote answer: $error")
            }
        }, answer)
    }

    private fun handleRemoteIceCandidate(candidate: IceCandidate) {
        Log.d(tag, "Handling remote ICE candidate")
        peerConnection?.addIceCandidate(candidate)
    }

    private fun updateCallState(state: String) {
        coroutineScope.launch {
            _callState.value = state
        }
    }

    fun cleanup() {
        Log.d(tag, "Cleaning up WebRTC resources")

        audioTrack?.dispose()
        audioSource?.dispose()
        peerConnection?.close()
        peerConnection = null
        audioTrack = null
        audioSource = null
        currentCallId = null
        isInitiator = false
    }
}