package com.example.chatbot_uap.login

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.chatbot_uap.MainActivity
import com.example.chatbot_uap.R
import com.example.chatbot_uap.register.Register

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val emailInput = findViewById<EditText>(R.id.etEmail)
        val passwordInput = findViewById<EditText>(R.id.etPassword)
        val loginButton = findViewById<Button>(R.id.btnLogin)
        val tvDaftar = findViewById<TextView>(R.id.tvDaftarDiSini)

        loginButton.setOnClickListener {
            val email = emailInput.text.toString()
            val password = passwordInput.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                emailInput.error = "Wajib diisi"
                passwordInput.error = "Wajib diisi"
            }
        }

        tvDaftar.setOnClickListener {
            // Pindah ke halaman Register
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
        }
    }
}