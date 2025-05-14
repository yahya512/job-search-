package com.example.depi_final_project

import android.location.Location
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "job")
@Parcelize
data class JobToSave(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0, // Add default value for auto-generation
    val companyLogoUrl: String?,
    val companyName: String?,
    val publicationDate: String?,
    val salary: String?,
    val title: String?,
    val url: String?,
    val location: String?,
) : Parcelable
