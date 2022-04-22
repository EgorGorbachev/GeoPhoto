package com.example.data.model.pojo

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PhotoLatLng(
    val lat: Double,
    val lng: Double
) : Parcelable