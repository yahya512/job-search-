package com.example.depi_final_project

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegisterScreen : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth;
    private lateinit var btnRegister: Button
    private lateinit var tvHaveAcc: TextView
    private lateinit var edRegisterEmilAddress: EditText
    private lateinit var edRegisterPass: EditText
    private lateinit var edRegisterConfirmPass: EditText
    private var isAllFieldsChecked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_screen)
        // Initialize Firebase Auth
        auth = Firebase.auth;

        btnRegister = findViewById(R.id.btn_register)
        tvHaveAcc = findViewById(R.id.tv_have_acc)
        edRegisterEmilAddress = findViewById(R.id.ed_register_emil_address)
        edRegisterPass = findViewById(R.id.ed_register_pass)
        edRegisterConfirmPass = findViewById(R.id.ed_register_confirm_pass)

        btnRegister.setOnClickListener {
            isAllFieldsChecked = checkAllFields(edRegisterEmilAddress, edRegisterPass, edRegisterConfirmPass)

            if (isAllFieldsChecked) {
                val email = edRegisterEmilAddress.text.toString()
                val password = edRegisterPass.text.toString()
                registerNewUser(email, password);
            }
        }

        tvHaveAcc.setOnClickListener {
            val registerTologinIntent = Intent(this@RegisterScreen, LoginScreen::class.java)
            startActivity(registerTologinIntent)
            finish()
        }
    }

    private fun checkAllFields(
        edRegisterEmilAddr: EditText,
        edRegisterPassword: EditText,
        edRegisterConfirmPassword: EditText
    ): Boolean {
        val emailToText = edRegisterEmilAddr.text.toString().trim()
        val passToText = edRegisterPassword.text.toString().trim()
        val confirmPassToText = edRegisterConfirmPassword.text.toString().trim()
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"

        if (emailToText.isEmpty()) {
            edRegisterEmilAddr.error = "Email is required"
            return false
        } else if (!emailToText.matches(emailPattern.toRegex())) {
            edRegisterEmilAddr.error = "Enter valid Email Address!"
            return false
        }

        if (passToText.isEmpty()) {
            edRegisterPassword.error = "Password is required"
            return false
        } else if (passToText.length < 8) {
            edRegisterPassword.error = "Password must be minimum 8 characters"
            return false
        }

        if (confirmPassToText.isEmpty()) {
            edRegisterConfirmPassword.error = "Password is required"
            return false
        } else if (confirmPassToText != passToText) {
            edRegisterConfirmPassword.error = "The Confirm Password Confirmation does not match"
            return false
        }

        // after all validation return true.
        return true
    }
    private fun registerNewUser(email: String, password: String){
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    val user = auth.currentUser
                    val registerToBodyIntent = Intent(this, Form1::class.java)
                    startActivity(registerToBodyIntent)
                    Toast.makeText(this, "Registration Successfully", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
    }
}