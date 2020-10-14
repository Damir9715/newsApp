package com.example.newsapp.repository

import android.util.Log
import com.example.newsapp.api.Network
import com.example.newsapp.db.ArticleDatabase
import com.example.newsapp.model.Article
import com.example.newsapp.model.NewsResponse
import retrofit2.Response

class NewsRepository(val db: ArticleDatabase) {

    suspend fun getBreakingNews(
        countryCode: String,
        category: String,
        pageSize: Int,
        pageNumber: Int
    ): Response<NewsResponse> {
        Log.i("fusroda", "request")
        return Network.api.getBreakingNews(countryCode, category, pageSize, pageNumber)
    }


    suspend fun searchNews(searchQuery: String, pageNumber: Int) =
        Network.api.searchForNews(searchQuery, pageNumber)

    suspend fun saveArticle(article: Article) = db.articleDao.upsert(article)

    fun getAllArticles() = db.articleDao.getAllArticles()

    suspend fun deleteArticle(article: Article) = db.articleDao.delete(article)
}