package com.mayuresh.newsstation.network

import android.content.Context
import com.mayuresh.newsstation.R
import okhttp3.Interceptor
import okhttp3.Response

class ApiKeyInterceptor(private val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val originalUrl = originalRequest.url

        val newUrl = originalUrl.newBuilder()
            .addQueryParameter(API_KEY, context.getString(R.string.rest_api_key))
            .build()

        val newRequest = originalRequest.newBuilder().url(newUrl).build()

        return chain.proceed(newRequest)
    }
}