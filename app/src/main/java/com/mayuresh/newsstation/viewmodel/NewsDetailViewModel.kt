package com.mayuresh.newsstation.viewmodel

import androidx.lifecycle.SavedStateHandle
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
class NewsDetailViewModel @Inject constructor(
    private val newsRepository: NewsRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    val newsDetails: StateFlow<Resource<News>>
        get() = newsRepository.newsDetail

    init {
        val newsId = savedStateHandle.get<Long>("news_id") ?: -1
        viewModelScope.launch {
            newsRepository.getNewsDetail(newsId)
        }
    }
}