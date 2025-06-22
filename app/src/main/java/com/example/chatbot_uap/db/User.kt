package com.example.chatbot_uap.db

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Yo, jadi ini tuh kayak representasi user gitu di database, ngerti kan?
 *
 * @property id Ini ID unik buat si user, guys. Ntar dibikin otomatis sama database-nya, sans.
 * @property email Alamat email user-nya, ya kali gak tau.
 * @property sandi Nah, ini password-nya si user. Biar aman gitu, loh.
 */
@Entity(tableName = "user_table")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val email: String,
    val sandi: String
)
