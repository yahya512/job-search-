package com.example.depi_final_project.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.depi_final_project.JobToSave
import com.example.depi_final_project.repositry.RemoteJobRepository
import kotlinx.coroutines.launch


class RemoteJobViewModel(
    app: Application,
    private val remoteJobRepository: RemoteJobRepository
) : AndroidViewModel(app) {

    fun insertJob(job: JobToSave) = viewModelScope.launch {
        remoteJobRepository.insertJob(job)
    }

    fun getAllJob() = remoteJobRepository.getAllJobs()

}