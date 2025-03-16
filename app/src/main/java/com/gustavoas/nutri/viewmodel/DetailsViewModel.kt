package com.gustavoas.nutri.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gustavoas.nutri.repository.ProfessionalsRepository
import com.gustavoas.nutri.model.NutritionProfessional
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class DetailsViewModel(private val repository: ProfessionalsRepository) : ViewModel() {
    private val _professional = MutableLiveData<NutritionProfessional>()
    val professional: LiveData<NutritionProfessional> = _professional

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private var currentJob: Job? = null

    fun loadProfessional(context: Context, id: Int) {
        currentJob?.cancel()

        currentJob = viewModelScope.launch {
            _isLoading.postValue(true)

            _professional.postValue(repository.fetchProfessional(context, id))

            _isLoading.postValue(false)
        }
    }
}