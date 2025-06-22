package com.example.chatbot_uap

import com.example.chatbot_uap.BuildConfig
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import java.io.IOException
import io.noties.markwon.Markwon

class MainActivity : AppCompatActivity() {
    private lateinit var markwon: Markwon
    val API_KEY = BuildConfig.API_KEY
    private val GEMINI_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash-latest:generateContent?key=$API_KEY"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        markwon = Markwon.create(this)
        val chatLayout = findViewById<LinearLayout>(R.id.chatLayout)
        val input = findViewById<EditText>(R.id.inputMessage)
        val sendButton = findViewById<ImageButton>(R.id.sendButton)

        sendButton.setOnClickListener {
            val userMessage = input.text.toString()
            input.setText("")

            addMessage(chatLayout, "Kamu: $userMessage", false)
            getGeminiResponse(userMessage) { response ->
                runOnUiThread {
                    addMessage(chatLayout, "Gemini: $response", true)
                }
            }
        }
    }

    fun addMessage(layout: LinearLayout, message: String, isMarkdown: Boolean) {
        val textView = TextView(this)

        if (isMarkdown) {
            // Jika ini respons dari Gemini, biarkan Markwon yang mengatur teksnya
            markwon.setMarkdown(textView, message)
        } else {
            // Jika ini pesan dari pengguna, tampilkan seperti biasa
            textView.text = message
        }

        // Atur properti lain jika perlu (misal: padding, text size)
        textView.textSize = 16f
        textView.setPadding(8, 8, 8, 8)

        layout.addView(textView)
    }

    fun getGeminiResponse(prompt: String, callback: (String) -> Unit) {
        val client = OkHttpClient()
        val json = """
        {
          "contents": [
            {
              "parts": [
                { "text": "$prompt" }
              ]
            }
          ]
        }
        """.trimIndent()

        val body = RequestBody.create("application/json".toMediaTypeOrNull(), json)
        val request = Request.Builder()
            .url(GEMINI_URL)
            .post(body)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback("Gagal terhubung: ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()

                // =======================================================================
                // LANGKAH DEBUGGING PALING PENTING: CETAK SELURUH RESPON KE LOGCAT
                // Kita gunakan tag "GeminiResponse" untuk mempermudah pencarian.
                android.util.Log.d("GeminiResponse", "HTTP Code: ${response.code}")
                android.util.Log.d("GeminiResponse", "Body: $responseBody")
                // =======================================================================

                // Pastikan Anda menggunakan blok if-else yang sudah diperbaiki ini
                if (response.isSuccessful && responseBody != null) {
                    try {
                        val json = com.google.gson.JsonParser.parseString(responseBody).asJsonObject

                        if (json.has("candidates")) {
                            val rawText = json.getAsJsonArray("candidates")?.get(0)?.asJsonObject
                                ?.get("content")?.asJsonObject
                                ?.get("parts")?.asJsonArray?.get(0)?.asJsonObject
                                ?.get("text")?.asString ?: "Tidak ada respons dari parsing."
                            callback(rawText)
                        } else {
                            // Jika tidak ada 'candidates', coba cari 'error' atau info lain
                            val promptFeedback = json.get("promptFeedback")?.toString() ?: "Tidak ada detail."
                            callback("Respons tidak mengandung 'candidates'. Kemungkinan diblokir. Feedback: $promptFeedback")
                        }

                    } catch (e: Exception) {
                        callback("Kesalahan parsing respons: ${e.message}")
                    }
                } else {
                    // Jika respons gagal (e.g., error 400, 403, 500)
                    val errorCode = response.code
                    val errorBody = responseBody ?: "Body kosong"
                    android.util.Log.e("GeminiAPI", "Request Gagal: Code $errorCode, Body: $errorBody")
                    callback("Gagal mendapatkan respons dari server (Kode: $errorCode). Cek Logcat untuk detail.")
                }
            }
        })
    }
}