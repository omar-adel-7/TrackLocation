package com.general.data.local

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface CacheApiDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(CacheApis: List<CacheApi?>?)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(CacheApi: CacheApi?): Long

    @Update
    fun update(CacheApi: CacheApi?): Int

    @Query("DELETE FROM CacheApi WHERE url = :url and params = :params and beanName = :beanName and objectOfArrayBeanName = :objectOfArrayBeanName")
    fun deleteByCustom(
        url: String?,
        params: String?,
        beanName: String?,
        objectOfArrayBeanName: String?
    ): Int

    @Delete
    fun delete(CacheApi: CacheApi?): Int

    @get:Query("SELECT * FROM CacheApi")
    val all: LiveData<List<CacheApi?>?>?

    @Query("SELECT * FROM CacheApi where url = :url and params = :params ")
    fun getObject(url: String?, params: String?): LiveData<CacheApi?>?

    @Query("SELECT * FROM CacheApi where url = :url and params = :params ")
    fun getObjectSync(url: String?, params: String?): CacheApi?

    @get:Query("SELECT * FROM CacheApi")
    val allSync: List<CacheApi?>?
}