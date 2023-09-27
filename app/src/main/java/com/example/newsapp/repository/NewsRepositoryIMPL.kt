package com.example.newsapp.repository

import android.content.Context
import com.example.newsapp.manager.NetworkManager
import com.example.newsapp.models.NewsHeadlineResponse
import com.example.newsapp.retrofit.APIUtils
import com.example.newsapp.service.ResponseListener


/**
 * Created by Abhishek Shah on 27 September 2023
 *
 * Thinkitive Technologies Pvt. Ltd.
 */
class NewsRepositoryIMPL : NewsRepository {
    companion object {
        /**
         * Provides the class name as TAG variable whenever required.
         *
         * Mostly used for Logging the data in LogCat.
         */
        private const val TAG = "NewsRepositoryIMPL"
        val manager = NetworkManager()
    }

    override fun getTopHeadlines(
        context: Context,
        query: HashMap<String, Any>,
        callback: ResponseListener<NewsHeadlineResponse>
    ) {
//        TODO("Not yet implemented")
        manager.createAPIRequest(
            APIUtils.getUserApiService(context = context).getTopHeadlines(
                query = query
            ),
            callback
        )
    }


}