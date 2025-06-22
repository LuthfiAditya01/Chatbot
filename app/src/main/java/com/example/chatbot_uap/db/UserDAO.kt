package com.example.chatbot_uap.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDAO {

    // Fungsi buat nambahin user baru pas register
    // onConflict = OnConflictStrategy.IGNORE artinya kalo ada data yg sama, diemin aja.
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addUser(user: User)

    // Fungsi buat ngecek user pas login
    // Kita cari user berdasarkan email dan sandi yang diinput.
    @Query("SELECT * FROM user_table WHERE email = :email AND sandi = :sandi LIMIT 1")
    suspend fun findUserByEmailAndPassword(email: String, sandi: String): User?
}