package com.example.data.repository

import androidx.lifecycle.LiveData
import com.example.data.source.local.dao.PhotoDao
import com.example.domain.model.PhotoLocation
import com.example.domain.repository.PhotoDataBaseRepository
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class PhotoDataBaseRepositoryImpl @Inject constructor(
    private val photoDao: PhotoDao
) : PhotoDataBaseRepository {
    override val allDataPhoto: LiveData<List<PhotoLocation>> = photoDao.readAllData()

    override suspend fun add(photoLocation: PhotoLocation) = photoDao.add(photoLocation)

    override suspend fun delete(photoLocation: PhotoLocation) = photoDao.delete(photoLocation)

    override suspend fun update(photoLocation: PhotoLocation) = photoDao.update(photoLocation)
}