package com.example.projekatrazvoj.network

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

object RetrofitInstance {
    val newbornApi: NewbornApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://odp.iddeea.gov.ba:8096/api/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(NewbornApiService::class.java)
    }
    val diedApi: DiedApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://odp.iddeea.gov.ba:8096/api/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(DiedApiService::class.java)
    }
} 