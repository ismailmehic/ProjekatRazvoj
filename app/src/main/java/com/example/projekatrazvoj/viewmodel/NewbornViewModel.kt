package com.example.projekatrazvoj.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projekatrazvoj.model.Newborn
import com.example.projekatrazvoj.model.NewbornFavorite
import com.example.projekatrazvoj.repository.NewbornRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.IOException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.example.projekatrazvoj.model.UiFilterSort

class NewbornViewModel(private val repository: NewbornRepository) : ViewModel() {
    private val _uiState = MutableStateFlow<NewbornUiState>(NewbornUiState.Loading)
    val uiState: StateFlow<NewbornUiState> = _uiState

    private val _filter = MutableStateFlow("")
    val filter: StateFlow<String> = _filter

    private val _favorites = MutableStateFlow<List<NewbornFavorite>>(emptyList())
    val favorites: StateFlow<List<NewbornFavorite>> = _favorites.asStateFlow()

    private val _selectedNewborn = MutableStateFlow<Newborn?>(null)
    val selectedNewborn: StateFlow<Newborn?> = _selectedNewborn.asStateFlow()

    private val _sortBy = MutableStateFlow(UiFilterSort.SortBy.DATE_DESC)
    val sortBy: StateFlow<UiFilterSort.SortBy> = _sortBy

    private val _selectedMunicipality = MutableStateFlow<String?>(null)
    val selectedMunicipality: StateFlow<String?> = _selectedMunicipality

    private val _selectedYear = MutableStateFlow<Int?>(null)
    val selectedYear: StateFlow<Int?> = _selectedYear

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        _uiState.value = NewbornUiState.Error(throwable.message ?: "Unknown error")
    }

    init {
        refreshAndObserve()
        viewModelScope.launch {
            repository.getFavorites().collect { _favorites.value = it }
        }
    }

    private fun refreshAndObserve() {
        viewModelScope.launch(exceptionHandler) {
            try {
                repository.refreshNewborns()
            } catch (e: IOException) {
                // Nema interneta, koristi lokalne podatke
            }
            repository.getAllNewborns().collect { list ->
                var filtered = list
                if (_filter.value.isNotBlank()) {
                    filtered = filtered.filter {
                        (it.municipality?.contains(_filter.value, true) ?: false) ||
                        (it.institution?.contains(_filter.value, true) ?: false)
                    }
                }
                _selectedMunicipality.value?.let { m ->
                    filtered = filtered.filter { it.municipality == m }
                }
                _selectedYear.value?.let { y ->
                    filtered = filtered.filter { it.year == y }
                }
                filtered = when (_sortBy.value) {
                    UiFilterSort.SortBy.DATE_DESC -> filtered.sortedByDescending { it.dateUpdate }
                    UiFilterSort.SortBy.DATE_ASC -> filtered.sortedBy { it.dateUpdate }
                    UiFilterSort.SortBy.TOTAL_DESC -> filtered.sortedByDescending { it.total ?: 0 }
                    UiFilterSort.SortBy.TOTAL_ASC -> filtered.sortedBy { it.total ?: 0 }
                    UiFilterSort.SortBy.MUNICIPALITY_ASC -> filtered.sortedBy { it.municipality ?: "" }
                    else -> filtered
                }
                _uiState.value = NewbornUiState.Success(filtered)
            }
        }
    }

    fun refresh() {
        viewModelScope.launch(exceptionHandler) {
            _uiState.value = NewbornUiState.Loading
            try {
                repository.refreshNewborns()
            } catch (e: Exception) {
                _uiState.value = NewbornUiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun setFilter(query: String) {
        _filter.value = query
        refreshAndObserve()
    }

    fun toggleFavorite(newborn: Newborn) {
        viewModelScope.launch {
            if (repository.isFavorite(newborn.id)) {
                repository.removeFavorite(newborn)
            } else {
                repository.addFavorite(newborn)
            }
        }
    }

    fun selectNewborn(id: Int) {
        viewModelScope.launch {
            _selectedNewborn.value = repository.getById(id)
        }
    }

    fun selectFavoriteNewborn(id: Int) {
        viewModelScope.launch {
            val fav = repository.getFavoriteById(id)
            if (fav != null) {
                _selectedNewborn.value = Newborn(
                    id = fav.id,
                    entity = fav.entity,
                    canton = fav.canton,
                    municipality = fav.municipality,
                    institution = fav.institution,
                    year = fav.year,
                    month = fav.month,
                    dateUpdate = fav.dateUpdate,
                    maleTotal = fav.maleTotal,
                    femaleTotal = fav.femaleTotal,
                    total = fav.total
                )
            } else {
                _selectedNewborn.value = null
            }
        }
    }

    fun setSortBy(sort: UiFilterSort.SortBy) {
        _sortBy.value = sort
        refreshAndObserve()
    }

    fun setMunicipality(m: String?) {
        _selectedMunicipality.value = m
        refreshAndObserve()
    }

    fun setYear(y: Int?) {
        _selectedYear.value = y
        refreshAndObserve()
    }
} 