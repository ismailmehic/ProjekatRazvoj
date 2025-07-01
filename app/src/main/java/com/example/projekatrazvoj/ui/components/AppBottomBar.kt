package com.example.projekatrazvoj.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable

@Composable
fun AppBottomBar(
    onFavorites: () -> Unit,
    onChart: () -> Unit,
    selected: String // "favorites" ili "chart"
) {
    NavigationBar {
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Favorite, contentDescription = "Favoriti") },
            label = { Text("Favoriti") },
            selected = selected == "favorites",
            onClick = onFavorites
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.PieChart, contentDescription = "Grafikon") },
            label = { Text("Grafikon") },
            selected = selected == "chart",
            onClick = onChart
        )
    }
} 