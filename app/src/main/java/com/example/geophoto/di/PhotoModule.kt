package com.example.geophoto.di

import android.content.Context
import androidx.room.Room
import com.example.geophoto.repositories.photo_database.PhotoDao
import com.example.geophoto.repositories.photo_database.PhotoDatabase
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
	fun providesDao(appDatabase: PhotoDatabase): PhotoDao {
		return appDatabase.PhotoDao()
	}
}