package com.gustavoas.nutri.repository

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.gustavoas.nutri.model.DatabaseSearchApiResponse

@Dao
interface SearchApiCacheDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun cacheResponse(response: DatabaseSearchApiResponse)

    @Query("SELECT * FROM search_database WHERE `key` = :key")
    suspend fun getCachedPage(key: String): DatabaseSearchApiResponse?

    @Query("DELETE FROM search_database WHERE `key` = :key")
    suspend fun clearCachedPage(key: String)

    companion object {
        fun getKey(limit: Int, sortOption: String, offset: Int): String {
            return "$limit-$sortOption-$offset"
        }
    }
}