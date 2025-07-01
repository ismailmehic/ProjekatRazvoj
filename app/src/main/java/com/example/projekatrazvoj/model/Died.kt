package com.example.projekatrazvoj.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

@Entity(tableName = "died")
data class Died(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @Json(name = "entity")
    val entity: String?,
    @Json(name = "canton")
    val canton: String?,
    @Json(name = "municipality")
    val municipality: String?,
    @Json(name = "institution")
    val institution: String?,
    @Json(name = "year")
    val year: Int?,
    @Json(name = "month")
    val month: Int?,
    @Json(name = "dateUpdate")
    val dateUpdate: String?,
    @Json(name = "maleTotal")
    val maleTotal: Int?,
    @Json(name = "femaleTotal")
    val femaleTotal: Int?,
    @Json(name = "total")
    val total: Int?
)

@Entity(tableName = "died_favorites")
data class DiedFavorite(
    @PrimaryKey val id: Int,
    val entity: String?,
    val canton: String?,
    val municipality: String?,
    val institution: String?,
    val year: Int?,
    val month: Int?,
    val dateUpdate: String?,
    val maleTotal: Int?,
    val femaleTotal: Int?,
    val total: Int?
)

fun Died.toFavorite() = DiedFavorite(
    id, entity, canton, municipality, institution, year, month, dateUpdate, maleTotal, femaleTotal, total
) 