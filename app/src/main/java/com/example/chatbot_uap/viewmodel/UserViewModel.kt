package com.example.chatbot_uap.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatbot_uap.db.User
import com.example.chatbot_uap.db.UserDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserViewModel(application: Application) : AndroidViewModel(application) {

    private val userDao = UserDatabase.getDatabase(application).userDao()

    fun addUser(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            userDao.addUser(user)
        }
    }

    suspend fun findUserByEmailAndPassword(email: String, sandi: String): User? {
        return userDao.findUserByEmailAndPassword(email, sandi)
    }
}