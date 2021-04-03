package com.news.data.core.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Blob

@Entity(tableName = "category")
data class Category(
        @PrimaryKey(autoGenerate = true)
        var id: Int,
        @ColumnInfo(name = "name", ) var name: String?,
        @ColumnInfo(name = "start") var start: Int?,
)