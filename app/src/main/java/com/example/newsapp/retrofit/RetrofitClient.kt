package com.example.newsapp.retrofit

import android.content.Context
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

abstract class RetrofitClient {
    companion object {
        lateinit var retrofit: Retrofit
        fun getClient(baseUrl: String, context: Context): Retrofit {
            val gson = GsonBuilder().setLenient().create()
            retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okClient(true, context))
                .build()
            return retrofit
        }


        fun getClient(baseUrl: String, isAddInterceptor: Boolean, context: Context): Retrofit {
            val gson = GsonBuilder().setLenient().create()
            retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okClient(isAddInterceptor, context))
                .build()
            return retrofit
        }

        private fun okClient(isAddInterceptor: Boolean, context: Context): OkHttpClient {
//            val loggingInterceptor = HttpLoggingInterceptor()
            val loggingInterceptor= NewsInterceptor(context)
//            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            val httpClient = OkHttpClient.Builder()
            httpClient.connectTimeout(1, TimeUnit.MINUTES)
            httpClient.readTimeout(1, TimeUnit.MINUTES)
            if (isAddInterceptor)
                httpClient.addInterceptor(loggingInterceptor)
            return httpClient.build()
        }

    }
}