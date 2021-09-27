package com.example.geophoto.models

import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PhotoLatLng(
	val lat: Double,
	val lng: Double
):Parcelable