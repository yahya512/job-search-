package com.example.depi_final_project.viewmodel


import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.depi_final_project.repositry.RemoteJobRepository

class RemoteJobViewModelFactory(
    val app: Application,
    private val remoteJobRepository: RemoteJobRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return RemoteJobViewModel(app, remoteJobRepository) as T
    }

}
