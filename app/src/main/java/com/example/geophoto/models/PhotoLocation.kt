package com.example.geophoto.models

import android.graphics.Bitmap
import android.location.Address
import android.net.Uri
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "photo_table")
data class PhotoLocation(
	@PrimaryKey
	val photo: Uri,
	val address: String,
	val latitude: Double,
	val longitude: Double,
	val comment: String
	):Parcelable