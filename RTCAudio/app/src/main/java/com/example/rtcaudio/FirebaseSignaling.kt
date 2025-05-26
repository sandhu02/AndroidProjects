package com.example.rtcaudio

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import org.webrtc.IceCandidate
import org.webrtc.SessionDescription

class FirebaseSignaling(
    private val onOfferReceived: (String, SessionDescription) -> Unit,
    private val onAnswerReceived: (SessionDescription) -> Unit,
    private val onIceCandidateReceived: (IceCandidate) -> Unit,
    private val onCallStateChanged: (String) -> Unit
) {
    private val tag = "FirebaseSignaling"
    private val firestore = FirebaseFirestore.getInstance()
    private val callsCollection = firestore.collection("calls")

    private var callListener: ListenerRegistration? = null
    private var incomingCallsListener: ListenerRegistration? = null

    fun sendOffer(callId: String, offer: SessionDescription) {
        Log.d(tag, "Sending offer for call: $callId")

        val offerData = mapOf(
            "type" to offer.type.canonicalForm(),
            "sdp" to offer.description,
            "timestamp" to System.currentTimeMillis()
        )

        callsCollection.document(callId)
            .set(mapOf("offer" to offerData))
            .addOnSuccessListener {
                Log.d(tag, "Offer sent successfully")
                listenForAnswer(callId)
                listenForIceCandidates(callId)
            }
            .addOnFailureListener { e ->
                Log.e(tag, "Failed to send offer", e)
            }
    }

    fun sendAnswer(callId: String, answer: SessionDescription) {
        Log.d(tag, "Sending answer for call: $callId")

        val answerData = mapOf(
            "type" to answer.type.canonicalForm(),
            "sdp" to answer.description,
            "timestamp" to System.currentTimeMillis()
        )

        callsCollection.document(callId)
            .update("answer", answerData)
            .addOnSuccessListener {
                Log.d(tag, "Answer sent successfully")
                listenForIceCandidates(callId)
            }
            .addOnFailureListener { e ->
                Log.e(tag, "Failed to send answer", e)
            }
    }

    fun sendIceCandidate(callId: String, candidate: IceCandidate) {
        Log.d(tag, "Sending ICE candidate for call: $callId")

        val candidateData = mapOf(
            "sdpMid" to candidate.sdpMid,
            "sdpMLineIndex" to candidate.sdpMLineIndex,
            "candidate" to candidate.sdp,
            "timestamp" to System.currentTimeMillis()
        )

        callsCollection.document(callId)
            .collection("candidates")
            .add(candidateData)
            .addOnSuccessListener {
                Log.d(tag, "ICE candidate sent successfully")
            }
            .addOnFailureListener { e ->
                Log.e(tag, "Failed to send ICE candidate", e)
            }
    }

    fun checkForOffer(callId: String, callback: (SessionDescription?) -> Unit) {
        Log.d(tag, "Checking for offer for call: $callId")

        callsCollection.document(callId)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val offerData = document.get("offer") as? Map<String, Any>
                    if (offerData != null) {
                        val type = offerData["type"] as String
                        val sdp = offerData["sdp"] as String
                        val offer = SessionDescription(
                            SessionDescription.Type.fromCanonicalForm(type),
                            sdp
                        )
                        callback(offer)
                    } else {
                        callback(null)
                    }
                } else {
                    callback(null)
                }
            }
            .addOnFailureListener { e ->
                Log.e(tag, "Failed to check for offer", e)
                callback(null)
            }
    }

    fun listenForIncomingCalls() {
        Log.d(tag, "Listening for incoming calls")

        incomingCallsListener?.remove()
        incomingCallsListener = callsCollection
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.e(tag, "Listen failed for incoming calls", e)
                    return@addSnapshotListener
                }

                snapshot?.documentChanges?.forEach { change ->
                    if (change.type == com.google.firebase.firestore.DocumentChange.Type.ADDED) {
                        val doc = change.document
                        val offerData = doc.get("offer") as? Map<String, Any>
                        val answerData = doc.get("answer") as? Map<String, Any>

                        // Only notify about new offers (not answered calls)
                        if (offerData != null && answerData == null) {
                            val type = offerData["type"] as String
                            val sdp = offerData["sdp"] as String
                            val offer = SessionDescription(
                                SessionDescription.Type.fromCanonicalForm(type),
                                sdp
                            )
                            onOfferReceived(doc.id, offer)
                        }
                    }
                }
            }
    }

    private fun listenForAnswer(callId: String) {
        Log.d(tag, "Listening for answer for call: $callId")

        callListener?.remove()
        callListener = callsCollection.document(callId)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.e(tag, "Listen failed for answer", e)
                    return@addSnapshotListener
                }

                snapshot?.let { doc ->
                    val answerData = doc.get("answer") as? Map<String, Any>
                    if (answerData != null) {
                        val type = answerData["type"] as String
                        val sdp = answerData["sdp"] as String
                        val answer = SessionDescription(
                            SessionDescription.Type.fromCanonicalForm(type),
                            sdp
                        )
                        onAnswerReceived(answer)
                    }
                }
            }
    }

    private fun listenForIceCandidates(callId: String) {
        Log.d(tag, "Listening for ICE candidates for call: $callId")

        callsCollection.document(callId)
            .collection("candidates")
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.e(tag, "Listen failed for ICE candidates", e)
                    return@addSnapshotListener
                }

                snapshot?.documentChanges?.forEach { change ->
                    if (change.type == com.google.firebase.firestore.DocumentChange.Type.ADDED) {
                        val candidateData = change.document.data
                        val sdpMid = candidateData["sdpMid"] as String
                        val sdpMLineIndex = (candidateData["sdpMLineIndex"] as Long).toInt()
                        val candidateSdp = candidateData["candidate"] as String

                        val iceCandidate = IceCandidate(sdpMid, sdpMLineIndex, candidateSdp)
                        onIceCandidateReceived(iceCandidate)
                    }
                }
            }
    }

    fun endCall(callId: String) {
        Log.d(tag, "Ending call: $callId")

        // Mark call as ended
        callsCollection.document(callId)
            .update("ended", true, "endTime", System.currentTimeMillis())
            .addOnSuccessListener {
                Log.d(tag, "Call ended successfully")
            }
            .addOnFailureListener { e ->
                Log.e(tag, "Failed to end call", e)
            }

        // Clean up listeners
        callListener?.remove()
        callListener = null
    }

    fun cleanup() {
        Log.d(tag, "Cleaning up Firebase signaling")
        callListener?.remove()
        incomingCallsListener?.remove()
        callListener = null
        incomingCallsListener = null
    }
}