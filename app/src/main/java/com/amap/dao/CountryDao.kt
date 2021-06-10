package com.amap.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.amap.entity.Country

@Dao
interface CountryDao {
    @Query("SELECT * FROM Country")
    fun getAll(): MutableList<Country>

    @Insert
    fun insertAll(vararg countries: Country)

    @Delete
    fun delete(country: Country)

    @Delete
    fun deleteAll(vararg countries: Country)
}