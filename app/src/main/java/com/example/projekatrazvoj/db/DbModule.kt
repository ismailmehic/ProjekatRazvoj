package com.example.projekatrazvoj.db

import android.content.Context
import androidx.room.Room
import com.example.projekatrazvoj.network.NewbornApiService
import com.example.projekatrazvoj.network.DiedApiService
import com.example.projekatrazvoj.network.RetrofitInstance

object DbModule {
    fun provideDatabase(context: Context) = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "newborns_db"
    ).fallbackToDestructiveMigration()
     .build()

    fun provideNewbornApiService(): NewbornApiService = RetrofitInstance.newbornApi
    fun provideDiedApiService(): DiedApiService = RetrofitInstance.diedApi
} 