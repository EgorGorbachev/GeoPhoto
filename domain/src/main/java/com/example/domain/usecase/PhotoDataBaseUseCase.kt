package com.example.domain.usecase

import androidx.lifecycle.LiveData
import com.example.domain.model.PhotoLocation
import com.example.domain.repository.PhotoDataBaseRepository
import javax.inject.Inject

class PhotoDataBaseUseCase @Inject constructor(
    private val repository: PhotoDataBaseRepository
) {
    val allDataPhoto: LiveData<List<PhotoLocation>> = repository.allDataPhoto

    suspend fun add(photoLocation: PhotoLocation) = repository.add(photoLocation)
    suspend fun delete(photoLocation: PhotoLocation) = repository.delete(photoLocation)
    suspend fun update(photoLocation: PhotoLocation) = repository.update(photoLocation)
}