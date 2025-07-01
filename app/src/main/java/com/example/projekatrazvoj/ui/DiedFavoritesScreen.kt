package com.example.projekatrazvoj.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.projekatrazvoj.model.DiedFavorite

@Composable
fun DiedFavoritesScreen(favorites: List<DiedFavorite>, onItemClick: (Int) -> Unit) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text("Favoriti (umrli)", style = MaterialTheme.typography.headlineMedium, modifier = Modifier.padding(16.dp))
        LazyColumn {
            items(favorites.size) { idx ->
                val item = favorites[idx]
                ListItem(
                    headlineContent = { Text(item.municipality ?: "Nepoznato") },
                    supportingContent = { Text("${item.institution ?: "Nepoznato"}, ${item.dateUpdate ?: "Nepoznato"}") },
                    modifier = Modifier.clickable { onItemClick(item.id) }
                )
                androidx.compose.material3.Divider()
            }
        }
    }
} 