package com.example.domain.repository

import androidx.lifecycle.LiveData
import com.example.domain.model.PhotoLocation

interface PhotoDataBaseRepository {
    val allDataPhoto: LiveData<List<PhotoLocation>>
    suspend fun add(photoLocation: PhotoLocation)
    suspend fun delete(photoLocation: PhotoLocation)
    suspend fun update(photoLocation: PhotoLocation)
}