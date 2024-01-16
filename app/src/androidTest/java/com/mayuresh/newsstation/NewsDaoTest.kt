package com.mayuresh.newsstation

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.mayuresh.newsstation.database.NewsDao
import com.mayuresh.newsstation.database.NewsDatabase
import com.mayuresh.newsstation.model.News
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Test

class NewsDaoTest {
    private lateinit var newsDatabase: NewsDatabase
    private lateinit var newsDao: NewsDao

    @Before()
    fun setUp() {
        newsDatabase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            NewsDatabase::class.java
        ).allowMainThreadQueries().build()
        newsDao = newsDatabase.getNewsDao()
    }

    // region insert test
    /**
     * Test case for inserting single news item.
     **/
    @Test
    fun insertNews_expectedSingleNews() = runBlocking {
        // Adding news to the database.
        val news = News(
            id = 1,
            author = "Author Name",
            content = "This is Sample Content",
            description = "This is Sample Description",
            publishedAt = "2022-01-15T14:30:00",
            title = "This is Sample Title",
            url = "https://picsum.photos/300/150.jpg",
            urlToImage = "https://picsum.photos/300/150.jpg"
        )
        newsDao.addNews(listOf(news))

        // Verifying the result.
        val savedNews = newsDao.getNews()
        assertEquals(1, savedNews.size)
    }

    /**
     * Test case for inserting multiple news item.
     **/
    @Test
    fun insertNews_expectedMultipleNews() = runBlocking {
        // Adding news to the database.
        val news = News(
            id = 1,
            author = "Author Name",
            content = "This is Sample Content",
            description = "This is Sample Description",
            publishedAt = "2022-01-15T14:30:00",
            title = "This is Sample Title",
            url = "https://picsum.photos/300/150.jpg",
            urlToImage = "https://picsum.photos/300/150.jpg"
        )

        val news2 = News(
            id = 2,
            author = "Author Name",
            content = "This is Sample Content",
            description = "This is Sample Description",
            publishedAt = "2022-01-15T14:30:00",
            title = "This is Sample Title",
            url = "https://picsum.photos/300/150.jpg",
            urlToImage = "https://picsum.photos/300/150.jpg"
        )
        newsDao.addNews(listOf(news, news2))

        // Verifying the result.
        val savedNews = newsDao.getNews()
        assertEquals(2, savedNews.size)
    }
    // endregion

    // region select test
    /**
     * Test case for selecting news item by id.
     **/
    @Test
    fun selectNewsDetail_expectedSameNewsDetail() = runBlocking {
        // Adding news to the database.
        val newsId: Long = 1
        val news = News(
            id = newsId,
            author = "Author Name",
            content = "This is Sample Content",
            description = "This is Sample Description",
            publishedAt = "2022-01-15T14:30:00",
            title = "This is Sample Title",
            url = "https://picsum.photos/300/150.jpg",
            urlToImage = "https://picsum.photos/300/150.jpg"
        )
        newsDao.addNews(listOf(news))

        // Verifying the result.
        val savedNews = newsDao.getNewsDetail(newsId)
        assertEquals(newsId, savedNews.id)
    }

    /**
     * Test case for selecting news item by id.
     **/
    @Test
    fun selectNewsDetail_inputDifferentId_expectedNotEquals() = runBlocking {
        // Adding news to the database.
        val newsId: Long = 1
        val news = News(
            id = newsId,
            author = "Author Name",
            content = "This is Sample Content",
            description = "This is Sample Description",
            publishedAt = "2022-01-15T14:30:00",
            title = "This is Sample Title",
            url = "https://picsum.photos/300/150.jpg",
            urlToImage = "https://picsum.photos/300/150.jpg"
        )
        newsDao.addNews(listOf(news))

        // Verifying the result.
        val savedNews = newsDao.getNewsDetail(newsId)
        assertNotEquals(2, savedNews.id)
    }
    // endregion

    @After
    fun tearDown() {
        newsDatabase.close()
    }
}