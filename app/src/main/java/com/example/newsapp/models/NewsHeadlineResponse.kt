package com.example.newsapp.models

import com.google.gson.annotations.SerializedName


/**
 * Created by Abhishek Shah on 27 September 2023
 *
 * Thinkitive Technologies Pvt. Ltd.
 */
data class NewsHeadlineResponse(
    @SerializedName("status")
    val status: String,
    @SerializedName("totalResults")
    val totalResults: Int,
    @SerializedName("articles")
    val articles: ArrayList<Articles>,
)
