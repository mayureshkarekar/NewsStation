package com.mayuresh.newsstation.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = NEWS_TABLE_NAME)
data class News(
    @SerializedName("id")
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    @SerializedName("author") val author: String,
    @SerializedName("content") val content: String,
    @SerializedName("description") val description: String,
    @SerializedName("publishedAt") val publishedAt: String,
    @SerializedName("title") val title: String,
    @SerializedName("url") val url: String,
    @SerializedName("urlToImage") val urlToImage: String
)

data class NewsResponse(
    @SerializedName("status") val status: String,
    @SerializedName("totalResults") val totalResults: Int,
    @SerializedName("articles") val articles: List<News>
)

// region constants
const val NEWS_TABLE_NAME = "news"
// endregion