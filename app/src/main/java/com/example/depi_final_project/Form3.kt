package com.example.depi_final_project

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.depi_final_project.databinding.ActivityForm3Binding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Form3 : AppCompatActivity() {
    val db = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val binding = ActivityForm3Binding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val paddingPx = resources.getDimensionPixelSize(R.dimen.window_padding)
            v.setPadding(
                paddingPx + systemBars.left,
                paddingPx + systemBars.top,
                paddingPx + systemBars.right,
                paddingPx + systemBars.bottom
            )
            insets
        }
        binding.btnNext.setOnClickListener {
            val user_id = intent.getStringExtra("user_id")
            val fullname = intent.getStringExtra("fullname")
            val email = intent.getStringExtra("email")
            val phone = intent.getStringExtra("phone")
            val location = intent.getStringExtra("location")
            val portfolio = intent.getStringExtra("portfolio")
            val profile_summary = intent.getStringExtra("profile_summary")
            val job_title = intent.getStringExtra("job_title")
            // Create a new user with a first and last name
            val user = hashMapOf(
                "user_id" to user_id,
                "fullname" to fullname,
                "email" to email,
                "phone" to phone,
                "location" to location,
                "portfolio" to portfolio,
                "profile_summary" to profile_summary,
                "job_title" to job_title
            )
            db.collection("users")
                .document(user_id ?: "")
                .set(user)
                .addOnSuccessListener { documentReference ->
                    Intent(this, Successfull_porcess::class.java).apply {
                        putExtra("user_id", user.get("user_id").toString())
                        startActivity(this)
                        finish()
                    }
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error adding document", e)
                    Toast.makeText(this, "Error adding user ${e.message}", Toast.LENGTH_SHORT).show();
                }
        }
        binding.btnBack.setOnClickListener {
            intent = Intent(this, Form2::class.java)
            startActivity(intent)
        }
    }
}