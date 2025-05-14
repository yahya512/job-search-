package com.example.depi_final_project

import android.app.Application
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.depi_final_project.databinding.JobLayoutAdapterBinding
import com.example.depi_final_project.db.RemoteJobDatabase
import com.example.depi_final_project.repositry.RemoteJobRepository
import com.example.depi_final_project.viewmodel.RemoteJobViewModel
import com.example.depi_final_project.viewmodel.RemoteJobViewModelFactory

class RemoteJobAdapter(private val context: AppCompatActivity) :
    RecyclerView.Adapter<RemoteJobAdapter.RemoteJobViewHolder>() {

    private val differCallback = object : DiffUtil.ItemCallback<JobData>() {
        override fun areItemsTheSame(oldItem: JobData, newItem: JobData): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: JobData, newItem: JobData): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    inner class RemoteJobViewHolder(val binding: JobLayoutAdapterBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RemoteJobViewHolder {
        val binding = JobLayoutAdapterBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return RemoteJobViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RemoteJobViewHolder, position: Int) {
        val currentJob = differ.currentList[position]
        val binding = holder.binding

        Glide.with(context)
            .load(currentJob.companyLogoUrl)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .error(R.drawable.job)
            .into(binding.jobImage)

        binding.companyName.text = currentJob.companyName
        binding.jobTitle.text = currentJob.title
        binding.salary.text = currentJob.salary ?: "Not specified"
        binding.Date.text = currentJob.publicationDate?.split("T")?.get(0)
        binding.location.text = currentJob.candidate_required_location


        binding.saveIcon.setOnClickListener {
        val jobToSave = JobToSave(
        companyLogoUrl = currentJob.companyLogoUrl,
        companyName = currentJob.companyName,
        publicationDate = currentJob.publicationDate,
        salary = currentJob.salary,
        title = currentJob.title,
        url = currentJob.url,
        location = currentJob.candidate_required_location,
        )

            val db = RemoteJobDatabase(context.applicationContext)
            val repository = RemoteJobRepository(db)
            val factory = RemoteJobViewModelFactory(context.applicationContext as Application, repository)
            val viewModel = ViewModelProvider(context as ViewModelStoreOwner, factory)
                .get(RemoteJobViewModel::class.java)

            viewModel.insertJob(jobToSave)
            Toast.makeText(context, "Job saved", Toast.LENGTH_SHORT).show()
            binding.saveIcon.setImageResource(R.drawable.ic_saved)
        }

        binding.root.setOnClickListener {
            val intent = Intent(context, JobDetails::class.java)
            intent.putExtra("job_url", currentJob.url)
            intent.putExtra("job_title", currentJob.title)
            intent.putExtra("job_company", currentJob.companyName)
            intent.putExtra("job_location", currentJob.candidate_required_location)
            intent.putExtra("job_salary", currentJob.salary)
            intent.putExtra("job_date", currentJob.publicationDate)
            intent.putExtra("job_logo", currentJob.companyLogoUrl)
            context.startActivity(intent)
        }


    }

    override fun getItemCount(): Int = differ.currentList.size
}