package com.example.geophoto.repositories

import android.provider.MediaStore
import androidx.lifecycle.LiveData
import com.example.geophoto.models.PhotoLocation
import com.example.geophoto.repositories.photo_database.PhotoDao
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class PhotoRepository @Inject constructor(
	private val photoDao: PhotoDao
) {
	val allDataPhoto: LiveData<List<PhotoLocation>> = photoDao.readAllData()
	
	suspend fun add(photoLocation: PhotoLocation) {
		photoDao.add(photoLocation)
	}
	
	suspend fun delete(photoLocation: PhotoLocation) {
		photoDao.delete(photoLocation)
	}
	
	suspend fun update(photoLocation: PhotoLocation) {
		photoDao.update(photoLocation)
	}
}