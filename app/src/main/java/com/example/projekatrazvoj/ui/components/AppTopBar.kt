@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
package com.example.projekatrazvoj.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable

@Composable
fun AppTopBar(
    title: String,
    datasetType: String,
    onFavorites: () -> Unit,
    onChart: () -> Unit,
    showBack: Boolean = false,
    onBack: (() -> Unit)? = null
) {
    TopAppBar(
        title = { Text(text = title) },
        navigationIcon = {
            if (showBack && onBack != null) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Nazad"
                    )
                }
            }
        },
        actions = {
            IconButton(onClick = onFavorites) {
                Icon(
                    imageVector = Icons.Filled.Favorite,
                    contentDescription = if (datasetType == "newborn") "Favoriti novorođeni" else "Favoriti umrli"
                )
            }
            IconButton(onClick = onChart) {
                Icon(
                    imageVector = Icons.Filled.PieChart,
                    contentDescription = "Grafikon"
                )
            }
        }
    )
}
