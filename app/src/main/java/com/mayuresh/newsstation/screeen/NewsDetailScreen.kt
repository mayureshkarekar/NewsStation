package com.mayuresh.newsstation.screeen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
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

@Composable
fun NewsDetail(news: News) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {
        val (image, title, content, author, date) = createRefs()

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
                .padding(10.dp, 2.dp),
            text = news.title
        )

        Text(
            modifier = Modifier
                .constrainAs(content) { top.linkTo(title.bottom) }
                .fillMaxWidth()
                .padding(10.dp, 2.dp),
            text = news.content
        )

        Text(
            modifier = Modifier
                .constrainAs(author) { top.linkTo(content.bottom) }
                .padding(10.dp, 2.dp),
            text = news.author
        )

        Text(
            modifier = Modifier
                .constrainAs(date) {
                    top.linkTo(content.bottom)
                    end.linkTo(content.end)
                }
                .padding(10.dp, 2.dp),
            text = news.publishedAt
        )
    }
}