package com.mayuresh.newsstation.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mayuresh.newsstation.model.NEWS_TABLE_NAME
import com.mayuresh.newsstation.model.News

@Dao
interface NewsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNews(news: List<News>)

    @Query("SELECT * FROM $NEWS_TABLE_NAME")
    suspend fun getNews(): List<News>

    @Query("SELECT * FROM $NEWS_TABLE_NAME WHERE id = :newsId")
    suspend fun getNewsDetail(newsId: Long): News
}