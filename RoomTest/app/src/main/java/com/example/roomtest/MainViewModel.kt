package com.example.roomtest

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.roomtest.room.DatabaseProvider
import com.example.roomtest.room.User
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class MainUiState (
    val showingDialog: Boolean = false,
    val name: String = "",
    val username: String = "",
    val userList: List<User> = listOf(),
    val isLoading: Boolean = false,
    val success: Boolean = false,
    val error: String? = null
)

class MainViewModel(context: Context) : ViewModel() {
    private val _uiState = MutableStateFlow(MainUiState())
    val uiState = _uiState.asStateFlow()

    val database = DatabaseProvider.getDatabase(context)
    val dao = database.userDao()

    fun updateNameField(newName: String) {
        _uiState.update { it.copy(name = newName) }
    }
    fun updateUsernameField(newUsername: String) {
        _uiState.update { it.copy(username = newUsername) }
    }

    fun closeDialog(){
        _uiState.update { it.copy(showingDialog = false) }
    }
    fun showDialog(){
        _uiState.update { it.copy(showingDialog = true) }
    }

    fun addUser(){
        val user = User(
            username = _uiState.value.username,
            name = _uiState.value.name
        )
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                dao.insert(user)
                val users = dao.getAllUsers()
                _uiState.update { it.copy(isLoading = false, success = true , userList = users) }
            }
            catch (e: Exception){
                _uiState.update { it.copy(isLoading = false , error = e.localizedMessage) }
            }
        }
    }

    fun getUsers(){
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val users = dao.getAllUsers()
                _uiState.update { it.copy(isLoading = false, success = true , userList = users) }
            }
            catch (e: Exception){
                _uiState.update { it.copy(isLoading = false , error = e.localizedMessage) }
            }
        }
    }

    fun deleteUser(user: User){
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                dao.deleteUser(user)
                val users = dao.getAllUsers()
                _uiState.update { it.copy(isLoading = false, success = true , userList = users) }
            }
            catch (e: Exception){
                _uiState.update { it.copy(isLoading = false , error = e.localizedMessage) }
            }
        }
    }

    init {
        getUsers()
    }
}
