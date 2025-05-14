package com.example.depi_final_project

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.depi_final_project.databinding.ActivityForm1Binding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class Form1 : AppCompatActivity() {
    val user = Firebase.auth.currentUser
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val binding = ActivityForm1Binding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val user_id = user?.uid ?: "No User ID";
        val fullname = binding.etFullName;
        val email = binding.etEmail;
        val phone = binding.etPhone;
        val location = binding.etLocation;

        binding.btnNext.setOnClickListener {
            if (validateAllFields(fullname, email, phone, location)) {
                intent = Intent(this, Form2::class.java)
                intent.apply{
                    putExtra("user_id", user_id)
                    putExtra("fullname", fullname.text.toString())
                    putExtra("email", email.text.toString())
                    putExtra("phone", phone.text.toString())
                    putExtra("location", location.text.toString())
                    startActivity(this)
                }
            }
        }
    }
    private fun validateAllFields(fullname:EditText, email:EditText, phone:EditText, location:EditText): Boolean {
        var isValid = true

        if (fullname.text.isNullOrBlank()) {
            fullname.error = "Full name is required"
            isValid = false
        }
        if (email.text.isBlank()) {
            email.error = "Email is required"
            isValid = false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email.text).matches()) {
            email.error = "Enter a valid email"
            isValid = false
        }

        if (phone.text.isBlank()) {
            phone.error = "Phone number is required"
            isValid = false
        } else if (phone.text.toString().length < 10) {
            phone.error = "Enter a valid phone number"
            isValid = false
        }

        if (location.text.isNullOrBlank()) {
            location.error = "Location is required"
            isValid = false
        }

        return isValid
    }

}