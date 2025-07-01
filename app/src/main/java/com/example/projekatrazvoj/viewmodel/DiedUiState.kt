package com.example.projekatrazvoj.viewmodel

import com.example.projekatrazvoj.model.Died

sealed class DiedUiState {
    object Loading : DiedUiState()
    data class Success(val data: List<Died>) : DiedUiState()
    data class Error(val message: String) : DiedUiState()
} 