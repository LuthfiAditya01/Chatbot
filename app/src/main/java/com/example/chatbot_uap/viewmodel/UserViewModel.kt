package com.example.chatbot_uap.viewmodel

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatbot_uap.db.User
import com.example.chatbot_uap.db.UserDatabase
import com.example.chatbot_uap.utils.HashingUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserViewModel(application: Application) : AndroidViewModel(application) {
    private val userDao = UserDatabase.getDatabase(application).userDao()

    // DIUBAH: Sebelum nyimpen, passwordnya kita hash dulu
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun addUser(user: User) {
        // Buat user baru dengan password yang sudah di-hash
        val hashedUser = user.copy(sandi = HashingUtils.hashPassword(user.sandi))
        userDao.addUser(hashedUser)
    }

    // DIUBAH: Logika login sekarang di sini
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun loginUser(email: String, plainPassword: String): User? {
        // 1. Cari user berdasarkan email dulu
        val user = userDao.findUserByEmail(email)

        // 2. Kalo user-nya ada, baru kita verifikasi passwordnya
        if (user != null && HashingUtils.verifyPassword(plainPassword, user.sandi)) {
            return user // Kalo password cocok, balikin data user
        }

        return null // Kalo user nggak ada atau password salah, balikin null
    }
}