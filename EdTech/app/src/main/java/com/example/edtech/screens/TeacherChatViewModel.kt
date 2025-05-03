package com.example.edtech.screens

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.edtech.FirebaseAuth.ChatMessage
import com.example.edtech.FirebaseAuth.User
import com.example.edtech.FirebaseAuth.getAllUsers
import com.example.edtech.FirebaseAuth.getMessages
import com.example.edtech.FirebaseAuth.getUserData
import com.example.edtech.FirebaseAuth.sendMessage
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


data class ChatUiState(
    val chatMessages: List<ChatMessage> = emptyList(),
    val messageField: String = "",
    val senderId : String = "" ,
)
data class TeacherChatUiState(
    val name: String = "",
    val allUsers: List<User> = emptyList(),
    val selectedUser: User? = null
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

    fun getAllUsers() {
        viewModelScope.launch {
            try {
                getAllUsers(
                    onComplete = { users ->
                        val teachers = users
                        _teacherChatUiState.update { it.copy(allUsers = teachers) }
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
        getChatRoomId { chatRoomId ->
            if (chatRoomId.isNotEmpty()) {
                viewModelScope.launch {
                    try {
                        getMessages(chatRoomId).collect { messages ->
                            _chatUitate.update { it.copy(chatMessages = messages) }
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

    init {
        _chatUitate.update { it.copy(senderId = currentUid.toString()) }
        getAllUsers()
    }
}

