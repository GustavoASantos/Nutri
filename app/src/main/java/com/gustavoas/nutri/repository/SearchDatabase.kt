package com.gustavoas.nutri.repository

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.gustavoas.nutri.model.DatabaseSearchApiResponse

@Database(entities = [DatabaseSearchApiResponse::class], version = 1)
abstract class SearchDatabase : RoomDatabase() {

    abstract fun apiCacheDao(): SearchApiCacheDao

    companion object {
        @Volatile
        private var INSTANCE: SearchDatabase? = null

        fun getDatabase(context: Context): SearchDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SearchDatabase::class.java,
                    "search_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}