package com.example.projekatrazvoj.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Male
import androidx.compose.material.icons.filled.Female
import androidx.compose.material.icons.filled.People
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.projekatrazvoj.model.Newborn
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.ui.platform.LocalConfiguration

@Composable
fun DetailsScreen(newborn: Newborn?, onShare: () -> Unit) {
    if (newborn == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Podaci nisu pronađeni")
        }
    } else {
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val isWide = maxWidth > 600.dp
            if (isWide) {
                Row(modifier = Modifier.fillMaxSize().padding(16.dp), horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                    Card(
                        modifier = Modifier.weight(1f).fillMaxHeight(),
                        elevation = CardDefaults.cardElevation(8.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Text(
                                text = newborn.municipality ?: "Nepoznato",
                                style = MaterialTheme.typography.headlineSmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color.Gray)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(newborn.institution ?: "Nepoznato")
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.CalendarToday, contentDescription = null, tint = Color.Gray)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(newborn.dateUpdate ?: "Nepoznato")
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Male, contentDescription = null, tint = Color.Blue)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Muških: ${newborn.maleTotal ?: 0}")
                                Spacer(modifier = Modifier.width(16.dp))
                                Icon(Icons.Default.Female, contentDescription = null, tint = Color.Magenta)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Ženskih: ${newborn.femaleTotal ?: 0}")
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.People, contentDescription = null, tint = Color.DarkGray)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Ukupno: ${newborn.total ?: 0}", style = MaterialTheme.typography.titleMedium)
                            }
                        }
                    }
                    // Bar chart i button u desnoj koloni
                    Column(modifier = Modifier.weight(1f).fillMaxHeight(), verticalArrangement = Arrangement.SpaceBetween) {
                        Column {
                            Text(
                                text = "Prikaz broja rođenih po polu",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.secondary
                            )
                            val primaryColor = MaterialTheme.colorScheme.primary
                            val secondaryColor = MaterialTheme.colorScheme.secondary
                            val tertiaryColor = MaterialTheme.colorScheme.tertiary
                            val barLabels = listOf("Muški", "Ženski", "Ukupno")
                            val barValues = listOf(newborn.maleTotal ?: 0, newborn.femaleTotal ?: 0, newborn.total ?: 0)
                            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.BottomCenter) {
                                Canvas(modifier = Modifier.fillMaxWidth().height(100.dp)) {
                                    val barWidth = size.width / 7f
                                    val space = barWidth
                                    val max = (barValues.maxOrNull() ?: 1).toFloat()
                                    val colors = listOf(primaryColor, secondaryColor, tertiaryColor)
                                    for (i in 0..2) {
                                        val value = barValues[i]
                                        val barHeight = (value / max) * size.height * 0.7f
                                        drawRect(
                                            color = colors[i],
                                            topLeft = Offset((i * (barWidth + space)) + space, size.height - barHeight),
                                            size = Size(barWidth, barHeight)
                                        )
                                    }
                                }
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 80.dp),
                                    horizontalArrangement = Arrangement.SpaceEvenly
                                ) {
                                    for (i in 0..2) {
                                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                            Text(barLabels[i], style = MaterialTheme.typography.labelSmall)
                                            Text(barValues[i].toString(), style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
                                        }
                                    }
                                }
                            }
                        }
                        Button(
                            onClick = onShare,
                            modifier = Modifier.align(Alignment.End).padding(bottom = 16.dp)
                        ) {
                            Text("Podijeli")
                        }
                    }
                }
            } else {
                Box(modifier = Modifier.fillMaxSize()) {
                    Card(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                            .align(Alignment.TopCenter),
                        elevation = CardDefaults.cardElevation(8.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Text(
                                text = newborn.municipality ?: "Nepoznato",
                                style = MaterialTheme.typography.headlineSmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color.Gray)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(newborn.institution ?: "Nepoznato")
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.CalendarToday, contentDescription = null, tint = Color.Gray)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(newborn.dateUpdate ?: "Nepoznato")
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Male, contentDescription = null, tint = Color.Blue)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Muških: ${newborn.maleTotal ?: 0}")
                                Spacer(modifier = Modifier.width(16.dp))
                                Icon(Icons.Default.Female, contentDescription = null, tint = Color.Magenta)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Ženskih: ${newborn.femaleTotal ?: 0}")
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.People, contentDescription = null, tint = Color.DarkGray)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Ukupno: ${newborn.total ?: 0}", style = MaterialTheme.typography.titleMedium)
                            }
            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Prikaz broja rođenih po polu",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.secondary
                            )
                            val primaryColor = MaterialTheme.colorScheme.primary
                            val secondaryColor = MaterialTheme.colorScheme.secondary
                            val tertiaryColor = MaterialTheme.colorScheme.tertiary
                            val barLabels = listOf("Muški", "Ženski", "Ukupno")
                            val barValues = listOf(newborn.maleTotal ?: 0, newborn.femaleTotal ?: 0, newborn.total ?: 0)
                            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.BottomCenter) {
                                Canvas(modifier = Modifier.fillMaxWidth().height(100.dp)) {
                                    val barWidth = size.width / 7f
                                    val space = barWidth
                                    val max = (barValues.maxOrNull() ?: 1).toFloat()
                                    val colors = listOf(primaryColor, secondaryColor, tertiaryColor)
                                    for (i in 0..2) {
                                        val value = barValues[i]
                                        val barHeight = (value / max) * size.height * 0.7f
                                        drawRect(
                                            color = colors[i],
                                            topLeft = Offset((i * (barWidth + space)) + space, size.height - barHeight),
                                            size = Size(barWidth, barHeight)
                                        )
                                    }
                                }
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 80.dp),
                                    horizontalArrangement = Arrangement.SpaceEvenly
                                ) {
                                    for (i in 0..2) {
                                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                            Text(barLabels[i], style = MaterialTheme.typography.labelSmall)
                                            Text(barValues[i].toString(), style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
                                        }
                                    }
                                }
                            }
                        }
                    }
                    Button(
                        onClick = onShare,
                        modifier = Modifier.align(Alignment.BottomEnd).padding(24.dp)
                    ) {
                Text("Podijeli")
                    }
                }
            }
        }
    }
} 