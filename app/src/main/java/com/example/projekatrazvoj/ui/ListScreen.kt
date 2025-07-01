package com.example.projekatrazvoj.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.projekatrazvoj.model.Newborn
import com.example.projekatrazvoj.viewmodel.NewbornUiState
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Divider
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.ui.unit.Dp

@Composable
fun ListScreen(
    uiState: com.example.projekatrazvoj.viewmodel.NewbornUiState,
    favorites: List<Int>,
    onItemClick: (Int) -> Unit,
    onFavoriteClick: (com.example.projekatrazvoj.model.Newborn) -> Unit,
    onRefresh: () -> Unit,
    filter: String,
    onFilterChange: (String) -> Unit,
    municipalities: List<String>,
    selectedMunicipality: String?,
    onMunicipalityChange: (String?) -> Unit,
    years: List<Int>,
    selectedYear: Int?,
    onYearChange: (Int?) -> Unit,
    sortBy: com.example.projekatrazvoj.model.UiFilterSort.SortBy,
    onSortByChange: (com.example.projekatrazvoj.model.UiFilterSort.SortBy) -> Unit,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    val isRefreshing = uiState is com.example.projekatrazvoj.viewmodel.NewbornUiState.Loading
    var municipalityExpanded by remember { mutableStateOf(false) }
    var yearExpanded by remember { mutableStateOf(false) }
    var sortExpanded by remember { mutableStateOf(false) }
    BoxWithConstraints(modifier = Modifier.fillMaxSize().padding(contentPadding).padding(8.dp)) {
        val isWide = maxWidth > 600.dp
        if (isWide) {
            Column(modifier = Modifier.fillMaxSize()) {
                OutlinedTextField(
                    value = filter,
                    onValueChange = { onFilterChange(it) },
                    label = { Text("Pretraga po opštini ili instituciji") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box {
                        OutlinedButton(onClick = { municipalityExpanded = true }) {
                            Text(selectedMunicipality ?: "Opština")
                        }
                        DropdownMenu(expanded = municipalityExpanded, onDismissRequest = { municipalityExpanded = false }) {
                            DropdownMenuItem(text = { Text("Sve opštine") }, onClick = { onMunicipalityChange(null); municipalityExpanded = false })
                            municipalities.forEach { m ->
                                DropdownMenuItem(text = { Text(m) }, onClick = { onMunicipalityChange(m); municipalityExpanded = false })
                            }
                        }
                    }
                    Box {
                        OutlinedButton(onClick = { yearExpanded = true }) {
                            Text(selectedYear?.toString() ?: "Godina")
                        }
                        DropdownMenu(expanded = yearExpanded, onDismissRequest = { yearExpanded = false }) {
                            DropdownMenuItem(text = { Text("Sve godine") }, onClick = { onYearChange(null); yearExpanded = false })
                            years.forEach { y ->
                                DropdownMenuItem(text = { Text(y.toString()) }, onClick = { onYearChange(y); yearExpanded = false })
                            }
                        }
                    }
                    Box {
                        OutlinedButton(onClick = { sortExpanded = true }) {
                            Text(
                                when (sortBy) {
                                    com.example.projekatrazvoj.model.UiFilterSort.SortBy.DATE_DESC -> "Datum ↓"
                                    com.example.projekatrazvoj.model.UiFilterSort.SortBy.DATE_ASC -> "Datum ↑"
                                    com.example.projekatrazvoj.model.UiFilterSort.SortBy.TOTAL_DESC -> "Ukupno ↓"
                                    com.example.projekatrazvoj.model.UiFilterSort.SortBy.TOTAL_ASC -> "Ukupno ↑"
                                    else -> ""
                                }
                            )
                        }
                        DropdownMenu(expanded = sortExpanded, onDismissRequest = { sortExpanded = false }) {
                            DropdownMenuItem(text = { Text("Datum ↓") }, onClick = { onSortByChange(com.example.projekatrazvoj.model.UiFilterSort.SortBy.DATE_DESC); sortExpanded = false })
                            DropdownMenuItem(text = { Text("Datum ↑") }, onClick = { onSortByChange(com.example.projekatrazvoj.model.UiFilterSort.SortBy.DATE_ASC); sortExpanded = false })
                            DropdownMenuItem(text = { Text("Ukupno ↓") }, onClick = { onSortByChange(com.example.projekatrazvoj.model.UiFilterSort.SortBy.TOTAL_DESC); sortExpanded = false })
                            DropdownMenuItem(text = { Text("Ukupno ↑") }, onClick = { onSortByChange(com.example.projekatrazvoj.model.UiFilterSort.SortBy.TOTAL_ASC); sortExpanded = false })
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                SwipeRefresh(state = rememberSwipeRefreshState(isRefreshing), onRefresh = onRefresh) {
                    when (uiState) {
                        is com.example.projekatrazvoj.viewmodel.NewbornUiState.Loading -> Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
                        is com.example.projekatrazvoj.viewmodel.NewbornUiState.Error -> Text("Greška: ${uiState.message}", color = MaterialTheme.colorScheme.error)
                        is com.example.projekatrazvoj.viewmodel.NewbornUiState.Success -> LazyColumn(modifier = Modifier.weight(1f).fillMaxHeight()) {
                            items(uiState.data.size) { idx ->
                                val item = uiState.data[idx]
                                val isFavorite = item.id in favorites
                                ListItem(
                                    headlineContent = { Text(item.municipality ?: "Nepoznato") },
                                    supportingContent = { Text("${item.institution ?: "Nepoznato"}, ${item.dateUpdate ?: "Nepoznato"}") },
                                    trailingContent = {
                                        IconButton(onClick = { onFavoriteClick(item) }) {
                                            Icon(
                                                imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                                contentDescription = null
                                            )
                                        }
                                    },
                                    modifier = Modifier.clickable { onItemClick(item.id) }
                                )
                                Divider()
                            }
                        }
                    }
                }
                Button(onClick = onRefresh, modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                    Text("Osvježi")
                }
            }
        } else {
            Column(modifier = Modifier.fillMaxSize()) {
                OutlinedTextField(
                    value = filter,
                    onValueChange = { onFilterChange(it) },
                    label = { Text("Pretraga po opštini ili instituciji") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box {
                        OutlinedButton(onClick = { municipalityExpanded = true }) {
                            Text(selectedMunicipality ?: "Opština")
                        }
                        DropdownMenu(expanded = municipalityExpanded, onDismissRequest = { municipalityExpanded = false }) {
                            DropdownMenuItem(text = { Text("Sve opštine") }, onClick = { onMunicipalityChange(null); municipalityExpanded = false })
                            municipalities.forEach { m ->
                                DropdownMenuItem(text = { Text(m) }, onClick = { onMunicipalityChange(m); municipalityExpanded = false })
                            }
                        }
                    }
                    Box {
                        OutlinedButton(onClick = { yearExpanded = true }) {
                            Text(selectedYear?.toString() ?: "Godina")
                        }
                        DropdownMenu(expanded = yearExpanded, onDismissRequest = { yearExpanded = false }) {
                            DropdownMenuItem(text = { Text("Sve godine") }, onClick = { onYearChange(null); yearExpanded = false })
                            years.forEach { y ->
                                DropdownMenuItem(text = { Text(y.toString()) }, onClick = { onYearChange(y); yearExpanded = false })
                            }
                        }
                    }
                    Box {
                        OutlinedButton(onClick = { sortExpanded = true }) {
                            Text(
                                when (sortBy) {
                                    com.example.projekatrazvoj.model.UiFilterSort.SortBy.DATE_DESC -> "Datum ↓"
                                    com.example.projekatrazvoj.model.UiFilterSort.SortBy.DATE_ASC -> "Datum ↑"
                                    com.example.projekatrazvoj.model.UiFilterSort.SortBy.TOTAL_DESC -> "Ukupno ↓"
                                    com.example.projekatrazvoj.model.UiFilterSort.SortBy.TOTAL_ASC -> "Ukupno ↑"
                                    else -> ""
                                }
                            )
                        }
                        DropdownMenu(expanded = sortExpanded, onDismissRequest = { sortExpanded = false }) {
                            DropdownMenuItem(text = { Text("Datum ↓") }, onClick = { onSortByChange(com.example.projekatrazvoj.model.UiFilterSort.SortBy.DATE_DESC); sortExpanded = false })
                            DropdownMenuItem(text = { Text("Datum ↑") }, onClick = { onSortByChange(com.example.projekatrazvoj.model.UiFilterSort.SortBy.DATE_ASC); sortExpanded = false })
                            DropdownMenuItem(text = { Text("Ukupno ↓") }, onClick = { onSortByChange(com.example.projekatrazvoj.model.UiFilterSort.SortBy.TOTAL_DESC); sortExpanded = false })
                            DropdownMenuItem(text = { Text("Ukupno ↑") }, onClick = { onSortByChange(com.example.projekatrazvoj.model.UiFilterSort.SortBy.TOTAL_ASC); sortExpanded = false })
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                SwipeRefresh(state = rememberSwipeRefreshState(isRefreshing), onRefresh = onRefresh) {
                    when (uiState) {
                        is com.example.projekatrazvoj.viewmodel.NewbornUiState.Loading -> Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
                        is com.example.projekatrazvoj.viewmodel.NewbornUiState.Error -> Text("Greška: ${uiState.message}", color = MaterialTheme.colorScheme.error)
                        is com.example.projekatrazvoj.viewmodel.NewbornUiState.Success -> LazyColumn(modifier = Modifier.weight(1f).fillMaxHeight()) {
                            items(uiState.data.size) { idx ->
                                val item = uiState.data[idx]
                                val isFavorite = item.id in favorites
                                ListItem(
                                    headlineContent = { Text(item.municipality ?: "Nepoznato") },
                                    supportingContent = { Text("${item.institution ?: "Nepoznato"}, ${item.dateUpdate ?: "Nepoznato"}") },
                                    trailingContent = {
                                        IconButton(onClick = { onFavoriteClick(item) }) {
                                            Icon(
                                                imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                                contentDescription = null
                                            )
                                        }
                                    },
                                    modifier = Modifier.clickable { onItemClick(item.id) }
                                )
                                Divider()
                            }
                        }
                    }
                }
                Button(onClick = onRefresh, modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                    Text("Osvježi")
                }
            }
        }
    }
} 