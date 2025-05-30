package com.example.edtech.screens

import android.os.Build
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.edtech.firebase.ChatMessage
import com.example.edtech.firebase.User
import com.example.edtech.firebase.getAllUsers
import com.example.edtech.firebase.getMessages
import com.example.edtech.firebase.getUserData
import com.example.edtech.firebase.sendMessage
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter


data class ChatUiState(
    val chatMessages: List<ChatMessage> = emptyList(),
    val messageField: String = "",
    val senderId : String = "" ,
    val isLoading : Boolean = false
)
data class TeacherChatUiState(
    val name: String = "",
    val allUsers: List<User> = emptyList(),
    val selectedUser: User? = null,
    val currentUser:User? = null ,
    val isLoading : Boolean = false
)

class TeacherChatViewModel () : ViewModel() {
    private val _teacherChatUiState = MutableStateFlow(TeacherChatUiState())
    val teacherChatUiState = _teacherChatUiState.asStateFlow()

    private val _chatUitate = MutableStateFlow(ChatUiState())
    val chatUitate = _chatUitate.asStateFlow()

    val currentUid = FirebaseAuth.getInstance().currentUser?.uid

    fun setSelectedUser(user: User) {
        _teacherChatUiState.update { it.copy(selectedUser = user) }
    }

    fun updateMessageField(message: String) {
        _chatUitate.update { it.copy(messageField = message) }
    }

    init {
        _chatUitate.update { it.copy(senderId = currentUid.toString()) }
        getAllUsers()
        getCurrentUser() // ✅ load once
    }

    private fun getCurrentUser() {
        getUserData(currentUid.toString()) { user ->
            if (user != null) {
                _teacherChatUiState.update { it.copy(currentUser = user) }
            }
        }
    }

    fun getAllUsers() {
        _teacherChatUiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            try {
                getAllUsers(
                    onComplete = { users ->
                        _teacherChatUiState.update { it.copy(allUsers = users, isLoading = false) }
                    },
                    onError = { exception ->
                        // Handle error (you could add an error state to your UI state if needed)
                        Log.e("TeacherChatViewModel" ,"Error loading users: ${exception.message}")
                    }
                )
            } catch (e: Exception) {
                Log.e("TeacherChatViewModel" ,"Error in getallusers: ${e.message}")
            }
        }
    }
    fun loadMessages() {
        _chatUitate.update { it.copy(isLoading = true) }
        getChatRoomId { chatRoomId ->
            if (chatRoomId.isNotEmpty()) {
                viewModelScope.launch {
                    try {
                        getMessages(chatRoomId).collect { messages ->
                            _chatUitate.update { it.copy(chatMessages = messages , isLoading = false) }
                        }
                    } catch (e: Exception) {
                        Log.e("ChatViewModel", "Error loading messages", e)
                    }
                }
            }else {
                Log.e("ChatViewModel", "Chat room ID is empty")
            }
        }
    }

    fun sendMessage() {
        val text: String = chatUitate.value.messageField
        getChatRoomId { chatRoomId ->
            if (chatRoomId.isNotEmpty()) {
                val message = ChatMessage(senderId = currentUid.toString(), text = text)
                sendMessage(chatRoomId, message)
            }
        }

    }

    fun getChatRoomId(callback: (String) -> Unit) {
        val selectedUser = teacherChatUiState.value.selectedUser
        if (selectedUser == null) {
            Log.e("ChatViewModel", "No user selected")
            callback("")
            return
        }

        getUserData(currentUid.toString()) { user ->
            if (user != null ) {
                val user1Id = user.email.replace(".", "_dot_")
                val user2Id = selectedUser.email.replace(".", "_dot_")

                val chatRoomId = if (user1Id < user2Id) {
                    "${user1Id}_${user2Id}"
                } else {
                    "${user2Id}_${user1Id}"
                }

                Log.d("ChatViewModel", "Generated chatRoomId: $chatRoomId")
                callback(chatRoomId)
            } else {
                Log.e("ChatViewModel", "Current user is null")
                callback("") // or handle error case
            }
        }
    }

    fun isThisUserCurrentUser(selectedUser: User?): Boolean {
        val currentUser = teacherChatUiState.value.currentUser
        return selectedUser?.email == currentUser?.email
    }

    fun formatTimeFromMillis(millis: Long): String {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val formatter = DateTimeFormatter.ofPattern("HH:mm:ss a").withZone(ZoneId.systemDefault())
            val time = formatter.format(Instant.ofEpochMilli(millis))
            return time
        }
        else {
            return "SDK not supported"
        }

    }

}

