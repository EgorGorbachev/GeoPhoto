package com.example.geophoto.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.geophoto.models.PhotoLocation
import com.example.geophoto.repositories.PhotoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GalleryViewModel @Inject constructor(
	private val repository: PhotoRepository
):ViewModel() {
	
	val allData: LiveData<List<PhotoLocation>> = repository.allDataPhoto
	
	fun deleteFromDatabase(photoLocation: PhotoLocation){
		GlobalScope.launch {
			repository.delete(photoLocation)
		}
	}
}