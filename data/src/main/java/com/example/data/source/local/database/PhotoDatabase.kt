package com.example.data.source.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.data.source.local.dao.PhotoDao
import com.example.data.util.Converters
import com.example.domain.model.PhotoLocation


@Database(entities = [PhotoLocation::class], version = 4, exportSchema = false)
@TypeConverters(Converters::class)
abstract class PhotoDatabase : RoomDatabase() {
    abstract fun PhotoDao(): PhotoDao
}