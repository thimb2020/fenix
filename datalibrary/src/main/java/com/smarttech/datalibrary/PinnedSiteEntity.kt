package com.smarttech.datalibrary


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import mozilla.components.feature.top.sites.TopSite
import mozilla.components.feature.top.sites.TopSite.Type.DEFAULT
import mozilla.components.feature.top.sites.TopSite.Type.PINNED

/**
 * Internal entity representing a pinned site.
 */
@Entity(tableName = "top_sites")
data class PinnedSiteEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long? = null,

    @ColumnInfo(name = "title")
    var title: String,

    @ColumnInfo(name = "url")
    var url: String,

    @ColumnInfo(name = "is_default")
    var isDefault: Boolean = false,

    @ColumnInfo(name = "created_at")
    var createdAt: Long = System.currentTimeMillis()
) {
    internal fun toTopSite(): TopSite {
        val type = if (isDefault) DEFAULT else PINNED
        return TopSite(
            id,
            title,
            url,
            createdAt,
            type
        )
    }
}

internal fun TopSite.toPinnedSite(): PinnedSiteEntity {
    return PinnedSiteEntity(
        id = id,
        title = title ?: "",
        url = url,
        isDefault = type === DEFAULT,
        createdAt = createdAt ?: 0
    )
}
