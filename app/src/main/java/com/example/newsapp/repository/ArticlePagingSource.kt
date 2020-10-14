package com.example.newsapp.repository

import androidx.paging.PagingSource
import com.example.newsapp.api.NewsService
import com.example.newsapp.model.Article
import com.example.newsapp.util.STARTING_PAGE_INDEX

class ArticlePagingSource(
    private val service: NewsService
) : PagingSource<Int, Article>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        val position = params.key ?: STARTING_PAGE_INDEX

        return try {
            val response =
                service.getBreakingNews(pageNumber = position, pageSize = params.loadSize)
                    .articles
//            Log.i("fusroda", "count: ${response.size}")

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
}