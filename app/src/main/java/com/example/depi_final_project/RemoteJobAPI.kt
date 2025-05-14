package com.example.depi_final_project

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface RemoteJobAPI {

    @GET("remote-jobs")
    fun getRemoteJob(
        @Query("search") query: String? = null,
        @Query("company_name") company_name: String? = null
    ): Call<RemoteJob>

}