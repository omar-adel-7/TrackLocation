package com.general.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.general.data.local.CacheApi

@Database(entities = [CacheApi::class], version = 1, exportSchema = false)
abstract class CacheApiDatabase : RoomDatabase() {
    abstract fun cacheApiDao(): CacheApiDao?
}