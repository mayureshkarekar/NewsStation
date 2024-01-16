package com.mayuresh.newsstation.repository

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.mayuresh.newsstation.database.NewsDatabase
import com.mayuresh.newsstation.network.NewsAPI
import com.mayuresh.newsstation.utils.Resource
import com.mayuresh.newsstation.utils.Utils
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NewsRepositoryTest {
    private lateinit var mockWebServer: MockWebServer
    private lateinit var newsDatabase: NewsDatabase
    private lateinit var newsRepository: NewsRepository

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        val newsAPI = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NewsAPI::class.java)
        newsDatabase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            NewsDatabase::class.java
        ).allowMainThreadQueries().build()
        newsRepository = NewsRepository(
            ApplicationProvider.getApplicationContext(),
            newsAPI,
            newsDatabase
        )
    }

    /**
     * Test case for non empty news list response in repository.
     **/
    @Test
    fun testGetNews_ExpectedNonEmptyResponse() = runTest {
        val mockResponse = MockResponse()
        val content = Utils.readResourceFile("/mock_news_success.json")

        // Setting the mock response.
        mockResponse.apply {
            setResponseCode(200)
            setBody(content)
        }
        mockWebServer.enqueue(mockResponse)

        // Making API call.
        newsRepository.getNews()
        mockWebServer.takeRequest()

        // Validating the result.
        assertEquals(10, newsRepository.news.value.data!!.size)
    }

    /**
     * Test case for error response in repository.
     **/
    @Test
    fun testGetNews_ExpectedErrorResponse() = runTest {
        val mockResponse = MockResponse()
        val content = Utils.readResourceFile("/mock_news_not_found.json")

        // Setting the mock response.
        mockResponse.apply {
            setResponseCode(200)
            setBody(content)
        }
        mockWebServer.enqueue(mockResponse)

        // Making API call.
        newsRepository.getNews()
        mockWebServer.takeRequest()

        // Validating the result.
        assert(newsRepository.news.value is Resource.Error)
    }

    /**
     * Test case for news details response from API and local database.
     **/
    @Test
    fun testGetNewsDetails_ExpectedSingleItemResponse() = runTest {
        val newsId: Long = 1
        val mockResponse = MockResponse()
        val content = Utils.readResourceFile("/mock_news_success.json")

        // Setting the mock response.
        mockResponse.apply {
            setResponseCode(200)
            setBody(content)
        }
        mockWebServer.enqueue(mockResponse)

        // Making API call.
        newsRepository.getNews()
        mockWebServer.takeRequest()

        // Fetching News Details from local database.
        newsRepository.getNewsDetail(newsId)

        // Validating the result.
        assertEquals(newsId, newsRepository.newsDetail.value.data!!.id)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
        newsDatabase.close()
    }
}