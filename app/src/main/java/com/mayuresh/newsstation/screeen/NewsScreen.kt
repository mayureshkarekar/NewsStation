package com.mayuresh.newsstation.screeen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.mayuresh.newsstation.CircularProgress
import com.mayuresh.newsstation.NoDataView
import com.mayuresh.newsstation.R
import com.mayuresh.newsstation.model.News
import com.mayuresh.newsstation.utils.DateTimeUtils
import com.mayuresh.newsstation.utils.DateTimeUtils.DAY_DATE_TIME_FORMAT
import com.mayuresh.newsstation.utils.DateTimeUtils.SERVER_DATE_TIME_FORMAT
import com.mayuresh.newsstation.utils.Resource
import com.mayuresh.newsstation.viewmodel.NewsViewModel

@Composable
fun NewsScreen(onClick: (Long) -> Unit) {
    val newsViewModel: NewsViewModel = hiltViewModel()
    val newsResource: State<Resource<List<News>>> = newsViewModel.news.collectAsState()

    when (newsResource.value) {
        is Resource.Loading -> {
            CircularProgress()
        }

        is Resource.Success -> {
            newsResource.value.data?.let { newsList ->
                LazyColumn {
                    items(newsList) {
                        NewsItem(news = it, onClick = onClick)
                    }
                }
            }
        }

        is Resource.Error -> {
            NoDataView(message = stringResource(id = (R.string.no_data_found)))
        }
    }
}

@Composable
fun NewsItem(news: News, onClick: (Long) -> Unit) {
    ConstraintLayout(
        modifier = Modifier
            .padding(10.dp)
            .clickable { onClick(news.id) }
            .background(color = Color.White)
            .clip(RoundedCornerShape(8.dp))
            .shadow(2.dp)
    ) {
        val (image, title, content, author, date) = createRefs()

        Image(
            modifier = Modifier
                .constrainAs(image) { top.linkTo(parent.top) }
                .fillMaxWidth()
                .height(200.dp),
            painter = rememberAsyncImagePainter(model = news.urlToImage),
            contentDescription = "Article Image",
            contentScale = ContentScale.Crop
        )

        Text(
            modifier = Modifier
                .constrainAs(title) { bottom.linkTo(image.bottom) }
                .fillMaxWidth()
                .padding(10.dp, 4.dp),
            style = MaterialTheme.typography.headlineSmall,
            text = news.title,
            color = Color.White,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Text(
            modifier = Modifier
                .constrainAs(content) { top.linkTo(image.bottom) }
                .fillMaxWidth()
                .padding(10.dp, 4.dp),
            style = MaterialTheme.typography.bodyMedium,
            text = news.content,
            maxLines = 2,
            color = colorResource(id = R.color.text_color),
            overflow = TextOverflow.Ellipsis
        )

        Text(
            modifier = Modifier
                .constrainAs(author) {
                    top.linkTo(content.bottom)
                    start.linkTo(content.start)
                }
                .padding(10.dp, 4.dp, 10.dp, 10.dp),
            style = MaterialTheme.typography.bodySmall,
            text = stringResource(id = R.string.formatted_author_name, news.author)
        )

        Text(
            modifier = Modifier
                .constrainAs(date) {
                    top.linkTo(content.bottom)
                    end.linkTo(content.end)
                }
                .padding(10.dp, 4.dp, 10.dp, 10.dp),
            style = MaterialTheme.typography.bodySmall,
            text = DateTimeUtils.convertDateTime(
                news.publishedAt, SERVER_DATE_TIME_FORMAT, DAY_DATE_TIME_FORMAT
            ).orEmpty()
        )
    }
}