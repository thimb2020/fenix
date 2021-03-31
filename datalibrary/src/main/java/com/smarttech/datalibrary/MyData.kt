package com.smarttech.datalibrary

import acr.browser.lightning.database.dao.AppDatabase
import acr.browser.lightning.database.dao.model.Category
import android.content.Context
import android.util.Log
import com.smarttech.datalibrary.utils.FileUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import mozilla.components.feature.tab.collections.TabCollectionStorage
import mozilla.components.feature.top.sites.PinnedSiteStorage
import mozilla.components.feature.top.sites.db.TopSiteDatabase_Impl
import java.util.concurrent.atomic.AtomicBoolean


class MyData {
    fun getTopSites(ctx: Context):  MutableList<Pair<String, String>>? {
        FileUtil.attachDbFromDropBox(ctx, "l2bt4m1rn5iknnz", "sites")
        val db = AppDatabase(ctx)
        var links = db.newsDao?.findLinksStart()
        var pairs = links?.map {
            Pair(
                it.name!!,
                it.url!!
            )
        }
        return pairs?.toMutableList()
    }

    suspend fun syncTopSites(ctx: Context) {
        var pairs = getTopSites(ctx)
        PinnedSiteStorage(ctx).addAllPinnedSites(pairs!!,true)
    }


    suspend fun getCats(ctx: Context): List<Category>? {
        val db = AppDatabase(ctx)
        return  db.newsDao?.findAllCategory()
    }
/*
    suspend fun syncCollections(ctx: Context) {
        val db = AppDatabase(ctx)
        var categories = db.newsDao?.findAllCategory()
        var tabCollectionStorage = TabCollectionStorage(ctx)
        categories?.forEach {
            tabCollectionStorage.createCollection(it.name!!)
        }
    }*/
}