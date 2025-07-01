package com.example.projekatrazvoj.repository

import com.example.projekatrazvoj.db.NewbornDao
import com.example.projekatrazvoj.model.Newborn
import com.example.projekatrazvoj.model.NewbornFavorite
import com.example.projekatrazvoj.network.NewbornApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import com.example.projekatrazvoj.model.toFavorite

class NewbornRepository(
    private val api: NewbornApiService,
    private val dao: NewbornDao
) {
    fun getAllNewborns(): Flow<List<Newborn>> = dao.getAllNewborns()
    fun getFavorites(): Flow<List<NewbornFavorite>> = dao.getAllNewbornFavorites()
    suspend fun addFavorite(newborn: Newborn) = withContext(Dispatchers.IO) {
        dao.insertNewbornFavorite(newborn.toFavorite())
    }
    suspend fun removeFavorite(newborn: Newborn) = withContext(Dispatchers.IO) {
        dao.deleteNewbornFavorite(newborn.toFavorite())
    }
    suspend fun isFavorite(id: Int): Boolean = withContext(Dispatchers.IO) {
        dao.getFavoriteById(id) != null
    }
    suspend fun refreshNewborns() {
        val remote = api.getNewborns().result
        dao.clearAll()
        dao.insertAll(remote)
    }
    suspend fun getById(id: Int) = dao.getById(id)
    suspend fun getFavoriteById(id: Int) = dao.getFavoriteById(id)
} 