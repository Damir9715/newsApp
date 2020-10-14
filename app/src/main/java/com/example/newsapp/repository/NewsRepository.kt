package com.example.newsapp.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.newsapp.api.Network
import com.example.newsapp.api.NewsService
import com.example.newsapp.db.ArticleDatabase
import com.example.newsapp.model.Article
import com.example.newsapp.util.NETWORK_PAGE_SIZE
import kotlinx.coroutines.flow.Flow

class NewsRepository(private val db: ArticleDatabase, private val service: NewsService) {

    fun getBreakingNews(): Flow<PagingData<Article>> {
        return Pager(
            config = PagingConfig(pageSize = NETWORK_PAGE_SIZE, enablePlaceholders = false),
            pagingSourceFactory = { ArticlePagingSource(service) }
        ).flow
    }


    suspend fun searchNews(searchQuery: String, pageNumber: Int) =
        Network.api.searchForNews(searchQuery, pageNumber)

    suspend fun saveArticle(article: Article) = db.articleDao.upsert(article)

    fun getAllArticles() = db.articleDao.getAllArticles()

    suspend fun deleteArticle(article: Article) = db.articleDao.delete(article)
}