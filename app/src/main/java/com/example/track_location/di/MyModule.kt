package com.example.track_location.di

import android.content.Context
import androidx.room.Room
import com.general.data.local.CacheApiDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class MyModule {

    @Provides
    @Singleton
    fun providesCacheApiDatabase(context: Context): CacheApiDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            CacheApiDatabase::class.java, "CacheApis.db"
        )
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()
    }
}