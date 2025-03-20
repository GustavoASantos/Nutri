package com.gustavoas.nutri.repository

import android.content.Context
import com.gustavoas.nutri.RetrofitClient
import com.gustavoas.nutri.Utils.isInternetAvailable
import com.gustavoas.nutri.model.NutritionProfessional

class ProfessionalsRepository(
    private val storageDao: ProfessionalsStorageDao
) {
    suspend fun fetchProfessional(
        context: Context,
        id: Int
    ): NutritionProfessional? {
        val cachedPro = storageDao.getCachedProfessional(id)

        if (cachedPro != null && !isInternetAvailable(context)) {
            return cachedPro
        } else {
            val response = try {
                RetrofitClient.apiService
                    .getProfessional(id)
            } catch (_: Exception) {
                null
            }
            response?.let {
                storageDao.cacheProfessional(response)
            }
            return response
        }
    }
}