package com.example.edtech.firebase

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow


data class User(
    val email: String = "",
    val role: String = "",
    val name: String = ""
)
data class ChatMessage(
    val senderId: String = "",
    val text: String = "",
    val timestamp: Long = System.currentTimeMillis()
)


fun saveUserRoleToFirestore(uid: String?, role: String , name: String) : Task<Void> {
    val db = FirebaseFirestore.getInstance()
    val userMap = hashMapOf(
        "role" to role,
        "email" to FirebaseAuth.getInstance().currentUser?.email ,
        "name" to name
    )

    return db.collection("users").document(uid.toString())
        .set(userMap)

}

fun getUserData(uid: String, onComplete: (User?) -> Unit) {
    val db = FirebaseFirestore.getInstance()

    db.collection("users").document(uid)
        .get()
        .addOnSuccessListener { document ->
            if (document.exists()) {
                val user = document.toObject(User::class.java)
                onComplete(user)
            } else {
                onComplete(null)
            }
        }
        .addOnFailureListener {
            onComplete(null)
        }
}

fun sendMessage(chatRoomId: String, message: ChatMessage) {
    val db = Firebase.firestore
    db.collection("chats")
        .document(chatRoomId)
        .collection("messages")
        .add(message)
}


fun getMessages(chatRoomId: String): Flow<List<ChatMessage>> = callbackFlow {
    val listener = FirebaseFirestore.getInstance().collection("chats")
        .document(chatRoomId)
        .collection("messages")
        .orderBy("timestamp")
        .addSnapshotListener { snapshot, error ->
            if (error != null || snapshot == null) {
                close(error ?: Exception("Unknown error"))
                return@addSnapshotListener
            }

            val messages = snapshot.toObjects(ChatMessage::class.java)
            trySend(messages)
        }

    awaitClose { listener.remove() }
}

fun getAllUsers(onComplete: (List<User>) -> Unit, onError: (Exception) -> Unit) {
    val users = mutableListOf<User>()
    val db = FirebaseFirestore.getInstance()

    db.collection("users")
        .get()
        .addOnSuccessListener { result ->
            try {
                for (document in result) {
                    val email = document.getString("email") ?: continue
                    val name = document.getString("name") ?: continue
                    val role = document.getString("role") ?: continue
                    users.add(User(email, role, name))
                    Log.d("Firestore", "Fetched user: $name")
                }
                onComplete(users)
            } catch (e: Exception) {
                Log.w("Firestore", "Error parsing documents", e)
                onError(e)
            }
        }
        .addOnFailureListener { exception ->
            Log.w("Firestore", "Error getting documents", exception)
            onError(exception)
        }
}