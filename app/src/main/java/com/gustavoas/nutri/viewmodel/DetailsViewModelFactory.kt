package com.gustavoas.nutri.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gustavoas.nutri.repository.ProfessionalsRepository

class DetailsViewModelFactory(
    private val repository: ProfessionalsRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DetailsViewModel(repository) as T
        }
        throw IllegalArgumentException("DetailsViewModelFactory: Unknown ViewModel class")
    }
}