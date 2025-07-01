package com.example.projekatrazvoj.db

import androidx.room.*
import com.example.projekatrazvoj.model.Newborn
import com.example.projekatrazvoj.model.NewbornFavorite
import kotlinx.coroutines.flow.Flow

@Dao
interface NewbornDao {
    @Query("SELECT * FROM newborns ORDER BY dateUpdate DESC")
    fun getAllNewborns(): Flow<List<Newborn>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(newborns: List<Newborn>)

    @Query("DELETE FROM newborns")
    suspend fun clearAll()

    @Update
    suspend fun update(newborn: Newborn)

    @Query("SELECT * FROM newborns WHERE id = :id")
    suspend fun getById(id: Int): Newborn?

    @Query("SELECT * FROM newborn_favorites")
    fun getAllNewbornFavorites(): Flow<List<NewbornFavorite>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNewbornFavorite(favorite: NewbornFavorite)

    @Delete
    suspend fun deleteNewbornFavorite(favorite: NewbornFavorite)

    @Query("SELECT * FROM newborn_favorites WHERE id = :id")
    suspend fun getFavoriteById(id: Int): NewbornFavorite?
} 