package com.mayuresh.newsstation.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mayuresh.newsstation.model.News

@Database(entities = [News::class], version = NEWS_DATABASE_VERSION)
abstract class NewsDatabase : RoomDatabase() {
    abstract fun getNewsDao(): NewsDao
}

// region constants
const val NEWS_DATABASE_NAME = "news_database"
private const val NEWS_DATABASE_VERSION = 1
// endregion