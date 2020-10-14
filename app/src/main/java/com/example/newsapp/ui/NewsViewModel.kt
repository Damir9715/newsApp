package com.example.newsapp.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.newsapp.model.Article
import com.example.newsapp.model.NewsResponse
import com.example.newsapp.repository.NewsRepository
import com.example.newsapp.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class NewsViewModel(val repository: NewsRepository) : ViewModel() {

    companion object {
        private const val VISIBLE_THRESHOLD = 5
    }

    val breakingNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var breakingNewsPage = 1
    var breakingNewsResponse: NewsResponse? = null

    val searchNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var searchNewsPage = 1
    var seaarchNewsResponse: NewsResponse? = null

    var isRequestInProgress = false

    init {
        getBreakingNews("ru")
    }


    private fun getBreakingNews(countryCode: String) = viewModelScope.launch {
        if (!isRequestInProgress) {
            breakingNews.postValue(Resource.Loading())
            isRequestInProgress = true
            val response = repository.getBreakingNews(countryCode = countryCode, category = "health", pageSize = 10, pageNumber = breakingNewsPage)
            breakingNews.postValue(handleBreakingNewsResponse(response))
        }
    }

    fun searchNews(searchQuery: String) = viewModelScope.launch {
        searchNews.postValue(Resource.Loading())
        val response = repository.searchNews(searchQuery, searchNewsPage)
        searchNews.postValue(handleSearchNewsResponse(response))
    }

    private fun handleBreakingNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                breakingNewsPage++
                if (breakingNewsResponse == null) {
                    breakingNewsResponse = resultResponse
                    isRequestInProgress = false
                } else {
                    breakingNewsResponse?.articles?.addAll(resultResponse.articles)
                    isRequestInProgress = false
                }
                return Resource.Success(breakingNewsResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleSearchNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                searchNewsPage++
                if (seaarchNewsResponse == null) {
                    seaarchNewsResponse = resultResponse
                } else {
                    seaarchNewsResponse?.articles?.addAll(resultResponse.articles)
                }
                return Resource.Success(seaarchNewsResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    fun saveArticle(article: Article) = viewModelScope.launch {
        repository.saveArticle(article)
    }

    fun getAllArticles() = repository.getAllArticles()

    fun deleteArticles(article: Article) = viewModelScope.launch {
        repository.deleteArticle(article)
    }

    fun listScrolled(visibleItemCount: Int, lastVisibleItem: Int, totalItemCount: Int) {
        if (totalItemCount <= visibleItemCount + lastVisibleItem + VISIBLE_THRESHOLD) {
            getBreakingNews("ru")
        }
    }
}