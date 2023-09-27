package com.example.newsapp.retrofit

import android.content.Context
import com.example.newsapp.BuildConfig
import com.google.gson.Gson
import com.example.newsapp.utility.Logger
import okhttp3.Headers
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import okio.Buffer
import org.json.JSONObject
import java.io.EOFException
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets


/**
 * Created by Abhishek Shah on 28 March 2023
 *
 * Thinkitive Technologies Pvt. Ltd.
 */
class NewsInterceptor(context: Context) : Interceptor {
    var context: Context

    init {
        this.context = context
    }

    companion object {
        /**
         * Provides the class name as TAG variable whenever required.
         *
         * Mostly used for Logging the data in LogCat.
         */
        private const val TAG = "TRSTInterceptor"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
//        TODO("Not yet implemented")
        val request = chain.request()
        val response = chain.proceed(request)
        val rawJson = response.body?.string()

        if (request.headers["x-api-key"].isNullOrEmpty()) {
            val url = chain.request().url.newBuilder().addQueryParameter("country","in").build()
            val builder = request.newBuilder().url(url)
            builder.header("x-api-key", BuildConfig.API_KEY)
            return chain.proceed(builder.build())
        }

        Logger.info(TAG, "--->" + request.method + " " + request.url)
        Logger.info(TAG, Gson().toJson(request.headers))
        request.body?.let {
            if (bodyHasUnknownEncoding(request.headers)) {
                Logger.info(TAG, "--> END ${request.method} (encoded body omitted)")
            } else if (it.isDuplex()) {
                Logger.info(TAG, "--> END ${request.method} (duplex request body omitted)")
            } else if (it.isOneShot()) {
                Logger.info(TAG, "--> END ${request.method} (one-shot body omitted)")
            } else {
                val buffer = Buffer()
                it.writeTo(buffer)

                val contentType = it.contentType()
                val charset: Charset =
                    contentType?.charset(StandardCharsets.UTF_8) ?: StandardCharsets.UTF_8

                Logger.info(TAG, "")
                if (buffer.isProbablyUtf8()) {
                    Logger.info(TAG, buffer.readString(charset))
                    Logger.info(TAG, "--> END ${request.method} (${it.contentLength()}-byte body)")
                } else {
                    Logger.info(
                        TAG,
                        "--> END ${request.method} (binary ${it.contentLength()}-byte body omitted)"
                    )
                }
            }
        }

        Logger.info(TAG, "<---" + response.code + " " + request.url)
        try {
            Logger.info("RawJson", rawJson.toString())
            val jsonResponse = JSONObject(rawJson.toString())
            Logger.info(TAG, jsonResponse.toString(4))
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return response.newBuilder().body(rawJson?.toResponseBody(response.body?.contentType()))
            .build()
    }

    private fun Buffer.isProbablyUtf8(): Boolean {
        try {
            val prefix = Buffer()
            val byteCount = size.coerceAtMost(64)
            copyTo(prefix, 0, byteCount)
            for (i in 0 until 16) {
                if (prefix.exhausted()) {
                    break
                }
                val codePoint = prefix.readUtf8CodePoint()
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false
                }
            }
            return true
        } catch (_: EOFException) {
            return false // Truncated UTF-8 sequence.
        }
    }

    private fun bodyHasUnknownEncoding(headers: Headers): Boolean {
        val contentEncoding = headers["Content-Encoding"] ?: return false
        return !contentEncoding.equals("identity", ignoreCase = true) &&
                !contentEncoding.equals("gzip", ignoreCase = true)
    }
}