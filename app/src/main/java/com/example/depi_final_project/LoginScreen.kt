package com.example.depi_final_project

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Delay
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
class LoginScreen : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth;
    private lateinit var tvNewAcc: TextView
    private lateinit var btnLogin: Button
    private lateinit var edLoginEmilAddress: EditText
    private lateinit var edLoginPass: EditText
    private var isAllFieldsChecked = false

    @OptIn(InternalCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_screen)

        val sharedPreferences = getSharedPreferences("UserPreferences", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val email = sharedPreferences.getString("email", "")
        val password = sharedPreferences.getString("password", "")

        auth = Firebase.auth
        btnLogin = findViewById(R.id.btn_login)
        tvNewAcc = findViewById(R.id.tv_new_acc)
        edLoginEmilAddress = findViewById(R.id.ed_login_emil_address)
        edLoginPass = findViewById(R.id.ed_login_pass)

        edLoginEmilAddress.text = Editable.Factory.getInstance().newEditable(email)
        edLoginPass.text = Editable.Factory.getInstance().newEditable(password)

        if(intent.getStringExtra("email").toString().isNotBlank()) {
            edLoginEmilAddress.text = Editable.Factory.getInstance().newEditable(intent.getStringExtra("email"))
        }

        btnLogin.setOnClickListener {
            isAllFieldsChecked = checkAllFields(edLoginEmilAddress, edLoginPass)

            if (isAllFieldsChecked) {
                val email = edLoginEmilAddress.text.toString()
                val password = edLoginPass.text.toString()
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            Log.d(TAG, "signInWithEmail:success")
                            val user = auth.currentUser
                            editor.putString("email", email)
                            editor.putString("password", password)
                            editor.apply()
                            val loginToBodyIntent = Intent(this, MainActivity::class.java)
                            startActivity(loginToBodyIntent)
                            finishAffinity()
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.exception)
                            Toast.makeText(
                                baseContext,
                                "Authentication failed.",
                                Toast.LENGTH_SHORT,
                            ).show()
                        }
                    }
            }
        }

        tvNewAcc.setOnClickListener {
            val loginToRegisterIntent = Intent(this@LoginScreen, RegisterScreen::class.java)
            startActivity(loginToRegisterIntent)
        }
    }

    private fun checkAllFields(
        edLoginEmilAddr: EditText,
        edLoginPassword: EditText
    ): Boolean {
        val emailToText = edLoginEmilAddr.text.toString().trim()
        val passToText = edLoginPassword.text.toString().trim()
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"

        if (emailToText.isEmpty()) {
            edLoginEmilAddr.error = "Email is required"
            return false
        } else if (!emailToText.matches(emailPattern.toRegex())) {
            edLoginEmilAddr.error = "Enter valid Email Address!"
            return false
        }

        if (passToText.isEmpty()) {
            edLoginPassword.error = "Password is required"
            return false
        } else if (passToText.length < 8) {
            edLoginPassword.error = "Password must be minimum 8 characters"
            return false
        }

        return true
    }
}