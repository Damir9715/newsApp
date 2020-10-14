package com.example.newsapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.newsapp.model.Article
import com.example.newsapp.repository.NewsRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class NewsViewModel(private val repository: NewsRepository) : ViewModel() {

//    private var currentSearchResult: Flow<PagingData<Article>>? = null

    fun getBreakingNews(): Flow<PagingData<Article>> {
        return repository.getBreakingNews().cachedIn(viewModelScope)
    }
}

//    private fun handleSearchNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
//        if (response.isSuccessful) {
//            response.body()?.let { resultResponse ->
//                searchNewsPage++
//                if (seaarchNewsResponse == null) {
//                    seaarchNewsResponse = resultResponse
//                } else {
//                    seaarchNewsResponse?.articles?.addAll(resultResponse.articles)
//                }
//                return Resource.Success(seaarchNewsResponse ?: resultResponse)
//            }
//        }
//        return Resource.Error(response.message())
//    }

//fun saveArticle(article: Article) = viewModelScope.launch {
//    repository.saveArticle(article)
//}
//
//fun getAllArticles() = repository.getAllArticles()
//
//fun deleteArticles(article: Article) = viewModelScope.launch {
//    repository.deleteArticle(article)
//}
//}