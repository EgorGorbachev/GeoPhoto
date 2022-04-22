package com.example.geophoto.di.modules

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.data.repository.PhotoDataBaseRepositoryImpl
import com.example.data.repository.PrefsRepositoryImpl
import com.example.data.source.local.dao.PhotoDao
import com.example.data.source.local.data_sources.SharedPreferencesDataSource
import com.example.domain.repository.PhotoDataBaseRepository
import com.example.domain.repository.PrefsRepository
import com.example.data.source.local.database.PhotoDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object PhotoModule {

    @Singleton
    @Provides
    fun provideSearchDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(
            context.applicationContext,
            PhotoDatabase::class.java,
            "images_database"
        ).build()

    @Provides
    fun providesDao(appDatabase: PhotoDatabase): PhotoDao = appDatabase.PhotoDao()

    @Provides
    @Singleton
    fun providePreferencesRepository(sharedPreferencesDataSource: SharedPreferencesDataSource): PrefsRepository =
        PrefsRepositoryImpl(sharedPreferencesDataSource)

    @Provides
    @Singleton
    fun provideSharedPreferencesDataSource(app: Application): SharedPreferencesDataSource =
        SharedPreferencesDataSource(app)

    @Provides
    @Singleton
    fun provideDataBaseRepository(photoDao: PhotoDao): PhotoDataBaseRepository =
        PhotoDataBaseRepositoryImpl(photoDao)
}