package com.example.projekatrazvoj.db

import androidx.room.*
import com.example.projekatrazvoj.model.Died
import com.example.projekatrazvoj.model.DiedFavorite
import kotlinx.coroutines.flow.Flow

@Dao
interface DiedDao {
    @Query("SELECT * FROM died ORDER BY dateUpdate DESC")
    fun getAllDied(): Flow<List<Died>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(died: List<Died>)

    @Query("DELETE FROM died")
    suspend fun clearAll()

    @Update
    suspend fun update(died: Died)

    @Query("SELECT * FROM died WHERE id = :id")
    suspend fun getById(id: Int): Died?

    @Query("SELECT * FROM died_favorites")
    fun getAllDiedFavorites(): Flow<List<DiedFavorite>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDiedFavorite(favorite: DiedFavorite)

    @Delete
    suspend fun deleteDiedFavorite(favorite: DiedFavorite)

    @Query("SELECT * FROM died_favorites WHERE id = :id")
    suspend fun getFavoriteById(id: Int): DiedFavorite?
} 