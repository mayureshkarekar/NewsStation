package com.mayuresh.newsstation.utils.network

import com.google.gson.JsonSyntaxException
import com.mayuresh.newsstation.network.NewsAPI
import com.mayuresh.newsstation.utils.utils.Utils
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NewsApiTest {
    private lateinit var mockWebServer: MockWebServer
    private lateinit var newsAPI: NewsAPI

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        newsAPI = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NewsAPI::class.java)
    }

    /**
     * Test case for non empty news list response.
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
        val response = newsAPI.getNews()
        mockWebServer.takeRequest()

        // Validating the result.
        assertEquals(10, response.body()!!.articles.size)
    }

    /**
     * Test case for not found error code.
     **/
    @Test
    fun testGetNews_ExpectedNotFoundResponse() = runTest {
        val mockResponse = MockResponse()
        val content = Utils.readResourceFile("/mock_news_not_found.json")

        // Setting the mock response.
        mockResponse.apply {
            setResponseCode(404)
            setBody(content)
        }
        mockWebServer.enqueue(mockResponse)

        // Making API call.
        val response = newsAPI.getNews()
        mockWebServer.takeRequest()

        // Validating the result.
        assertEquals(404, response.code())
    }

    /**
     * Test case for malformed JSON/invalid response.
     **/
    @Test(expected = JsonSyntaxException::class)
    fun testGetNews_ExpectedMalformedException() = runTest {
        val mockResponse = MockResponse()
        val content = Utils.readResourceFile("/mock_news_malformed.json")

        // Setting the mock response.
        mockResponse.apply {
            setResponseCode(200)
            setBody(content)
        }
        mockWebServer.enqueue(mockResponse)

        // Making API call.
        val response = newsAPI.getNews()
        mockWebServer.takeRequest()

        // Validating the result.
        assertEquals(10, response.body()!!.articles.size)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }
}