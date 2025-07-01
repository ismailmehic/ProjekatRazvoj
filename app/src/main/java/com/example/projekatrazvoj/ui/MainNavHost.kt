package com.example.projekatrazvoj.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.projekatrazvoj.model.UiScreen
import androidx.compose.material3.Scaffold
import com.example.projekatrazvoj.ui.components.AppTopBar
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.delay
import androidx.compose.ui.platform.LocalContext
import android.content.Intent
import androidx.core.content.ContextCompat.startActivity
import androidx.compose.runtime.collectAsState
import com.example.projekatrazvoj.viewmodel.NewbornViewModel
import com.example.projekatrazvoj.viewmodel.DiedViewModel
import com.example.projekatrazvoj.viewmodel.DiedUiState
import androidx.navigation.compose.rememberNavController
import com.example.projekatrazvoj.ui.components.AppBottomBar

@Composable
fun MainNavHost(
    newbornViewModel: NewbornViewModel,
    diedViewModel: DiedViewModel,
    datasetType: String
) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = if (datasetType == "newborn") "list" else "died_list"
    ) {
        if (datasetType == "newborn") {
            composable("list") {
                val uiState = newbornViewModel.uiState.collectAsState().value
                val favorites = newbornViewModel.favorites.collectAsState().value.map { it.id }
                val municipalities = (uiState as? com.example.projekatrazvoj.viewmodel.NewbornUiState.Success)?.data?.mapNotNull { it.municipality }?.distinct()?.sorted() ?: emptyList()
                val years = (uiState as? com.example.projekatrazvoj.viewmodel.NewbornUiState.Success)?.data?.mapNotNull { it.year }?.distinct()?.sortedDescending() ?: emptyList()
                val sortBy = newbornViewModel.sortBy.collectAsState().value
                val selectedMunicipality = newbornViewModel.selectedMunicipality.collectAsState().value
                val selectedYear = newbornViewModel.selectedYear.collectAsState().value
                val filter = newbornViewModel.filter.collectAsState().value
                Scaffold(
                    topBar = {
                        AppTopBar(
                            title = "Novorođeni",
                            datasetType = "newborn",
                            onFavorites = { navController.navigate("favorites") },
                            onChart = { navController.navigate("chart") }
                        )
                    },
                    bottomBar = {
                        AppBottomBar(
                            onFavorites = { navController.navigate("favorites") },
                            onChart = { navController.navigate("chart") },
                            selected = "list"
                        )
                    }
                ) {
                    ListScreen(
                        uiState = uiState,
                        favorites = favorites,
                        onItemClick = { id -> navController.navigate("details/$id") },
                        onFavoriteClick = { newbornViewModel.toggleFavorite(it) },
                        onRefresh = { newbornViewModel.refresh() },
                        filter = filter,
                        onFilterChange = { newbornViewModel.setFilter(it) },
                        municipalities = municipalities,
                        selectedMunicipality = selectedMunicipality,
                        onMunicipalityChange = { newbornViewModel.setMunicipality(it) },
                        years = years,
                        selectedYear = selectedYear,
                        onYearChange = { newbornViewModel.setYear(it) },
                        sortBy = sortBy,
                        onSortByChange = { newbornViewModel.setSortBy(it) },
                        contentPadding = it
                    )
                }
            }
            composable("details/{id}") { backStackEntry ->
                val id = backStackEntry.arguments?.getString("id")?.toIntOrNull()
                val newborn = newbornViewModel.selectedNewborn.collectAsState().value
                val context = LocalContext.current
                LaunchedEffect(id) { if (id != null) newbornViewModel.selectNewborn(id) }
                Scaffold(
                    topBar = {
                        AppTopBar(
                            title = "Detalji",
                            datasetType = "newborn",
                            onFavorites = { navController.navigate("favorites") },
                            onChart = { navController.navigate("chart") },
                            showBack = true,
                            onBack = { navController.popBackStack() }
                        )
                    }
                ) {
                    DetailsScreen(newborn = newborn, onShare = {
                        newborn?.let {
                            val shareText = "Opština: ${it.municipality ?: "Nepoznato"}\n" +
                                "Institucija: ${it.institution ?: "Nepoznato"}\n" +
                                "Datum: ${it.dateUpdate ?: "Nepoznato"}\n" +
                                "Muških: ${it.maleTotal ?: 0}\n" +
                                "Ženskih: ${it.femaleTotal ?: 0}\n" +
                                "Ukupno: ${it.total ?: 0}"
                            val sendIntent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(Intent.EXTRA_TEXT, shareText)
                                type = "text/plain"
                            }
                            context.startActivity(Intent.createChooser(sendIntent, "Podijeli podatke"))
                        }
                    })
                }
            }
            composable("details_fav/{id}") { backStackEntry ->
                val id = backStackEntry.arguments?.getString("id")?.toIntOrNull()
                val newborn = newbornViewModel.selectedNewborn.collectAsState().value
                val context = LocalContext.current
                LaunchedEffect(id) { if (id != null) newbornViewModel.selectFavoriteNewborn(id) }
                Scaffold(
                    topBar = {
                        AppTopBar(
                            title = "Detalji (omiljeni)",
                            datasetType = "newborn",
                            onFavorites = { navController.navigate("favorites") },
                            onChart = { navController.navigate("chart") },
                            showBack = true,
                            onBack = { navController.popBackStack() }
                        )
                    }
                ) {
                    DetailsScreen(newborn = newborn, onShare = {
                        newborn?.let {
                            val shareText = "Opština: ${it.municipality ?: "Nepoznato"}\n" +
                                "Institucija: ${it.institution ?: "Nepoznato"}\n" +
                                "Datum: ${it.dateUpdate ?: "Nepoznato"}\n" +
                                "Muških: ${it.maleTotal ?: 0}\n" +
                                "Ženskih: ${it.femaleTotal ?: 0}\n" +
                                "Ukupno: ${it.total ?: 0}"
                            val sendIntent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(Intent.EXTRA_TEXT, shareText)
                                type = "text/plain"
                            }
                            context.startActivity(Intent.createChooser(sendIntent, "Podijeli podatke"))
                        }
                    })
                }
            }
            composable("favorites") {
                val favorites = newbornViewModel.favorites.collectAsState().value
                Scaffold(
                    topBar = {
                        AppTopBar(
                            title = "Favoriti",
                            datasetType = "newborn",
                            onFavorites = { },
                            onChart = { navController.navigate("chart") },
                            showBack = true,
                            onBack = { navController.popBackStack() }
                        )
                    }
                ) {
                    FavoritesScreen(favorites = favorites, onItemClick = { id -> navController.navigate("details_fav/$id") })
                }
            }
            composable("chart") {
                val data = newbornViewModel.uiState.collectAsState().value
                Scaffold(
                    topBar = {
                        AppTopBar(
                            title = "Grafikon",
                            datasetType = "newborn",
                            onFavorites = { navController.navigate("favorites") },
                            onChart = { },
                            showBack = true,
                            onBack = { navController.popBackStack() }
                        )
                    }
                ) {
                    ChartScreen(datasetType = "newborn", newborns = if (data is com.example.projekatrazvoj.viewmodel.NewbornUiState.Success) data.data else emptyList())
                }
            }
        } else {
            composable("died_list") {
                val uiState = diedViewModel.uiState.collectAsState().value
                val favorites = diedViewModel.favorites.collectAsState().value
                val diedMunicipalities = (uiState as? com.example.projekatrazvoj.viewmodel.DiedUiState.Success)?.data?.mapNotNull { it.municipality }?.distinct()?.sorted() ?: emptyList()
                val diedYears = (uiState as? com.example.projekatrazvoj.viewmodel.DiedUiState.Success)?.data?.mapNotNull { it.year }?.distinct()?.sortedDescending() ?: emptyList()
                val diedSortBy = diedViewModel.sortBy.collectAsState().value
                val diedSelectedMunicipality = diedViewModel.selectedMunicipality.collectAsState().value
                val diedSelectedYear = diedViewModel.selectedYear.collectAsState().value
                val diedFilter = diedViewModel.filter.collectAsState().value
                Scaffold(
                    topBar = {
                        AppTopBar(
                            title = "Umrli",
                            datasetType = "died",
                            onFavorites = { navController.navigate("died_favorites") },
                            onChart = { navController.navigate("chart") }
                        )
                    },
                    bottomBar = {
                        AppBottomBar(
                            onFavorites = { navController.navigate("died_favorites") },
                            onChart = { navController.navigate("chart") },
                            selected = "died_list"
                        )
                    }
                ) {
                    DiedListScreen(
                        uiState = uiState,
                        favorites = favorites,
                        onItemClick = { id -> navController.navigate("died_details/$id") },
                        onFavoriteClick = { diedViewModel.toggleFavorite(it) },
                        onRefresh = { diedViewModel.refresh() },
                        filter = diedFilter,
                        onFilterChange = { diedViewModel.setFilter(it) },
                        municipalities = diedMunicipalities,
                        selectedMunicipality = diedSelectedMunicipality,
                        onMunicipalityChange = { diedViewModel.setMunicipality(it) },
                        years = diedYears,
                        selectedYear = diedSelectedYear,
                        onYearChange = { diedViewModel.setYear(it) },
                        sortBy = diedSortBy,
                        onSortByChange = { diedViewModel.setSortBy(it) },
                        contentPadding = it
                    )
                }
            }
            composable("died_details/{id}") { backStackEntry ->
                val id = backStackEntry.arguments?.getString("id")?.toIntOrNull()
                val died = diedViewModel.selectedDied.collectAsState().value
                val context = LocalContext.current
                LaunchedEffect(id) { if (id != null) diedViewModel.selectDied(id) }
                Scaffold(
                    topBar = {
                        AppTopBar(
                            title = "Detalji",
                            datasetType = "died",
                            onFavorites = { navController.navigate("died_favorites") },
                            onChart = { navController.navigate("chart") },
                            showBack = true,
                            onBack = { navController.popBackStack() }
                        )
                    }
                ) {
                    DiedDetailsScreen(died = died, onShare = {
                        died?.let {
                            val shareText = "Opština: ${it.municipality ?: "Nepoznato"}\n" +
                                "Institucija: ${it.institution ?: "Nepoznato"}\n" +
                                "Datum: ${it.dateUpdate ?: "Nepoznato"}\n" +
                                "Muških: ${it.maleTotal ?: 0}\n" +
                                "Ženskih: ${it.femaleTotal ?: 0}\n" +
                                "Ukupno: ${it.total ?: 0}"
                            val sendIntent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(Intent.EXTRA_TEXT, shareText)
                                type = "text/plain"
                            }
                            context.startActivity(Intent.createChooser(sendIntent, "Podijeli podatke"))
                        }
                    })
                }
            }
            composable("died_details_fav/{id}") { backStackEntry ->
                val id = backStackEntry.arguments?.getString("id")?.toIntOrNull()
                val died = diedViewModel.selectedDied.collectAsState().value
                val context = LocalContext.current
                LaunchedEffect(id) { if (id != null) diedViewModel.selectFavoriteDied(id) }
                Scaffold(
                    topBar = {
                        AppTopBar(
                            title = "Detalji (omiljeni)",
                            datasetType = "died",
                            onFavorites = { navController.navigate("died_favorites") },
                            onChart = { navController.navigate("chart") },
                            showBack = true,
                            onBack = { navController.popBackStack() }
                        )
                    }
                ) {
                    DiedDetailsScreen(died = died, onShare = {
                        died?.let {
                            val shareText = "Opština: ${it.municipality ?: "Nepoznato"}\n" +
                                "Institucija: ${it.institution ?: "Nepoznato"}\n" +
                                "Datum: ${it.dateUpdate ?: "Nepoznato"}\n" +
                                "Muških: ${it.maleTotal ?: 0}\n" +
                                "Ženskih: ${it.femaleTotal ?: 0}\n" +
                                "Ukupno: ${it.total ?: 0}"
                            val sendIntent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(Intent.EXTRA_TEXT, shareText)
                                type = "text/plain"
                            }
                            context.startActivity(Intent.createChooser(sendIntent, "Podijeli podatke"))
                        }
                    })
                }
            }
            composable("died_favorites") {
                val favorites = diedViewModel.favorites.collectAsState().value
                Scaffold(
                    topBar = {
                        AppTopBar(
                            title = "Favoriti (umrli)",
                            datasetType = "died",
                            onFavorites = { },
                            onChart = { navController.navigate("chart") },
                            showBack = true,
                            onBack = { navController.popBackStack() }
                        )
                    }
                ) {
                    DiedFavoritesScreen(favorites = favorites, onItemClick = { id -> navController.navigate("died_details_fav/$id") })
                }
            }
            composable("chart") {
                val data = diedViewModel.uiState.collectAsState().value
                Scaffold(
                    topBar = {
                        AppTopBar(
                            title = "Grafikon",
                            datasetType = "died",
                            onFavorites = { navController.navigate("died_favorites") },
                            onChart = { },
                            showBack = true,
                            onBack = { navController.popBackStack() }
                        )
                    }
                ) {
                    ChartScreen(datasetType = "died", died = if (data is com.example.projekatrazvoj.viewmodel.DiedUiState.Success) data.data else emptyList())
                }
            }
        }
    }
} 