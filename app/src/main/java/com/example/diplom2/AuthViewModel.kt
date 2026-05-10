package com.example.diplom2

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bd.AppDatabase
import repository.UserRepository
import kotlinx.coroutines.launch
import table.User

class AuthViewModel : ViewModel() {
    private lateinit var userRepo: UserRepository

    fun init(context: android.content.Context) {
        val db = AppDatabase.getInstance(context)
        userRepo = UserRepository(db.userDao())
    }

    fun login(email: String, password: String, onResult: (User?) -> Unit) {
        viewModelScope.launch {
            val user = userRepo.login(email, password)
            onResult(user)
        }
    }

    fun register(email: String, password: String, name: String, levelId: Int, onResult: (Long) -> Unit) {
        viewModelScope.launch {
            val userId = userRepo.register(email, password, name, levelId)
            onResult(userId)
        }
    }
}