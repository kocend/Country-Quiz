package com.amap.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.amap.dao.CountryDao
import com.amap.entity.Country
import java.io.Serializable

@Database(entities = [Country::class], version = 1)
abstract class AppDatabase : RoomDatabase(), Serializable {
    abstract fun countryDao(): CountryDao
}