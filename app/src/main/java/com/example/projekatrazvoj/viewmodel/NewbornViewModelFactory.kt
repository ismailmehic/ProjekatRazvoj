package com.example.projekatrazvoj.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.projekatrazvoj.repository.NewbornRepository

class NewbornViewModelFactory(private val repository: NewbornRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NewbornViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NewbornViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
} 