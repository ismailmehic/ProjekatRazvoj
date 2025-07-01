package com.example.projekatrazvoj.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.projekatrazvoj.repository.DiedRepository

class DiedViewModelFactory(private val repository: DiedRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DiedViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DiedViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
} 