package com.example.newsapp.manager

import android.net.ParseException
import android.util.Log
import com.google.gson.Gson
import com.google.gson.stream.MalformedJsonException
import com.example.newsapp.service.ResponseListener
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException

class NetworkManager {
    private var compositeDisposable: Disposable? = null
    private val tag: String = NetworkManager::class.java.simpleName

    // Function to call API and get response

    fun <V : Any> createAPIRequest(observables: Observable<V>, callBack: ResponseListener<V>) {
        callBack.onLoading(true)
        compositeDisposable = observables
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableObserver<V>() {
                override fun onNext(s: V) {
                    callBack.onResponseReceived(s, 0)
                    callBack.onLoading(false)
                }

                override fun onError(e: Throwable) {
                    callBack.onErrorReceived(setUpErrors(e), 0)
                    callBack.onLoading(false)
                }

                override fun onComplete() {
                    callBack.onLoading(false)
                }
            })
    }

    // Function to return error received in a proper format

    private fun setUpErrors(t: Throwable): ErrorModel {
        Log.e(tag, "setUpError statusCode: " + "statusCode " + t.message)
        val errorModel = ErrorModel()
        try {
            when (t) {
                is SocketTimeoutException -> {
                    errorModel.errorCode = ResponseCodes.INTERNET_NOT_AVAILABLE
                    errorModel.message = ResponseCodes.logErrorMessage(errorModel.errorCode)
                }
                is TimeoutException -> {
                    errorModel.errorCode = ResponseCodes.URL_CONNECTION_ERROR
                    errorModel.message = ResponseCodes.logErrorMessage(errorModel.errorCode)
                }
                is ClassCastException -> {
                    errorModel.errorCode = ResponseCodes.MODEL_TYPE_CAST_EXCEPTION
                    errorModel.message = ResponseCodes.logErrorMessage(errorModel.errorCode)
                }
                is MalformedJsonException -> {
                    errorModel.errorCode = ResponseCodes.MODEL_TYPE_CAST_EXCEPTION
                    errorModel.message = ResponseCodes.logErrorMessage(errorModel.errorCode)
                }
                is ParseException -> {
                    errorModel.errorCode = ResponseCodes.MODEL_TYPE_CAST_EXCEPTION
                    errorModel.message = ResponseCodes.logErrorMessage(errorModel.errorCode)
                }
                is UnknownHostException -> {
                    errorModel.errorCode = ResponseCodes.INTERNET_NOT_AVAILABLE
                    errorModel.message = ResponseCodes.logErrorMessage(errorModel.errorCode)
                }
                else -> {
                    Log.e("errorMessage", Gson().toJson(t))
                    val errorMessage = (t as HttpException).response()?.errorBody()!!.string()
                    val responseCode = t.response()?.code()
                    if (responseCode != null) {
                        errorModel.errorCode = responseCode
                    }
                    var errorObject = JSONObject(errorMessage)
                    var error = ErrorMessageJson(errorObject.getString("timestamp"),
                        errorObject.getString("status"),
                        if( errorObject.has("statusCode")) errorObject.getInt("statusCode") else errorObject.getInt("status"),
                        if(errorObject.has("message")) errorObject.getString("message") else errorObject.getString("error"),
                    )
                    errorModel.errorMessage = error
                }
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            errorModel.errorCode = ResponseCodes.UNKNOWN_ERROR
            errorModel.message = ResponseCodes.logErrorMessage(errorModel.errorCode)
        } finally {
//            progressHide()
        }
        return errorModel
    }
}