package com.example.depi_final_project.repositry

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.depi_final_project.JobToSave
import com.example.depi_final_project.RemoteJob
import com.example.depi_final_project.RetrofitInstance
import com.example.depi_final_project.db.RemoteJobDatabase
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RemoteJobRepository(private val db: RemoteJobDatabase) {

    private val remoteJobService = RetrofitInstance.api
    private val remoteJobResponseLiveData: MutableLiveData<RemoteJob> = MutableLiveData()


    init {

        getRemoteJobResponse()
    }

    private fun getRemoteJobResponse() {

        remoteJobService.getRemoteJob().enqueue(
            object : Callback<RemoteJob> {
                override fun onResponse(call: Call<RemoteJob>, response: Response<RemoteJob>) {
                    if (response.body() != null) {
                        remoteJobResponseLiveData.postValue(response.body())
                    }
                }

                override fun onFailure(call: Call<RemoteJob>, t: Throwable) {
                    remoteJobResponseLiveData.postValue(null)
                    Log.d("error ibm", t.message.toString())
                }
            })
    }


    suspend fun insertJob(job: JobToSave) = db.getRemoteJobDao().insertJob(job)
    fun getAllJobs() = db.getRemoteJobDao().getAllJob()

}