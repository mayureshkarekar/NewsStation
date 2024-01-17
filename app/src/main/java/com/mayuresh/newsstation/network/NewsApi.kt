package com.mayuresh.newsstation.network

import com.mayuresh.newsstation.model.NewsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsAPI {
    @GET(EVERYTHING)
    suspend fun getNews(
        @Query(QUERY) query: String = DEFAULT_QUERY,
        @Query(PAGE_SIZE) pageSize: Int = DEFAULT_PAGE_SIZE
    ): Response<NewsResponse>
}

// region constants
const val BASE_URL = "https://newsapi.org/v2/"
const val API_KEY = "apiKey"
const val EVERYTHING = "everything"
const val QUERY = "q"
const val DEFAULT_QUERY = "bitcoin"
const val PAGE_SIZE = "pageSize"
const val DEFAULT_PAGE_SIZE = 30
// endregion