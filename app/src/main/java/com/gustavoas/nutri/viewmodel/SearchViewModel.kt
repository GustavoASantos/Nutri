package com.gustavoas.nutri.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gustavoas.nutri.repository.SearchRepository
import com.gustavoas.nutri.model.SearchApiResponse
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SearchViewModel(private val repository: SearchRepository): ViewModel() {
    private val _professionals = MutableLiveData<SearchApiResponse>()
    val professionals: LiveData<SearchApiResponse> = _professionals

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private var currentJob: Job? = null

    fun loadProfessionals(context: Context, limit: Int, offset: Int, sortBy: String) {
        currentJob?.cancel()

        currentJob = viewModelScope.launch {
            _isLoading.postValue(true)

            _professionals.postValue(repository.fetchProfessionals(context, limit, offset, sortBy))

            _isLoading.postValue(false)
        }
    }
}