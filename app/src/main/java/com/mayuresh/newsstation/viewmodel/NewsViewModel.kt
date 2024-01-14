package com.mayuresh.newsstation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mayuresh.newsstation.model.News
import com.mayuresh.newsstation.repository.NewsRepository
import com.mayuresh.newsstation.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(private val newsRepository: NewsRepository) : ViewModel() {
    val news: StateFlow<Resource<List<News>>>
        get() = newsRepository.news

    init {
        viewModelScope.launch {
            newsRepository.getNews()
        }
    }
}