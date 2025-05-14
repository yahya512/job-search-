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
import androidx.core.view.isVisible
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.example.depi_final_project.databinding.ActivityMainBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.toString

//
class MainActivity : AppCompatActivity() {
    val user = Firebase.auth.currentUser
    val db = Firebase.firestore
    lateinit var remoteJobAdapter: RemoteJobAdapter
    lateinit var binding:ActivityMainBinding;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val userId =user?.uid
        val docRef = db.collection("users").document(userId.toString())
        val searchJob = binding.searchJobEditText
        val company_name = binding.CompanyNameEditText


        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    Log.d(TAG, "DocumentSnapshot data: ${document.data}")
                    val userName = document.get("fullname").toString() ?: "Guest"
                    binding.Jobtitle.text = "Welcome, $userName"
                } else {
                    Log.d(TAG, "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
            }
        remoteJobAdapter = RemoteJobAdapter(this@MainActivity)
        binding.jobsRecyclerView.adapter = remoteJobAdapter // Set to your RecyclerView

        getData()
        binding.searchButton.setOnClickListener {
            if (searchJob.text.isBlank() && company_name.text.isBlank()) {
                searchJob.error = "Please enter a job title"
                company_name.error = "Please enter a location"
                Toast.makeText(this, "Please enter one of the fields", Toast.LENGTH_SHORT).show()
            }else if (searchJob.text.isNotBlank() && company_name.text.isNotBlank())
                getData(searchJob.text.toString(), company_name.text.toString())
            else if (searchJob.text.isNotBlank() && company_name.text.isBlank())
                getData(searchJob.text.toString(), null)
            else
                getData(null, company_name.text.toString())
        }
        binding.notificationIcon.setOnClickListener {
            Toast.makeText(this, "Notification Icon Clicked", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, Notification::class.java)
            startActivity(intent)
        }
        binding.swiperefresh.setOnRefreshListener {
            getData()
            binding.searchJobEditText.text = null
            binding.CompanyNameEditText.text = null
            binding.swiperefresh.isRefreshing = false
        }

        binding.bottomNavigation.selectedItemId = R.id.nav_home

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    // Handle home click
                    true
                }
                R.id.nav_save -> {
                    // Open Saved Jobs Activity
                    val intent = Intent(this, SavedJob::class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_profile -> {
                    Toast.makeText(this, "Profile Icon Clicked", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }

    }
    private fun getData(job: String? = null, company_name: String? = null) {
        binding.progressBar.isVisible = true
        binding.jobsRecyclerView.isVisible = false
        RetrofitInstance.api.getRemoteJob(job, company_name).enqueue(object : Callback<RemoteJob> {
            override fun onResponse(call: Call<RemoteJob>, response: Response<RemoteJob>) {
                if (response.isSuccessful) {
                    response.body()?.let { remoteJob ->
                        remoteJobAdapter.differ.submitList(remoteJob.jobs)
                        if(job != null)
                            binding.RecommendedText.text = "$job Job Opportunities (${remoteJob.jobCount.toString()})"
                        else if(company_name != null)
                            binding.RecommendedText.text = "$company_name Job Opportunities (${response.body()?.jobCount.toString()})"
                        else
                            binding.RecommendedText.text = "Job Opportunities (${response.body()?.jobCount.toString()})"
                        binding.progressBar.isVisible = false
                        binding.jobsRecyclerView.isVisible = true
                    }
                } else {
                    Toast.makeText(this@MainActivity, "Failed to load data", Toast.LENGTH_SHORT).show()
                    binding.progressBar.isVisible = false
                }
            }

            override fun onFailure(call: Call<RemoteJob>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                binding.progressBar.isVisible = false
            }
        })
    }

}
