package com.mayuresh.newsstation.screeen

import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mayuresh.newsstation.model.News
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NewsDetailScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    /**
     * Test for checking if the view has click action.
     **/
    @Test
    fun testOpenInBrowserClick_ExpectedHasClickAction() {
        // Rendering the view.
        composeTestRule.setContent {
            NewsDetail(
                news = News(
                    id = 1,
                    author = "Author Name",
                    content = "This is Sample Content",
                    description = "This is Sample Description",
                    publishedAt = "2022-01-15T14:30:00",
                    title = "This is Sample Title",
                    url = "https://picsum.photos/300/150.jpg",
                    urlToImage = "https://picsum.photos/300/150.jpg"
                )
            )
        }

        // Verifying if the button has click action.
        val btnOpenInBrowser = composeTestRule.onNodeWithTag(TEST_TAG_OPEN_IN_BROWSER)
        btnOpenInBrowser.assertHasClickAction()
    }
}