package com.example.projekatrazvoj.network

import com.example.projekatrazvoj.model.Died
import retrofit2.http.POST
import retrofit2.http.Header
import retrofit2.http.Body

// Wrapper za API odgovor
data class DiedResponse(
    val errors: List<String>,
    val result: List<Died>
)

// Body za POST zahtjev
data class DiedRequest(
    val datasetId: Int = 1
)

interface DiedApiService {
    @POST("DiedByRequestDate/list")
    suspend fun getDied(
        @Header("Authorization") token: String = "Bearer eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiIyMTY5IiwibmJmIjoxNzUxMzcyNzI5LCJleHAiOjE3NTE0NTkxMjksImlhdCI6MTc1MTM3MjcyOX0.8Xl46b4T654aWOwYWheXp06o3gX-Z2367zjrGEXluzJ8fmJhM8HPlsUYXsQIQvpymzr0ogGaJNQxmUp2J8umRA",
        @Body body: DiedRequest = DiedRequest()
    ): DiedResponse
} 