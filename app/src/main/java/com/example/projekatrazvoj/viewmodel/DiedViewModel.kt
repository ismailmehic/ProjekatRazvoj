package com.example.projekatrazvoj.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projekatrazvoj.model.Died
import com.example.projekatrazvoj.model.DiedFavorite
import com.example.projekatrazvoj.repository.DiedRepository
import com.example.projekatrazvoj.model.UiFilterSort
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.IOException

class DiedViewModel(private val repository: DiedRepository) : ViewModel() {
    private val _uiState = MutableStateFlow<DiedUiState>(DiedUiState.Loading)
    val uiState: StateFlow<DiedUiState> = _uiState

    private val _filter = MutableStateFlow("")
    val filter: StateFlow<String> = _filter

    private val _favorites = MutableStateFlow<List<DiedFavorite>>(emptyList())
    val favorites: StateFlow<List<DiedFavorite>> = _favorites.asStateFlow()

    private val _selectedDied = MutableStateFlow<Died?>(null)
    val selectedDied: StateFlow<Died?> = _selectedDied.asStateFlow()

    private val _sortBy = MutableStateFlow(UiFilterSort.SortBy.DATE_DESC)
    val sortBy: StateFlow<UiFilterSort.SortBy> = _sortBy

    private val _selectedMunicipality = MutableStateFlow<String?>(null)
    val selectedMunicipality: StateFlow<String?> = _selectedMunicipality

    private val _selectedYear = MutableStateFlow<Int?>(null)
    val selectedYear: StateFlow<Int?> = _selectedYear

    private val exceptionHandler = kotlinx.coroutines.CoroutineExceptionHandler { _, throwable ->
        _uiState.value = DiedUiState.Error(throwable.message ?: "Unknown error")
    }

    init {
        refreshAndObserve()
        viewModelScope.launch {
            repository.getFavorites().collect { _favorites.value = it }
        }
    }

    fun refreshAndObserve() {
        viewModelScope.launch(exceptionHandler) {
            try {
                repository.refreshDied()
            } catch (e: IOException) {
                // Nema interneta, koristi lokalne podatke
            }
            repository.getAllDied().collect { list ->
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
                _uiState.value = DiedUiState.Success(filtered)
            }
        }
    }

    fun refresh() {
        viewModelScope.launch(exceptionHandler) {
            _uiState.value = DiedUiState.Loading
            try {
                repository.refreshDied()
            } catch (e: Exception) {
                _uiState.value = DiedUiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun setFilter(query: String) {
        _filter.value = query
        refreshAndObserve()
    }

    fun toggleFavorite(died: Died) {
        viewModelScope.launch {
            if (repository.isFavorite(died.id)) {
                repository.removeFavorite(died)
            } else {
                repository.addFavorite(died)
            }
        }
    }

    fun selectDied(id: Int) {
        viewModelScope.launch {
            _selectedDied.value = repository.getById(id)
        }
    }

    fun selectFavoriteDied(id: Int) {
        viewModelScope.launch {
            val fav = repository.getFavoriteById(id)
            if (fav != null) {
                _selectedDied.value = Died(
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
                _selectedDied.value = null
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