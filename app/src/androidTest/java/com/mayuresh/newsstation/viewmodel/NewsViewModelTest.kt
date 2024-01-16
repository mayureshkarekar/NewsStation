package com.mayuresh.newsstation.viewmodel

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import app.cash.turbine.test
import com.mayuresh.newsstation.database.NewsDatabase
import com.mayuresh.newsstation.network.NewsAPI
import com.mayuresh.newsstation.repository.NewsRepository
import com.mayuresh.newsstation.utils.Resource
import com.mayuresh.newsstation.utils.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@ExperimentalCoroutinesApi
class NewsViewModelTest {
    private lateinit var mockWebServer: MockWebServer
    private lateinit var newsDatabase: NewsDatabase
    private lateinit var newsRepository: NewsRepository
    private lateinit var newsViewModel: NewsViewModel
    private val testDispatcher = StandardTestDispatcher()

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
            ApplicationProvider.getApplicationContext(), newsAPI, newsDatabase
        )
        Dispatchers.setMain(testDispatcher)
    }

    /**
     * Test case for non empty news list response in viewmodel.
     **/
    @Test
    fun testGetNews_ExpectedNonEmptyResponse() = runBlocking {
        val mockResponse = MockResponse()
        val content = Utils.readResourceFile("/mock_news_success.json")

        // Setting the mock response.
        mockResponse.apply {
            setResponseCode(200)
            setBody(content)
        }
        mockWebServer.enqueue(mockResponse)

        // Making API call.
        newsViewModel = NewsViewModel(newsRepository)
        mockWebServer.takeRequest()

        // Scheduling the Coroutines
        testDispatcher.scheduler.advanceUntilIdle()

        newsRepository.news.test {
            val newsResource = awaitItem()
            Assert.assertEquals(10, newsResource.data!!.size)
            cancel()
        }
    }

    /**
     * Test case for error response in viewmodel.
     **/
    @Test
    fun testGetNews_ExpectedErrorResponse() = runBlocking {
        val mockResponse = MockResponse()
        val content = Utils.readResourceFile("/mock_news_not_found.json")

        // Setting the mock response.
        mockResponse.apply {
            setResponseCode(200)
            setBody(content)
        }
        mockWebServer.enqueue(mockResponse)

        // Making API call.
        newsViewModel = NewsViewModel(newsRepository)
        mockWebServer.takeRequest()

        // Scheduling the Coroutines
        testDispatcher.scheduler.advanceUntilIdle()

        newsRepository.news.test {
            val newsResource = awaitItem()
            assert(newsResource is Resource.Error)
            cancel()
        }
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        mockWebServer.shutdown()
        newsDatabase.close()
    }
}