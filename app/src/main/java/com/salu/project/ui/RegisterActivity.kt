package com.salu.project.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.salu.project.R
import com.salu.project.api.RetrofitClient
import com.salu.project.model.User
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val etName = findViewById<EditText>(R.id.etName)
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnRegister = findViewById<Button>(R.id.btnRegister)
        val tvToLogin = findViewById<TextView>(R.id.tvToLogin)

        btnRegister.setOnClickListener {
            val name = etName.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val user = User(name, email, password, "user")

            lifecycleScope.launch {
                try {
                    val response = RetrofitClient.getUserApi(this@RegisterActivity).register(user)
                    Toast.makeText(this@RegisterActivity, "Registration Successful: ${response.name}", Toast.LENGTH_LONG).show()
                    finish() // Close and go back to Login
                } catch (e: Exception) {
                    Toast.makeText(this@RegisterActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }

        tvToLogin.setOnClickListener {
            finish()
        }
    }
}
