package com.gustavoas.nutri.repository

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.gustavoas.nutri.model.NutritionProfessional

@Dao
interface ProfessionalsStorageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun cacheProfessional(professional: NutritionProfessional)

    @Query("SELECT * FROM professionals_database WHERE id = :id")
    suspend fun getCachedProfessional(id: Int): NutritionProfessional?
}