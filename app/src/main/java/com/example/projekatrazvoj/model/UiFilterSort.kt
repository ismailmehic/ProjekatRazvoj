package com.example.projekatrazvoj.model

data class UiFilterSort(
    val query: String = "",
    val sortBy: SortBy = SortBy.DATE_DESC
) {
    enum class SortBy { DATE_DESC, DATE_ASC, TOTAL_DESC, TOTAL_ASC, MUNICIPALITY_ASC }
} 