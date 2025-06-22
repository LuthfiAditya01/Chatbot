package com.example.chatbot_uap.register

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.chatbot_uap.R
import com.example.chatbot_uap.login.Login

class Register : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val tvLogin = findViewById<TextView>(R.id.tvMasukDiSini)
        tvLogin.setOnClickListener {
            // Pindah ke layar login
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
            finish() // Tutup halaman register agar tidak kembali ke sini saat tekan tombol back
        }
    }
}