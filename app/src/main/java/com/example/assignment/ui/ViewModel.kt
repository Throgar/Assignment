package com.example.assignment.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.UserApi.UserApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class UsersViewModel(
    state: SavedStateHandle,
    private val api: UserApi
) : ViewModel() {

    val users = state.getLiveData<Result<List<User>>?>("users", null)

    fun load() {
        GlobalScope.launch {
            val result = api.getUsers(page = 1)
            users.postValue(result)
        }
    }
}

data class User(
    val id: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val avatar: String
)