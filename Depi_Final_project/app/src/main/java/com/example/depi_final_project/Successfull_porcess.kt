package com.example.depi_final_project

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.depi_final_project.databinding.ActivitySuccessfullPorcessBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Successfull_porcess : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val binding = ActivitySuccessfullPorcessBinding.inflate(layoutInflater)
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

        val userName = intent.getStringExtra("user_name")
        binding.tvName.text= "Hi, $userName";
    }
}