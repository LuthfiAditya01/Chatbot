package com.example.chatbot_uap.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.security.SecureRandom
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec
import java.util.Base64

object HashingUtils {

    private const val ALGORITHM = "PBKDF2WithHmacSHA1"
    private const val ITERATIONS = 65536
    private const val KEY_LENGTH = 256
    private const val SALT_SIZE = 16

    // Fungsi ini buat bikin hash dari password
    @RequiresApi(Build.VERSION_CODES.O)
    fun hashPassword(password: String): String {
        // 1. Bikin 'bumbu rahasia' (salt) acak yang aman
        val random = SecureRandom()
        val salt = ByteArray(SALT_SIZE)
        random.nextBytes(salt)

        // 2. Proses hashing pake PBEKeySpec
        val spec = PBEKeySpec(password.toCharArray(), salt, ITERATIONS, KEY_LENGTH)
        val factory = SecretKeyFactory.getInstance(ALGORITHM)
        val hash = factory.generateSecret(spec).encoded

        // 3. Gabungin salt dan hash jadi satu string buat disimpen di database
        // Kita encode ke Base64 biar jadi teks yang aman
        val b64Salt = Base64.getEncoder().encodeToString(salt)
        val b64Hash = Base64.getEncoder().encodeToString(hash)

        return "$b64Salt:$b64Hash"
    }

    // Fungsi ini buat ngecek password pas login
    @RequiresApi(Build.VERSION_CODES.O)
    fun verifyPassword(password: String, storedHash: String): Boolean {
        try {
            // 1. Pisahin lagi antara salt dan hash dari string yang disimpen
            val parts = storedHash.split(":")
            val salt = Base64.getDecoder().decode(parts[0])
            val hash = Base64.getDecoder().decode(parts[1])

            // 2. Bikin hash dari password yang diinput user, TAPI PAKE SALT YANG LAMA
            val spec = PBEKeySpec(password.toCharArray(), salt, ITERATIONS, KEY_LENGTH)
            val factory = SecretKeyFactory.getInstance(ALGORITHM)
            val testHash = factory.generateSecret(spec).encoded

            // 3. Bandingin hash yang baru dibuat sama hash yang ada di database
            // Kalo sama, berarti passwordnya bener
            return hash.contentEquals(testHash)
        } catch (e: Exception) {
            // Kalo format storedHash salah atau ada error lain, anggap aja gagal
            return false
        }
    }
}