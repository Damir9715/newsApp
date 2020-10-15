package com.example.newsapp.repository

import androidx.paging.PagingSource
import com.example.newsapp.api.NewsService
import com.example.newsapp.model.Article
import com.example.newsapp.util.STARTING_PAGE_INDEX

class ArticlePagingSource(
    private val service: NewsService,
    private val request: RequestType,
    private val searchQuery: String = ""
) : PagingSource<Int, Article>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        val position = params.key ?: STARTING_PAGE_INDEX

        return try {

            val response = when (request) {
                RequestType.BREAKING_NEWS -> {
                    service
                        .getBreakingNews(pageNumber = position, pageSize = params.loadSize)
                        .articles
                }
                RequestType.SEARCH_NEWS -> {
                    service
                        .searchForNews(
                            query = searchQuery,
                            pageNumber = position,
                            pageSize = params.loadSize
                        )
                        .articles
                }
            }

            LoadResult.Page(
                data = response,
                prevKey = if (position == STARTING_PAGE_INDEX) null else position - 1,
                nextKey = when {
                    response.isEmpty() -> null
                    position == STARTING_PAGE_INDEX -> position + 3 // first requests load 3*n pages
                    else -> position + 1
                }
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }

    enum class RequestType(name: String) {
        BREAKING_NEWS("breaking_news"),
        SEARCH_NEWS("search_news")
    }
}