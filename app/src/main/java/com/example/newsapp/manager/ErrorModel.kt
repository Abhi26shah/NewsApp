package com.example.newsapp.manager

class ErrorModel {
    var errorCode: Int = 0
    var errorMessage : ErrorMessageJson? = null
    var message : String = ""
    override fun toString(): String {
        return "ErrorModel(errorCode=$errorCode, errorMessage=$errorMessage, message='$message')"
    }

}



data class ErrorMessageJson(var timestamp: String, var status: String,var statusCode: Int,var message: String) {

}