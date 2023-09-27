package com.example.newsapp.utility

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest


/**
 * Created by Abhishek Shah on 27 September 2023
 *
 * Thinkitive Technologies Pvt. Ltd.
 */

@Composable
fun loadRemoteImage(context: Context, url: String): AsyncImagePainter {
    return rememberAsyncImagePainter (
        remember(url) {
            ImageRequest.Builder(context)
                .data(url)
                .allowConversionToBitmap(true)
                .build()
        }
    )
}