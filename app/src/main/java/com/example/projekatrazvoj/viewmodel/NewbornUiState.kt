package com.example.projekatrazvoj.viewmodel

import com.example.projekatrazvoj.model.Newborn

sealed class NewbornUiState {
    object Loading : NewbornUiState()
    data class Success(val data: List<Newborn>) : NewbornUiState()
    data class Error(val message: String) : NewbornUiState()
} 