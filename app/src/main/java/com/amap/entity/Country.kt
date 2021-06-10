package com.amap.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Country(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "capital") val capital: String,
    @ColumnInfo(name = "population") val population: Int?,
    @ColumnInfo(name = "currency") val currency: String?,
    @ColumnInfo(name = "flag") val flag: String?,
    @ColumnInfo(name = "latitude") val latitude: Float,
    @ColumnInfo(name = "longitude") val longitude: Float,
    @ColumnInfo(name = "continent") val continent: String
)