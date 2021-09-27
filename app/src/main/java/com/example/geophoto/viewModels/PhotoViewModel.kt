package com.example.geophoto.viewModels

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.geophoto.models.PhotoLocation
import com.example.geophoto.repositories.PhotoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhotoViewModel @Inject constructor(
	private val repository: PhotoRepository
) : ViewModel() {
	
	var currentAddress:String? = null
	var currentPhoto: Uri? = null
	
	fun insertDatabase(photoLocation: PhotoLocation) {
		GlobalScope.launch {
			repository.add(photoLocation)
		}
	}
	
	
}