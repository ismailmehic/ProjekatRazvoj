package com.example.projekatrazvoj.repository

import com.example.projekatrazvoj.db.DiedDao
import com.example.projekatrazvoj.model.Died
import com.example.projekatrazvoj.network.DiedApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import com.example.projekatrazvoj.model.DiedFavorite
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.flow.flow
import android.util.Log
import com.example.projekatrazvoj.model.toFavorite

class DiedRepository(
    private val api: DiedApiService,
    private val dao: DiedDao
) {
    fun getAllDied(): Flow<List<Died>> = dao.getAllDied()
    fun getFavorites(): Flow<List<DiedFavorite>> = dao.getAllDiedFavorites()
    suspend fun addFavorite(died: Died) = withContext(Dispatchers.IO) {
        dao.insertDiedFavorite(died.toFavorite())
    }
    suspend fun removeFavorite(died: Died) = withContext(Dispatchers.IO) {
        dao.deleteDiedFavorite(died.toFavorite())
    }
    suspend fun isFavorite(id: Int): Boolean = withContext(Dispatchers.IO) {
        dao.getFavoriteById(id) != null
    }
    suspend fun refreshDied() {
        val remote = api.getDied().result
        dao.clearAll()
        dao.insertAll(remote)
    }
    suspend fun getById(id: Int) = dao.getById(id)
    suspend fun getFavoriteById(id: Int) = dao.getFavoriteById(id)
} 