package com.example.depi_final_project

import com.google.gson.annotations.SerializedName

data class RemoteJob(
    @SerializedName("job-count")
    val jobCount: Int?,
    val jobs: List<JobData>?,
    val legalNotice: String?
)

