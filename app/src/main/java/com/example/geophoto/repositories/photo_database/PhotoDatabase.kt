package com.example.geophoto.repositories.photo_database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.geophoto.models.PhotoLocation
import com.example.geophoto.presentation.util.Converters
import dagger.Provides


@Database(entities = [PhotoLocation::class], version = 4, exportSchema = false)
@TypeConverters(Converters::class)
abstract class PhotoDatabase : RoomDatabase() {
	abstract fun PhotoDao(): PhotoDao
}