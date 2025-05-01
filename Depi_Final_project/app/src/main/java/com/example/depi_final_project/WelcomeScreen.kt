package com.example.depi_final_project

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class WelcomeScreen : AppCompatActivity() {
    private lateinit var btnWelcomeLogin: Button
    private lateinit var btnWelcomeRegister: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome_screen)

        btnWelcomeLogin = findViewById(R.id.btn_welcome_login)
        btnWelcomeRegister = findViewById(R.id.btn_welcome_register)

        btnWelcomeLogin.setOnClickListener {
            val loginIntent = Intent(this@WelcomeScreen, LoginScreen::class.java)
            startActivity(loginIntent)
            finish()
        }

        btnWelcomeRegister.setOnClickListener {
            val registerIntent = Intent(this@WelcomeScreen, RegisterScreen::class.java)
            startActivity(registerIntent)
            finish()
        }
    }
}