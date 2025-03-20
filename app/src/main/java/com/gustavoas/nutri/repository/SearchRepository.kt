package com.gustavoas.nutri.repository

import android.content.Context
import com.gustavoas.nutri.RetrofitClient
import com.gustavoas.nutri.repository.SearchApiCacheDao.Companion.getKey
import com.gustavoas.nutri.Utils.isInternetAvailable
import com.gustavoas.nutri.model.SearchApiResponse
import com.gustavoas.nutri.model.DatabaseSearchApiResponse

class SearchRepository(
    private val cacheDao: SearchApiCacheDao
) {
    private val cacheExpiryTime = 60 * 60 * 1000L

    suspend fun fetchProfessionals(
        context: Context, limit: Int, offset: Int, sortBy: String
    ): SearchApiResponse {
        val cachedPage = cacheDao.getCachedPage(getKey(limit, sortBy, offset))

        val isCacheValid = cachedPage != null &&
                (System.currentTimeMillis() - cachedPage.timestamp < cacheExpiryTime
                        || !isInternetAvailable(context))

        if (isCacheValid && cachedPage?.response?.professionals?.isNotEmpty() == true) {
            return cachedPage.response
        } else {
            val response = try {
                RetrofitClient.apiService
                    .getProfessionals(limit = limit, offset = offset, sortBy = sortBy)
            } catch (_: Exception) {
                SearchApiResponse(
                    count = 0,
                    professionals = emptyList(),
                    limit = limit,
                    offset = offset
                )
            }

            if (response.professionals.isNotEmpty()) {
                cacheDao.cacheResponse(
                    DatabaseSearchApiResponse(
                        key = getKey(limit, sortBy, offset),
                        sortOption = sortBy,
                        offset = offset,
                        response = response,
                        timestamp = System.currentTimeMillis()
                    )
                )
            }

            return response
        }
    }
}