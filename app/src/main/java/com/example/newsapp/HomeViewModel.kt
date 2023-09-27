package com.example.newsapp

import android.content.Context
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import com.example.newsapp.manager.ErrorModel
import com.example.newsapp.models.Articles
import com.example.newsapp.models.NewsHeadlineResponse
import com.example.newsapp.repository.NewsRepository
import com.example.newsapp.repository.NewsRepositoryIMPL
import com.example.newsapp.service.ResponseListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


/**
 * Created by Abhishek Shah on 27 September 2023
 *
 * Thinkitive Technologies Pvt. Ltd.
 */
class HomeViewModel: ViewModel() {

    private var _newsHeadlineList = SnapshotStateList<Articles>()
    val newsHeadlineList: SnapshotStateList<Articles> = _newsHeadlineList

    fun getTopHeadlines(context: Context, page: Int = 1, pageSize: Int = 20) {
        val query = hashMapOf<String, Any>()
        query["page"] = page
        query["pageSize"] = pageSize
        newsRepository.getTopHeadlines(context = context,
            query = query,
            object : ResponseListener<NewsHeadlineResponse> {
                override fun onResponseReceived(response: NewsHeadlineResponse, requestCode: Int) {
//                    TODO("Not yet implemented")
                    _newsHeadlineList.addAll(response.articles)
                }

                override fun onErrorReceived(error: ErrorModel, requestCode: Int) {
//                    TODO("Not yet implemented")
                }
            })
    }

    fun openURL(context: Context, url: String?) {
        if (!url.isNullOrEmpty()) {
            val builder = CustomTabsIntent.Builder()
            val customTabsIntent = builder.build()
            customTabsIntent.launchUrl(context, Uri.parse(url))
        }
    }

    companion object {
        val newsRepository: NewsRepository = NewsRepositoryIMPL()
    }
}