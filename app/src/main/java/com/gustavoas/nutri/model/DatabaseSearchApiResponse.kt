package com.gustavoas.nutri.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.gustavoas.nutri.repository.SearchApiResponseConverter

@Entity(tableName = "search_database")
@TypeConverters(SearchApiResponseConverter::class)
data class DatabaseSearchApiResponse(
    @PrimaryKey val key: String,
    val sortOption: String,
    val offset: Int,
    val response: SearchApiResponse,
    val timestamp: Long
)