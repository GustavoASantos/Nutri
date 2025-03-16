package com.gustavoas.nutri.repository

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.gustavoas.nutri.model.NutritionProfessional

@Database(entities = [NutritionProfessional::class], version = 1)
abstract class ProfessionalsDatabase : RoomDatabase() {

    abstract fun professionalsStorageDao(): ProfessionalsStorageDao

    companion object {
        @Volatile
        private var INSTANCE: ProfessionalsDatabase? = null

        fun getDatabase(context: Context): ProfessionalsDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ProfessionalsDatabase::class.java,
                    "professionals_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}