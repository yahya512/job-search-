package com.example.depi_final_project

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.depi_final_project.databinding.ActivityJobDetailsBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class JobDetails : AppCompatActivity() {
    private lateinit var binding: ActivityJobDetailsBinding
    val db = Firebase.firestore
    val user = Firebase.auth.currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityJobDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        /*
        binding.button.setOnClickListener {
            val i = Intent(this, MainActivity::class.java)
            val snackbar = Snackbar
                .make(binding.root, "Button Clicked", Snackbar.LENGTH_SHORT)
                .setAction("undo") { startActivity(i) }
            snackbar.show()
        }*/

        binding.webView.apply {
            loadUrl(intent.getStringExtra("job_url").toString())
            settings.javaScriptEnabled = true
        }

        val user_id = user?.uid ?: "No User ID";
        val companyName = intent.getStringExtra("job_company")
        val jobTitle = intent.getStringExtra("job_title")
        val jobSalary = intent.getStringExtra("job_salary")
        val jobDate = intent.getStringExtra("job_date")
        val companyLogoUrl = intent.getStringExtra("job_logo")
        val jobUrl = intent.getStringExtra("job_url")

        binding.ApplyButton.setOnClickListener {
            val jobToSave = hashMapOf(
                "companyLogoUrl" to companyLogoUrl,
                "companyName " to companyName,
                "publicationDate" to jobDate,
                "salary" to  jobSalary,
                "title" to  jobTitle,
                "url" to jobUrl,
                "user_id" to user_id,
                "state" to " ",
            )


            db.collection("JobTracking")
                .document()
                .set(jobToSave)
                .addOnSuccessListener {
                    AlertDialog.Builder(this)
                        .setTitle("Application Submitted Successfully !")
                        .setMessage("Check the Tracking screen for updates.")
                        .setPositiveButton("OK") { dialog, _ ->
                            dialog.dismiss()
                        }
                        .show()
                }
                .addOnFailureListener { e ->
                    Log.w("TAG", "Error writing document", e)
                    Toast.makeText(this, "Error writing document ${e.message}", Toast.LENGTH_SHORT).show();
                }


        }
    }
}
