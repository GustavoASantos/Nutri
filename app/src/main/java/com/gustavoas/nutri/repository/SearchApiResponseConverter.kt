package com.gustavoas.nutri.repository

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.gustavoas.nutri.model.SearchApiResponse

class SearchApiResponseConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromApiResponse(response: SearchApiResponse): String {
        return gson.toJson(response)
    }

    @TypeConverter
    fun toApiResponse(responseString: String): SearchApiResponse {
        val type = object : TypeToken<SearchApiResponse>() {}
        return gson.fromJson(responseString, type)
    }
}