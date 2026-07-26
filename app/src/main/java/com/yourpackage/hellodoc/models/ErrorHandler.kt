package com.yourpackage.hellodoc.utils

import android.content.Context
import android.widget.Toast
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

object ErrorHandler {

    fun handleError(context: Context, throwable: Throwable): String {
        return when (throwable) {
            is SocketTimeoutException -> "Connection timeout. Please try again."
            is ConnectException -> "Unable to connect to server. Check your internet."
            is UnknownHostException -> "No internet connection."
            is HttpException -> {
                when (throwable.code()) {
                    401 -> "Session expired. Please login again."
                    403 -> "Access denied."
                    404 -> "Resource not found."
                    500 -> "Server error. Please try again later."
                    else -> "Error: ${throwable.code()}"
                }
            }
            else -> "An unexpected error occurred. Please try again."
        }.also { message ->
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }
    }
}