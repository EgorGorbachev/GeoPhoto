package com.example.geophoto.viewModels

import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.geophoto.models.PhotoLatLng
import com.example.geophoto.models.PhotoLocation
import com.example.geophoto.repositories.PhotoRepository
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhotoDetailsViewModel @Inject constructor(
	private val repository: PhotoRepository
) : ViewModel() {
	
	var photoDetailsAddress: String? = null
	var photoDetailsPhoto: Uri? = null
	var photoDetailsLatLng: LatLng? = null
	var photoDetailsComment: String? = null
	
	fun insertDatabase(photoLocation: PhotoLocation) {
		GlobalScope.launch {
			repository.add(photoLocation)
		}
	}
}