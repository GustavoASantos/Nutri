package com.gustavoas.nutri

import com.gustavoas.nutri.model.SearchApiResponse
import com.gustavoas.nutri.model.NutritionProfessional
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface NutritionApiInterface {
    @GET("professionals/search")
    suspend fun getProfessionals(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int,
        @Query("sort_by") sortBy: String
    ): SearchApiResponse

    @GET("professionals/{id}")
    suspend fun getProfessional(
        @Path("id") id: Int
    ): NutritionProfessional
}