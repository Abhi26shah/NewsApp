package com.example.newsapp.service

import com.example.newsapp.manager.ErrorModel

interface ResponseListener<T> {
    fun onResponseReceived(response: T, requestCode: Int)
    fun onErrorReceived(error: ErrorModel, requestCode: Int)

    fun onLoading(isLoading : Boolean){}
}