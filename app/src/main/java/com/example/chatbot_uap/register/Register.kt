package com.example.chatbot_uap.register

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.chatbot_uap.R
import com.example.chatbot_uap.db.User
import com.example.chatbot_uap.login.Login
import com.example.chatbot_uap.viewmodel.UserViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Register : AppCompatActivity() {

    private lateinit var userViewModel: UserViewModel

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register) // Ini udah bener, manggil layout keren lo

        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        // --- PENYESUAIAN ID DIMULAI DARI SINI ---

        // DIUBAH: Sesuaikan dengan ID EditText email di XML baru lo
        val emailInput = findViewById<EditText>(R.id.inputEmail)

        // DIUBAH: Sesuaikan dengan ID EditText password di XML baru lo
        val passwordInput = findViewById<EditText>(R.id.inputPassword)

        // DIUBAH: Sesuaikan dengan ID Button di XML baru lo
        val registerButton = findViewById<Button>(R.id.registerButton)

        // TETAP: ID untuk "Masuk di sini" udah sama
        val tvLogin = findViewById<TextView>(R.id.tvMasukDiSini)

        registerButton.setOnClickListener {
            val email = emailInput.text.toString()
            val password = passwordInput.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                // DIUBAH TOTAL: Kita bungkus semua pake coroutine scope
                lifecycleScope.launch(Dispatchers.IO) { // Proses berat (db) di background
                    val user = User(email = email, sandi = password)
                    userViewModel.addUser(user) // Panggil suspend function, ini bakal ditungguin sampe selesai

                    // Pindah ke Main Thread buat nampilin Toast & Pindah Halaman
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@Register, "Registrasi Berhasil!", Toast.LENGTH_SHORT)
                            .show()
                        val intent = Intent(this@Register, Login::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
            } else {
                Toast.makeText(this, "Email dan Password Wajib Diisi!", Toast.LENGTH_SHORT).show()
            }
        }

        tvLogin.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
            finish()
        }
    }
}