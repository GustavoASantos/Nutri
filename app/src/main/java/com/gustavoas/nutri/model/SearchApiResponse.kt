package com.gustavoas.nutri.model

data class SearchApiResponse(
    val count: Int,
    val limit: Int,
    val offset: Int,
    val professionals: List<NutritionProfessional>,
)