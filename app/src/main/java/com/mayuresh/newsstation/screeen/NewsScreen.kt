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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
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

        Image(modifier = Modifier
            .constrainAs(image) { top.linkTo(parent.top) }
            .fillMaxWidth()
            .height(150.dp),
            painter = rememberAsyncImagePainter(model = news.urlToImage),
            contentDescription = "Article Image",
            contentScale = ContentScale.Crop)

        Text(
            modifier = Modifier
                .constrainAs(title) { bottom.linkTo(image.bottom) }
                .fillMaxWidth()
                .padding(10.dp),
            text = news.title,
        )

        Text(modifier = Modifier
            .constrainAs(content) { top.linkTo(image.bottom) }
            .fillMaxWidth()
            .padding(10.dp), text = news.content, maxLines = 3, color = Color.Black)

        Text(modifier = Modifier
            .constrainAs(author) {
                top.linkTo(content.bottom)
                start.linkTo(content.start)
            }
            .padding(10.dp, 4.dp, 10.dp, 10.dp), text = news.author)

        Text(modifier = Modifier
            .constrainAs(date) {
                top.linkTo(content.bottom)
                end.linkTo(content.end)
            }
            .padding(10.dp, 4.dp, 10.dp, 10.dp),
            text = DateTimeUtils.convertDateTime(
                news.publishedAt, SERVER_DATE_TIME_FORMAT, DAY_DATE_TIME_FORMAT
            ).orEmpty())
    }
}