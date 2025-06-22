package com.example.chatbot_uap.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [User::class], version = 1, exportSchema = false)
abstract class UserDatabase : RoomDatabase() {

    abstract fun userDao(): UserDAO

    /**
     * Jadi gini, ini tuh kayak 'besti'-nya si `UserDatabase`.
     *
     * Tugasnya tuh simpel, cuma nyediain satu doang instance dari `UserDatabase` pake method `getDatabase`.
     * Biar apa? Biar gak kebanyakan database instance gitu lho, jadi cuma satu aja yang dipake di seluruh aplikasi.
     * Hemat resource, brokk! ✨
     */
    companion object {
        @Volatile
        private var INSTANCE: UserDatabase? = null

        fun getDatabase(context: Context): UserDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    UserDatabase::class.java,
                    "user_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}