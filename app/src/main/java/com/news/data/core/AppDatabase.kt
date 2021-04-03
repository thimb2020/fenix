package com.news.data.core


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.news.data.core.dao.CountryDao
import com.news.data.core.dao.NewsDao
import com.news.data.core.model.Category
import com.news.data.core.model.Country
import com.news.data.core.model.Link

/**
 * Copyright:MyApplication
 * Author: liyang <br></br>
 * Date:2018/6/15 下午5:07<br></br>
 * Desc: <br></br>
 */
@Database(entities = [Category::class, Link::class, Country::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract val newsDao: NewsDao?
    abstract val countryDao: CountryDao?

    companion object {
        @Volatile
        private var instance: AppDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(context,
                AppDatabase::class.java, "sites.db")
                //.fallbackToDestructiveMigration().allowMainThreadQueries()
                .allowMainThreadQueries()
                .build()
    }
}