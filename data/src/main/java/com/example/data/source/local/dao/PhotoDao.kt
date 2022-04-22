package com.example.data.source.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.domain.model.PhotoLocation


@Dao
interface PhotoDao {

    @Query("SELECT * FROM photo_table")
    fun readAllData(): LiveData<List<PhotoLocation>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun add(photoLocation: PhotoLocation)

    @Delete
    suspend fun delete(photoLocation: PhotoLocation)

    @Update
    suspend fun update(photoLocation: PhotoLocation)
}