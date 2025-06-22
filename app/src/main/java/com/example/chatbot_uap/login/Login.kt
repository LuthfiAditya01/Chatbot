package com.example.chatbot_uap.login

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.chatbot_uap.MainActivity
import com.example.chatbot_uap.R
import com.example.chatbot_uap.register.Register
import com.example.chatbot_uap.viewmodel.UserViewModel
import kotlinx.coroutines.launch

class Login : AppCompatActivity() {

    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        // --- SEMUA ID DI BAWAH INI UDAH COCOK SAMA LAYOUT LO ---

        val emailInput = findViewById<EditText>(R.id.etEmail)           // ID INI UDAH PAS!
        val passwordInput = findViewById<EditText>(R.id.etPassword)     // ID INI UDAH PAS!
        val loginButton = findViewById<Button>(R.id.btnLogin)           // ID INI UDAH PAS!
        val tvDaftar = findViewById<TextView>(R.id.tvDaftarDiSini) // ID INI UDAH PAS!

        // --- Logika di bawah sini udah siap tempur ---

        loginButton.setOnClickListener {
            val email = emailInput.text.toString()
            val password = passwordInput.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                lifecycleScope.launch {
                    val user = userViewModel.findUserByEmailAndPassword(email, password)
                    if (user != null) {
                        // Login Berhasil
                        Toast.makeText(this@Login, "Login Berhasil!", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@Login, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        // Login Gagal
                        Toast.makeText(this@Login, "Email atau Password Salah!", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Email dan Password Wajib Diisi!", Toast.LENGTH_SHORT).show()
            }
        }

        tvDaftar.setOnClickListener {
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
        }
    }
}