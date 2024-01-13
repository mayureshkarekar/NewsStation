package com.mayuresh.newsstation.model

import com.google.gson.annotations.SerializedName

data class Source(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String
)

data class News(
    @SerializedName("author") val author: String,
    @SerializedName("content") val content: String,
    @SerializedName("description") val description: String,
    @SerializedName("publishedAt") val publishedAt: String,
    @SerializedName("source") val source: Source,
    @SerializedName("title") val title: String,
    @SerializedName("url") val url: String,
    @SerializedName("urlToImage") val urlToImage: String
)

data class NewsResponse(
    @SerializedName("status") val status: String,
    @SerializedName("totalResults") val totalResults: Int,
    @SerializedName("articles") val articles: List<News>
)