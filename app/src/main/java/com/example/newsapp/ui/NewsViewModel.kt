package com.example.newsapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.newsapp.model.Article
import com.example.newsapp.repository.NewsRepository
import kotlinx.coroutines.flow.Flow

class NewsViewModel(private val repository: NewsRepository) : ViewModel() {

    var currentQueryValue: String? = null
    private var currentSearchResult: Flow<PagingData<Article>>? = null

    fun getBreakingNews(): Flow<PagingData<Article>> {
        return repository.getBreakingNews().cachedIn(viewModelScope)
    }

    fun searchNews(query: String): Flow<PagingData<Article>> {
        val lastResult = currentSearchResult
        if (query == currentQueryValue && lastResult != null) {
            return lastResult
        }
        currentQueryValue = query
        val newResult = repository.searchNews(query).cachedIn(viewModelScope)
        currentSearchResult = newResult
        return newResult
    }
}
