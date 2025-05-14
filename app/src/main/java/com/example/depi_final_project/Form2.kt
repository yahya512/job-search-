package com.example.depi_final_project

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.depi_final_project.databinding.ActivityForm1Binding
import com.example.depi_final_project.databinding.ActivityForm2Binding

class Form2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val binding = ActivityForm2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val portfolio = binding.etPortfolio;
        val profile_summary = binding.etProfileSummary;
        setupJobTitleSpinner(binding.spinnerJobTitle);

        binding.btnNext.setOnClickListener {
            if (validateAllFields(portfolio, binding.spinnerJobTitle)) {
                Intent(this, Form3::class.java).apply {
                    intent.extras?.let { putExtras(it) }
                    putExtra("portfolio", portfolio.text.toString())
                    putExtra("profile_summary", profile_summary.text.toString())
                    putExtra("job_title", binding.spinnerJobTitle.selectedItem.toString())
                    startActivity(this)
                }
            }
        }
        binding.btnBack.setOnClickListener {
            intent = Intent(this, Form1::class.java)
            startActivity(intent)
        }
    }
    private fun validateAllFields(Portfolio: EditText, spinner: Spinner): Boolean {
        var isValid = true

        // Validate Full Name
        if (Portfolio.text.isNullOrBlank()) {
            Portfolio.error = "Portfolio is required"
            isValid = false
        }
        if (!validateJobTitle(spinner)) {
            isValid = false
        }
        return isValid
    }
    private fun setupJobTitleSpinner(spinner: Spinner) {
        // Define your job titles list
        val jobTitles = arrayOf(
            "Select job title",
            "Software Engineer",
            "Product Manager",
            "UX Designer",
            "Data Scientist",
            "DevOps Engineer",
            "QA Engineer",
            "System Administrator",
            "Mobile Developer",
            "Web Developer",
            "Other"
        )

        // Create adapter for spinner
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            jobTitles
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        spinner.adapter = adapter

        // Handle selection
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position > 0) {
                    val selectedJob = jobTitles[position]
                }
        }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
    }

    // Add this to your validation function
    private fun validateJobTitle(spinner: Spinner): Boolean {
        return if (spinner.selectedItemPosition == 0) {
            Toast.makeText(this, "Please select a job title", Toast.LENGTH_SHORT).show()
            spinner.performClick()
            false
        } else {
            true
        }
    }

}