package com.example.newsapp.repository

import com.example.newsapp.api.Network
import com.example.newsapp.db.ArticleDatabase

class NewsRepository(val db: ArticleDatabase) {

    suspend fun getBreakingNews(countryCode: String, pageNumber: Int) =
        Network.api.getBreakingNews(countryCode, pageNumber)

    suspend fun searchNews(searchQuery: String, pageNumber: Int) =
        Network.api.searchForNews(searchQuery, pageNumber)
}