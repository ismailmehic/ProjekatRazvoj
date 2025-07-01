package com.example.projekatrazvoj.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.MaterialTheme

@Composable
fun OnboardingScreen(onDatasetSelected: (String) -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Dobrodošli! Odaberite dataset:", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = { onDatasetSelected("newborn") }, modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            Text("Novorođeni")
        }
        Button(onClick = { onDatasetSelected("died") }, modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            Text("Umrli")
        }
    }
} 