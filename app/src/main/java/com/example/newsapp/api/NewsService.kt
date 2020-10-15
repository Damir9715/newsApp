package com.example.newsapp.api

import com.example.newsapp.model.NewsResponse
import com.example.newsapp.util.API_KEY
import com.example.newsapp.util.CATEGORY
import com.example.newsapp.util.COUNTRY_CODE
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsService {

    @GET("v2/top-headlines")
    suspend fun getBreakingNews(
        @Query("country") countryCode: String = COUNTRY_CODE,
        @Query("category") category: String = CATEGORY,
        @Query("pageSize") pageSize: Int,
        @Query("page") pageNumber: Int,
        @Query("apiKey") apiKey: String = API_KEY
    ): NewsResponse

    @GET("v2/everything")
    suspend fun searchForNews(
        @Query("q") query: String,
        @Query("pageSize") pageSize: Int,
        @Query("page") pageNumber: Int,
        @Query("apiKey") apiKey: String = API_KEY
    ): NewsResponse
}