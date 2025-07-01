package com.example.projekatrazvoj.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.projekatrazvoj.model.Newborn
import com.example.projekatrazvoj.model.Died
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.ui.unit.Dp

@Composable
fun ChartScreen(
    datasetType: String,
    newborns: List<Newborn> = emptyList(),
    died: List<Died> = emptyList()
) {
    val isNewborn = datasetType == "newborn"
    val barColor = if (isNewborn) Color(0xFF1976D2) else Color(0xFFD32F2F)
    val legendLabel = if (isNewborn) "Novorođeni" else "Umrli"
    val chartTitle = if (isNewborn) "Broj novorođenih po mesecima" else "Broj umrlih po mesecima"
    val chartDesc = if (isNewborn) "Prikaz ukupnog broja novorođenih za svaki mesec." else "Prikaz ukupnog broja umrlih za svaki mesec."
    val months = listOf("Jan", "Feb", "Mar", "Apr", "Maj", "Jun", "Jul", "Avg", "Sep", "Okt", "Nov", "Dec")
    val grouped: Map<Int, Int> = if (isNewborn) {
        newborns.groupBy { it.month ?: 0 }
            .mapValues { entry -> entry.value.sumOf { it.total ?: 0 } }
    } else {
        died.groupBy { it.month ?: 0 }
            .mapValues { entry -> entry.value.sumOf { it.total ?: 0 } }
    }
    val max: Int = grouped.values.maxOrNull() ?: 1
    BoxWithConstraints(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        val isWide = maxWidth > 600.dp
        if (isWide) {
            Row(modifier = Modifier.fillMaxSize(), horizontalArrangement = Arrangement.spacedBy(32.dp)) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(chartTitle, style = MaterialTheme.typography.headlineMedium)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(chartDesc, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.secondary)
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Canvas(modifier = Modifier.size(16.dp).padding(end = 4.dp)) {
                            drawRect(color = barColor)
                        }
                        Text(legendLabel, style = MaterialTheme.typography.labelMedium)
                    }
                }
                Box(modifier = Modifier.weight(2f).fillMaxHeight(), contentAlignment = Alignment.BottomCenter) {
                    val barChartHeight = 260.dp
                    Canvas(modifier = Modifier.fillMaxWidth().height(barChartHeight)) {
                        val barWidth = size.width / 14f
                        val space = barWidth * 0.5f
                        for (i in 0 until 12) {
                            val value = grouped[i + 1] ?: 0
                            val barHeight = (value / max.toFloat()) * (size.height * 0.7f)
                            val x = i * (barWidth + space) + space
                            drawRect(
                                color = barColor,
                                topLeft = Offset(x, size.height - barHeight),
                                size = androidx.compose.ui.geometry.Size(barWidth, barHeight)
                            )
                        }
                    }
                    // Brojevi iznad stubića
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 40.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        for (i in 0 until 12) {
                            val value = grouped[i + 1] ?: 0
                            Box(Modifier.width(24.dp), contentAlignment = Alignment.Center) {
                                Text(
                                    text = if (value > 0) value.toString() else "",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.primary,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                    // Oznake meseci ispod stubića
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 180.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        for (i in 0 until 12) {
                            Box(Modifier.width(24.dp), contentAlignment = Alignment.Center) {
                                Text(
                                    text = months[i],
                                    style = MaterialTheme.typography.labelSmall,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }
            }
        } else {
            Column(modifier = Modifier.fillMaxSize()) {
                Text(chartTitle, style = MaterialTheme.typography.headlineMedium)
                Spacer(modifier = Modifier.height(4.dp))
                Text(chartDesc, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.secondary)
                Spacer(modifier = Modifier.height(16.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Canvas(modifier = Modifier.size(16.dp).padding(end = 4.dp)) {
                        drawRect(color = barColor)
                    }
                    Text(legendLabel, style = MaterialTheme.typography.labelMedium)
                }
                Spacer(modifier = Modifier.height(16.dp))
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.BottomCenter) {
                    val barChartHeight = 220.dp
                    Canvas(modifier = Modifier.fillMaxWidth().height(barChartHeight)) {
                        val barWidth = size.width / 14f
                        val space = barWidth * 0.5f
                        for (i in 0 until 12) {
                            val value = grouped[i + 1] ?: 0
                            val barHeight = (value / max.toFloat()) * (size.height * 0.7f)
                            val x = i * (barWidth + space) + space
                            drawRect(
                                color = barColor,
                                topLeft = Offset(x, size.height - barHeight),
                                size = androidx.compose.ui.geometry.Size(barWidth, barHeight)
                            )
                        }
                    }
                    // Brojevi iznad stubića
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 40.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        for (i in 0 until 12) {
                            val value = grouped[i + 1] ?: 0
                            Box(Modifier.width(24.dp), contentAlignment = Alignment.Center) {
                                Text(
                                    text = if (value > 0) value.toString() else "",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.primary,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                    // Oznake meseci ispod stubića
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 180.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        for (i in 0 until 12) {
                            Box(Modifier.width(24.dp), contentAlignment = Alignment.Center) {
                                Text(
                                    text = months[i],
                                    style = MaterialTheme.typography.labelSmall,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }
            }
        }
    }
} 