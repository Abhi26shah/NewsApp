package com.example.newsapp.service

import com.example.newsapp.BuildConfig
import com.example.newsapp.models.NewsHeadlineResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface UserAPIService {

    @GET("top-headlines")
    fun getTopHeadlines(
        @QueryMap query: HashMap<String, Any> = hashMapOf()
    ): Observable<NewsHeadlineResponse>

}