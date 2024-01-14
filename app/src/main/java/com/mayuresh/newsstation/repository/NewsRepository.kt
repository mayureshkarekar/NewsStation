package com.mayuresh.newsstation.repository

import android.content.Context
import com.mayuresh.newsstation.database.NewsDatabase
import com.mayuresh.newsstation.model.News
import com.mayuresh.newsstation.network.NewsAPI
import com.mayuresh.newsstation.utils.NetworkUtils
import com.mayuresh.newsstation.utils.Resource
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

class NewsRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val newsAPI: NewsAPI,
    private val newsDatabase: NewsDatabase
) {
    // region news list
    private val _news = MutableStateFlow<Resource<List<News>>>(Resource.Loading())
    val news: StateFlow<Resource<List<News>>>
        get() = _news

    /**
     * Fetches the list of news from server if internet is available, else from database.
     **/
    suspend fun getNews() {
        _news.emit(Resource.Loading())

        try {
            if (NetworkUtils.isInternetAvailable(context)) {
                // Fetching data from server.
                val response = newsAPI.getNews()
                if (response.isSuccessful && response.body() != null) {
                    response.body()?.let {
                        val newsDao = newsDatabase.getNewsDao()
                        newsDao.addNews(it.articles)
                        _news.emit(Resource.Success(newsDao.getNews()))
                        Timber.d("Fetching news from server successful.")
                    }
                } else {
                    _news.emit(Resource.Error(response.errorBody().toString()))
                    Timber.e("Failed to fetch news ${response.errorBody().toString()}")
                }
            } else {
                // Fetching cached data.
                val news = newsDatabase.getNewsDao().getNews()
                _news.emit(Resource.Success(news))
                Timber.d("Fetching news from local database successful.")
            }
        } catch (e: IOException) {
            _news.emit(Resource.Error("Internet not available."))
            Timber.e(e, "Failed to fetch news ${e.message}.")
        } catch (e: HttpException) {
            _news.emit(Resource.Error("Unexpected response."))
            Timber.e(e, "Failed to fetch news ${e.message}.")
        } catch (e: Exception) {
            _news.emit(Resource.Error("Something went wrong."))
            Timber.e(e, "Failed to fetch news ${e.message}.")
        }
    }
    // endregion

    // region news details
    private val _newsDetail = MutableStateFlow<Resource<News>>(Resource.Loading())
    val newsDetail: StateFlow<Resource<News>>
        get() = _newsDetail

    /**
     * Returns the details of news for given id.
     * @param newsId - ID of news for which details to be fetched.
     **/
    suspend fun getNewsDetail(newsId: Long) {
        _newsDetail.emit(Resource.Loading())
        val newsDetail = newsDatabase.getNewsDao().getNewsDetail(newsId)
        _newsDetail.emit(Resource.Success(newsDetail))
        Timber.d("Fetched news details.")
    }
    // endregion
}