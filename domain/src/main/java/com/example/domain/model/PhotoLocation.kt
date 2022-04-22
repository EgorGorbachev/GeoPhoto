package com.example.domain.model

import android.net.Uri
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "photo_table")
data class PhotoLocation(
    @PrimaryKey
    val photo: Uri,
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val comment: String
) : Parcelable