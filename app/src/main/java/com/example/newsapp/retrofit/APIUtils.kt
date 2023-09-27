package com.example.newsapp.retrofit

import android.content.Context
import com.example.newsapp.BuildConfig
import com.example.newsapp.service.UserAPIService

class APIUtils {
    companion object {
        val BASE_URL = BuildConfig.BASE_URL

        fun getUserApiService(context: Context): UserAPIService {
            return RetrofitClient.getClient(BASE_URL, context).create(UserAPIService::class.java)
        }

    }
}