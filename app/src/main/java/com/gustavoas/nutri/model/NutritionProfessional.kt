package com.gustavoas.nutri.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.gustavoas.nutri.repository.NutritionProfessionalConverter
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "professionals_database")
@TypeConverters(NutritionProfessionalConverter::class)
data class NutritionProfessional(
    @PrimaryKey val id: Int,
    val name: String,
    val about_me: String?,
    val rating: Int,
    val rating_count: Int,
    val languages: List<String>,
    val expertise: List<String>,
    val profile_picture_url: String,
) : Parcelable