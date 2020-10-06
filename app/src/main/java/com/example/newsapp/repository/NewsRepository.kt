package com.example.newsapp.repository

import com.example.newsapp.api.Network
import com.example.newsapp.db.ArticleDatabase
import com.example.newsapp.model.Article

class NewsRepository(val db: ArticleDatabase) {

    suspend fun getBreakingNews(countryCode: String, pageNumber: Int) =
        Network.api.getBreakingNews(countryCode, pageNumber)

    suspend fun searchNews(searchQuery: String, pageNumber: Int) =
        Network.api.searchForNews(searchQuery, pageNumber)

    suspend fun saveArticle(article: Article) = db.articleDao.upsert(article)

    fun getAllArticles() = db.articleDao.getAllArticles()

    suspend fun deleteArticle(article: Article) = db.articleDao.delete(article)
}