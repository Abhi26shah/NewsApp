package com.example.newsapp.repository

import android.content.Context
import com.example.newsapp.models.NewsHeadlineResponse
import com.example.newsapp.service.ResponseListener


/**
 * Created by Abhishek Shah on 27 September 2023
 *
 * Thinkitive Technologies Pvt. Ltd.
 */
interface NewsRepository {

    fun getTopHeadlines(
        context: Context,
        query: HashMap<String,Any>,
        callback: ResponseListener<NewsHeadlineResponse>
    )
}