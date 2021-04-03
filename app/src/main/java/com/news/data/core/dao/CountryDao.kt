package com.news.data.core.dao

import androidx.room.Dao
import androidx.room.Query
import com.news.data.core.model.Country

@Dao
interface CountryDao {
    @Query("SELECT * FROM country")
    fun getAll(): List<Country>
    @Query("SELECT * FROM country where code=:code")
    fun findByCode(code : String): Country
    @Query("SELECT * FROM country where id=:id")
    fun findById(id : Int): Country
}