package com.example.depi_final_project

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.depi_final_project.databinding.JobLayoutAdapterBinding


class SavedJobsAdapter(context: Context) : RecyclerView.Adapter<SavedJobsAdapter.RemoteJobViewHolder>() {
    private val context = context
    private var binding: JobLayoutAdapterBinding? = null

    class RemoteJobViewHolder(itemBinding: JobLayoutAdapterBinding) :
        RecyclerView.ViewHolder(itemBinding.root)

    private val differCallback = object :
        DiffUtil.ItemCallback<JobToSave>() {
        override fun areItemsTheSame(
            oldItem: JobToSave,
            newItem: JobToSave
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: JobToSave,
            newItem: JobToSave
        ): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RemoteJobViewHolder {
        binding = JobLayoutAdapterBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return RemoteJobViewHolder(binding!!)
    }

    override fun onBindViewHolder(holder: RemoteJobViewHolder, position: Int) {
        val currentJob = differ.currentList[position]

        holder.itemView.apply {

            Glide.with(this)
                .load(currentJob.companyLogoUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.drawable.job)
                .into(binding?.jobImage!!)

            binding?.companyName?.text = currentJob.companyName
            binding?.jobTitle?.text = currentJob.title
            binding?.salary?.text = currentJob?.salary ?: "Not specified".toString()

            binding?.Date?.text = currentJob.publicationDate?.toString()
            binding?.location?.text = currentJob.location

        }
        binding?.root?.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW,currentJob.url?.toUri())
            context.startActivity(intent)
        }
        binding?.saveIcon?.isVisible = false
    }
    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}