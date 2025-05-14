package com.example.depi_final_project

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.example.depi_final_project.databinding.ActivitySavedJobBinding
import com.example.depi_final_project.db.RemoteJobDatabase
import com.example.depi_final_project.repositry.RemoteJobRepository
import com.example.depi_final_project.viewmodel.RemoteJobViewModel
import com.example.depi_final_project.viewmodel.RemoteJobViewModelFactory

class SavedJob : AppCompatActivity() {
    private lateinit var binding: ActivitySavedJobBinding
    private lateinit var viewModel: RemoteJobViewModel
    private lateinit var adapter: SavedJobsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySavedJobBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setupViewModel()
        setupRecyclerView()
        setupObservers()
        fetchSavedJobs()
        binding.bottomNavigation.selectedItemId = R.id.nav_save
        binding.jobsRecyclerView.adapter = adapter

        binding.bottomNavigation.selectedItemId = R.id.nav_save
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    true}
                R.id.nav_save -> true

                R.id.nav_profile -> {
                    Toast.makeText(this, "Profile Icon Clicked", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.nav_track -> true
                else -> false
                }
        }
    }
    private fun setupViewModel() {
        val db = RemoteJobDatabase(applicationContext) // or `this.application`
        val repository = RemoteJobRepository(db)
        val factory = RemoteJobViewModelFactory(application, repository)
        viewModel = ViewModelProvider(this, factory).get(RemoteJobViewModel::class.java)
    }

    private fun setupRecyclerView() {
        adapter = SavedJobsAdapter(this@SavedJob)
        binding.progressBar.isVisible = false
        binding.jobsRecyclerView.adapter = adapter
    }
    private fun setupObservers() {
        viewModel.getAllJob().observe(this) { jobs ->
            adapter.differ.submitList(jobs as List<JobToSave?>?)
        }
    }
    private fun fetchSavedJobs() {
        viewModel.getAllJob()
    }

}