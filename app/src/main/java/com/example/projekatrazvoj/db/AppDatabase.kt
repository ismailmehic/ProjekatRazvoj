package com.example.projekatrazvoj.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.projekatrazvoj.model.Newborn
import com.example.projekatrazvoj.model.Died
import com.example.projekatrazvoj.model.DiedFavorite
import com.example.projekatrazvoj.model.NewbornFavorite

@Database(entities = [Newborn::class, Died::class, DiedFavorite::class, NewbornFavorite::class], version = 8, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun newbornDao(): NewbornDao
    abstract fun diedDao(): DiedDao
} 