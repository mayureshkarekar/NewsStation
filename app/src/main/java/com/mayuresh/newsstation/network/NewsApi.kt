package com.mayuresh.newsstation.network

import com.mayuresh.newsstation.model.NewsResponse
import retrofit2.Response
import retrofit2.http.GET

// region constants
const val BASE_URL = "https://827a9d57-d94a-422d-bed0-81a6c374a739.mock.pstmn.io/"
const val NEWS = "news"
// endregion

interface NewsAPI {
    @GET(NEWS)
    suspend fun getNews(): Response<NewsResponse>
}