package com.example.cusotmauthtest.screens

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cusotmauthtest.retrofit.User
import com.example.cusotmauthtest.retrofit.Video
import com.example.cusotmauthtest.retrofit.getAuthApiInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class UsersScreenUiState(
    val usersResponse: UsersResponse? = null
)

class UsersForChatViewModel (val context: Context) : ViewModel () {
    private val _uiState = MutableStateFlow(UsersScreenUiState())
    val uiState = _uiState.asStateFlow()

    fun getUsers() {
        viewModelScope.launch {
            _uiState.update { it.copy(usersResponse = UsersResponse.Loading) }
            try {
                val response = getAuthApiInstance(context = context)?.getUsers()
                if (response == null){
                    _uiState.update { it.copy(usersResponse = UsersResponse.Failure("Empty Response")) }
                }
                else {
                    _uiState.update { it.copy(usersResponse = UsersResponse.Success(response) ) }
                }
            }
            catch (e: Exception) {
                _uiState.update { it.copy(usersResponse = UsersResponse.Failure("Network Error : ${e.localizedMessage}")) }
            }
        }
    }

    init {
        getUsers()
    }
}

sealed class UsersResponse() {
    data class Success(val usersList : List<User>) : UsersResponse()
    data class Failure(val error : String) : UsersResponse()
    object Loading : UsersResponse()
}