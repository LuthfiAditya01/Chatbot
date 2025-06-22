package com.example.chatbot_uap.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

/**
 * Gini bro, ini tuh Data Access Object (DAO) buat si User.
 * Jadi, ini tuh isinya function-function buat ngobrol sama tabel 'user_table' di database gitu.
 * Kayak mau nambahin user baru, atau nyari user, nah lewat sini semua.
 */
@Dao
interface UserDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addUser(user: User)

    // DIUBAH: Kita cuma butuh cari user berdasarkan email
    @Query("SELECT * FROM user_table WHERE email = :email LIMIT 1")
    suspend fun findUserByEmail(email: String): User?
}