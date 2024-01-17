package com.mayuresh.newsstation.screeen

import android.content.Intent
import android.net.Uri
import android.webkit.URLUtil
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.mayuresh.newsstation.R
import com.mayuresh.newsstation.model.News
import com.mayuresh.newsstation.utils.DateTimeUtils
import com.mayuresh.newsstation.utils.Resource
import com.mayuresh.newsstation.viewmodel.NewsDetailViewModel

@Composable
fun NewsDetailScreen() {
    val newsDetailViewModel: NewsDetailViewModel = hiltViewModel()
    val newsDetailResource: State<Resource<News>> = newsDetailViewModel.newsDetails.collectAsState()

    when (newsDetailResource.value) {
        is Resource.Loading -> {
            CircularProgress()
        }

        is Resource.Success -> {
            newsDetailResource.value.data?.let {
                NewsDetail(it)
            }
        }

        is Resource.Error -> {
            NoDataView(message = stringResource(id = (R.string.no_data_found)))
        }
    }
}

const val TEST_TAG_OPEN_IN_BROWSER = "open_in_browser"

@Composable
fun NewsDetail(news: News) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {
        val (image, title, content, author, date, openInBrowser) = createRefs()

        Image(
            modifier = Modifier
                .constrainAs(image) { top.linkTo(parent.top) }
                .fillMaxWidth()
                .height(300.dp),
            painter = rememberAsyncImagePainter(model = news.urlToImage),
            contentDescription = "Article Image",
            contentScale = ContentScale.Crop
        )

        Text(
            modifier = Modifier
                .constrainAs(title) { top.linkTo(image.bottom) }
                .fillMaxWidth()
                .padding(10.dp, 8.dp),
            style = MaterialTheme.typography.headlineSmall,
            text = news.title
        )

        Text(
            modifier = Modifier
                .constrainAs(date) {
                    top.linkTo(title.bottom)
                    end.linkTo(content.end)
                }
                .padding(10.dp, 2.dp),
            style = MaterialTheme.typography.bodySmall,
            text = DateTimeUtils.convertDateTime(
                news.publishedAt,
                DateTimeUtils.SERVER_DATE_TIME_FORMAT,
                DateTimeUtils.DAY_DATE_TIME_FORMAT
            ).orEmpty()
        )

        Text(
            modifier = Modifier
                .constrainAs(content) { top.linkTo(date.bottom) }
                .fillMaxWidth()
                .padding(10.dp, 8.dp),
            style = MaterialTheme.typography.bodyLarge,
            text = news.content,
            color = colorResource(id = R.color.text_color)
        )

        Text(
            modifier = Modifier
                .constrainAs(author) { top.linkTo(content.bottom) }
                .padding(10.dp, 2.dp),
            style = MaterialTheme.typography.bodySmall,
            text = stringResource(id = R.string.formatted_author_name, news.author)
        )

        if (URLUtil.isHttpsUrl(news.url)) {
            val context = LocalContext.current
            val browserIntent = remember { Intent(Intent.ACTION_VIEW, Uri.parse(news.url)) }

            Text(
                modifier = Modifier
                    .testTag(TEST_TAG_OPEN_IN_BROWSER)
                    .constrainAs(openInBrowser) {
                        bottom.linkTo(parent.bottom)
                    }
                    .fillMaxWidth()
                    .background(color = colorResource(id = R.color.purple_200))
                    .clickable {
                        if (browserIntent.resolveActivity(context.packageManager) != null) {
                            context.startActivity(browserIntent)
                        }
                    }
                    .padding(24.dp, 20.dp),
                style = MaterialTheme.typography.titleSmall,
                text = stringResource(id = R.string.tap_to_know_more),
                textAlign = TextAlign.Center,
                color = Color.White
            )
        }
    }
}